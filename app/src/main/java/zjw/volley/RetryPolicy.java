package zjw.volley;

/**
 * Created by mac on 2017/8/24.
 */

public interface RetryPolicy {

    void retry(Exception error);

    int getTimeOutMs();


}
