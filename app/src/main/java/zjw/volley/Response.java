package zjw.volley;

import java.util.Map;

/**
 * Created by mac on 2017/8/24.
 */

public class Response {


    final int statusCode;

    final byte[] data;

    final Map<String, String> headers;

    final boolean notModified;

    final long networkTimeMs;


    public Response(int statusCode, byte[] data, Map<String, String> headers,
                    boolean notModified, long networkTimeMs) {
        this.statusCode = statusCode;
        this.data = data;
        this.headers = headers;
        this.notModified = notModified;
        this.networkTimeMs = networkTimeMs;
    }
}
