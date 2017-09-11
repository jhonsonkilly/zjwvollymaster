package zjw.volley;

import android.os.SystemClock;



import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by mac on 2017/8/24.
 */

public class BaseConnection implements NetWork {

    HttpStack stack;

    BaseConnection(HttpStack httpStack) {
        this.stack = httpStack;
    }


    @Override
    public void preformNetWork(Request<?> request) throws IOException {
        long requestStart = SystemClock.elapsedRealtime();
        //所有的返回头
        Map<String, String> allHeaders;
        //加入缓存的
        Map<String, String> responseHeaders = Collections.emptyMap();


        putHeaderIntoCache(responseHeaders, request.getCacheEntry());
        HttpResponse httpResponse = stack.performRequest(request, responseHeaders);

        allHeaders = sortAllHeaders(httpResponse.getAllHeaders());
        int code = httpResponse.getStatusLine().getStatusCode();
        long requestEnd = SystemClock.elapsedRealtime();
        if(code== HttpStatus.SC_NOT_MODIFIED){
            //寻找缓存的key对应的entry
            return new Response();
        }
        if(httpResponse.getEntity()!=null){

        }

        return new Response(code, request.getCacheEntry().data, request.getCacheEntry().lastModified, allHeaders, requestEnd - requestStart);

    }

    void putHeaderIntoCache(Map<String, String> map, Cache.Entry entry) {
        //把返回的相应头的字段存起来
        if (entry == null) {
            return;
        }

        if (entry.lastModified > 0) {
            Date refTime = new Date(entry.lastModified);
            map.put("If-Modified-Since", DateUtils.formatDate(refTime));
        }

    }

    Map<String, String> sortAllHeaders(Header[] headers) {

        TreeMap<String, String> map = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < headers.length; i++) {
            map.put(headers[i].getName(), headers[i].getValue());
        }
        return map;
    }

}
