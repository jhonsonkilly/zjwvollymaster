package zjw.volley;

import android.support.annotation.NonNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Map;

/**
 * Created by mac on 2017/8/24.
 */

public abstract class Request<T> implements Comparable<Request<T>> {


    RetryPolicy policy;

    Cache.Entry mCacheEntry;


    RequestQueue requestQueue;

    int count;

    int mthtod;

    private static final String DEFAULT_PARAMS_ENCODING = "UTF-8";


    public interface Method {
        int DEPRECATED_GET_OR_POST = -1;
        int GET = 0;
        int POST = 1;
        int PUT = 2;
        int DELETE = 3;
        int HEAD = 4;
        int OPTIONS = 5;
        int TRACE = 6;
        int PATCH = 7;
    }

    Request(int method) {
        this.mthtod = method;
        setPolicy(new DefaultRetryPolicy());
    }

    String url;

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeaders() {
        return Collections.emptyMap();
    }


    public int getTimeoutMs() {


        return policy.getTimeOutMs();
    }

    public Request<?> setPolicy(RetryPolicy policy) {
        this.policy = policy;
        return this;
    }

    public Request<?> setCacheEntry(Cache.Entry entry) {
        mCacheEntry = entry;
        return this;
    }

    public Cache.Entry getCacheEntry() {
        return mCacheEntry;
    }

    public void setNumber() {

    }

    @Override
    public int compareTo(@NonNull Request<T> tRequest) {
        Priority left = this.getPriority();
        Priority right = tRequest.getPriority();
        if (left == right) {
            return count - tRequest.count;
        } else {
            return right.ordinal() - left.ordinal();
        }

    }

    public enum Priority {
        LOW,
        NORMAL,
        HIGH,
        IMMEDIATE
    }

    public int getMethod() {
        return mthtod;
    }


    public Priority getPriority() {
        return Priority.NORMAL;
    }

    public Request<?> setQueue(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
        return this;
    }


    public void setNumber(int number) {
        this.count = number;
    }

    public byte[] getBody() {
        Map<String, String> postParams = getParms();
        if (postParams != null && postParams.size() > 0) {
            return encodeParameters(getParms(), getParamsEncoding());
        }
        return null;

    }

    private byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                encodedParams.append('&');
            }
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }

    //有post需求的需要重写此方法
    public Map<String, String> getParms() {
        return null;
    }

    protected String getParamsEncoding() {
        return DEFAULT_PARAMS_ENCODING;
    }

    /**
     * Returns the content type of the POST or PUT body.
     */
    public String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
    }


}
