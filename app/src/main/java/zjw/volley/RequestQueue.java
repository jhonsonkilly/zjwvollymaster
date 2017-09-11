package zjw.volley;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by mac on 2017/8/25.
 */

public class RequestQueue<T> {

    private final AtomicInteger mSequenceGenerator = new AtomicInteger();

    public void add(Request<T> request) {


        request.setQueue(this);

        request.setNumber(mSequenceGenerator.incrementAndGet());



    }
}
