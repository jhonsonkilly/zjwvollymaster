package zjw.volley;

import java.io.IOException;

/**
 * Created by mac on 2017/8/24.
 */

public interface NetWork {

    void preformNetWork(Request<?> request) throws IOException;
}
