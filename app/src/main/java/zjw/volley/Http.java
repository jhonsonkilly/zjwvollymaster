package zjw.volley;


import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Created by mac on 2017/8/24.
 */

public interface Http {

    HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders)
            throws IOException;
}
