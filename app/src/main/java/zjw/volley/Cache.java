package zjw.volley;

import java.util.Collections;
import java.util.Map;

/**
 * Created by mac on 2017/8/23.
 */

public interface Cache {


    Entry get(String key);


    void put(String key, Entry entry);


    void clear();


    void remove(String key);


    class Entry {


        long ttl;

        byte[] data;


        public long lastModified;

        Map<String, String> headerMap = Collections.EMPTY_MAP;

    }
}
