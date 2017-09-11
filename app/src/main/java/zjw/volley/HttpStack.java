package zjw.volley;

import com.example.myapplication.Request.Method;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mac on 2017/8/24.
 */

public class HttpStack implements Http {

    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    final ReWriteUrl reWriteUrl;



    interface ReWriteUrl {
        String rewriteUrl(String url);
    }

    HttpStack(ReWriteUrl reWriteUrl) {
        this.reWriteUrl = reWriteUrl;
    }

    HttpStack() {
        this(null);
    }

    //最底层的业务，不惜要关心缓存,建立链接,设置entity和设置头信息,设置了post参数的信息
    @Override
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException {
        HashMap<String, String> header = new HashMap<>();

        header.putAll(additionalHeaders);
        header.putAll(request.getHeaders());

        String url;
        url = request.getUrl();
        if (reWriteUrl != null) {
            String reUrl = reWriteUrl.rewriteUrl(url);
            if (reUrl == null) {
                throw new IOException("URL blocked by rewriter: " + url);
            }
            url = reUrl;
        }

        URL parsedUrl = new URL(url);
        HttpURLConnection connection = openConnection(parsedUrl, request);


        for (Map.Entry<String, String> headers : header.entrySet()) {
            if (headers.getKey() != null) {

                connection.addRequestProperty(headers.getKey(), headers.getValue());

            }
        }

        setConnectionParametersForRequest(connection, request);
        //添加请求方式
        ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
        int responseCode = connection.getResponseCode();
        if (responseCode == -1) {
            // -1 is returned by getResponseCode() if the response code could not be retrieved.
            // Signal to the caller that something was wrong with the connection.
            throw new IOException("Could not retrieve response code from HttpUrlConnection.");
        }
        StatusLine responseStatus = new BasicStatusLine(protocolVersion,
                connection.getResponseCode(), connection.getResponseMessage());
        BasicHttpResponse response = new BasicHttpResponse(responseStatus);
        if (hasResponseBody(request.getMethod(), responseStatus.getStatusCode())) {
            response.setEntity(entityFromConnection(connection));
        }
        Map<String, List<String>> fields = connection.getHeaderFields();
        for (Map.Entry<String, List<String>> headers : fields.entrySet()) {
            if (headers.getKey() != null) {

                Header h = new BasicHeader(headers.getKey(), headers.getValue().get(0));

                response.addHeader(h);
            }
        }

        Header h = new BasicHeader("Cache-Control", "max-age=31536000");
        response.addHeader(h);


        return response;


    }


    private static boolean hasResponseBody(int requestMethod, int responseCode) {
        return requestMethod != Request.Method.HEAD
                && !(HttpStatus.SC_CONTINUE <= responseCode && responseCode < HttpStatus.SC_OK)
                && responseCode != HttpStatus.SC_NO_CONTENT
                && responseCode != HttpStatus.SC_NOT_MODIFIED;
    }


    public HttpURLConnection openConnection(URL url, Request<?> request) throws IOException {


        HttpURLConnection connection = createConnection(url);

        int timeoutMs = request.getTimeoutMs();
        connection.setConnectTimeout(timeoutMs);
        connection.setReadTimeout(timeoutMs);
        connection.setUseCaches(false);
        connection.setDoInput(true);


        /*if ("https".equals(url.getProtocol()) && mSslSocketFactory != null) {
            ((HttpsURLConnection) connection).setSSLSocketFactory(mSslSocketFactory);
        }*/

        return connection;
    }

    private static HttpEntity entityFromConnection(HttpURLConnection connection) {
        BasicHttpEntity entity = new BasicHttpEntity();
        InputStream inputStream;
        try {
            inputStream = connection.getInputStream();
        } catch (IOException ioe) {
            inputStream = connection.getErrorStream();
        }
        entity.setContent(inputStream);
        entity.setContentLength(connection.getContentLength());
        entity.setContentEncoding(connection.getContentEncoding());
        entity.setContentType(connection.getContentType());
        return entity;
    }

    protected HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();


        return connection;
    }

    static void setConnectionParametersForRequest(HttpURLConnection connection,
                                                  Request<?> request) throws IOException {
        switch (request.getMethod()) {
            case Request.Method.DEPRECATED_GET_OR_POST:
                // This is the deprecated way that needs to be handled for backwards compatibility.
                // If the request's post body is null, then the assumption is that the request is
                // GET.  Otherwise, it is assumed that the request is a POST.
                byte[] postBody = request.getBody();
                if (postBody != null) {
                    connection.setRequestMethod("POST");
                    addBody(connection, request, postBody);
                }
                break;
            case Method.GET:
                // Not necessary to set the request method because connection defaults to GET but
                // being explicit here.
                connection.setRequestMethod("GET");
                break;
            case Method.DELETE:
                connection.setRequestMethod("DELETE");
                break;
            case Method.POST:
                connection.setRequestMethod("POST");
                addBodyIfExists(connection, request);
                break;
            case Method.PUT:
                connection.setRequestMethod("PUT");
                addBodyIfExists(connection, request);
                break;
            case Method.HEAD:
                connection.setRequestMethod("HEAD");
                break;
            case Method.OPTIONS:
                connection.setRequestMethod("OPTIONS");
                break;
            case Method.TRACE:
                connection.setRequestMethod("TRACE");
                break;
            case Method.PATCH:
                connection.setRequestMethod("PATCH");
                addBodyIfExists(connection, request);
                break;
            default:
                throw new IllegalStateException("Unknown method type.");
        }
    }

    private static void addBodyIfExists(HttpURLConnection connection, Request<?> request)
            throws IOException {
        byte[] body = request.getBody();
        if (body != null) {
            addBody(connection, request, body);
        }
    }

    private static void addBody(HttpURLConnection connection, Request<?> request, byte[] body)
            throws IOException {
        // Prepare output. There is no need to set Content-Length explicitly,
        // since this is handled by HttpURLConnection using the size of the prepared
        // output stream.
        connection.setDoOutput(true);
        connection.addRequestProperty(HEADER_CONTENT_TYPE, request.getBodyContentType());
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.write(body);
        out.close();
    }
}
