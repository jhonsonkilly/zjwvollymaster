package zjw.volley;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by mac on 2017/8/23.
 */

public class BasedDiskCache implements Cache {


    private final Map<String, Header> mEntries = new LinkedHashMap(16, 0.75f, true);

    @Override
    public Entry get(String key) {
        Header header = mEntries.get(key);
        return header.getDataIntoEntrys();
    }

    @Override
    public void put(String key, Entry entry) {

        Header e = new Header(key, entry);
        mEntries.put(key, e);

    }

    @Override
    public void clear() {

        mEntries.clear();
    }

    @Override
    public void remove(String key) {

        mEntries.remove(key);
    }


    static class Header {
        final long ttl;
        final byte[] data;
        final long lastModified;
        final Map<String, String> headerMap;
        final String key;

        Header(String key, long ttl, byte[] data, long lastModified, Map<String, String> headerMap) {
            this.key = key;
            this.ttl = ttl;
            this.data = data;
            this.lastModified = lastModified;
            this.headerMap = headerMap;

        }

        Header(String key, Entry entry) {
            this(key, entry.ttl, entry.data, entry.lastModified, entry.headerMap);

        }

        Entry getDataIntoEntrys() {
            Entry e = new Entry();
            e.data = data;
            e.ttl = ttl;
            e.headerMap = headerMap;
            e.lastModified = lastModified;
            return e;
        }
    }
}
