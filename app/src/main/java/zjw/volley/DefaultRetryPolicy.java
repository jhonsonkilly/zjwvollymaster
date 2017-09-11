package zjw.volley;

/**
 * Created by mac on 2017/8/24.
 */

public class DefaultRetryPolicy implements RetryPolicy {

    private int mCurrentTimeoutMs;


    private int mCurrentRetryCount;


    private final int mMaxNumRetries;

    private final float mBackoff;


    /**
     * The default socket timeout in milliseconds
     */
    public static final int DEFAULT_TIMEOUT_MS = 2500;

    /**
     * The default number of retries
     */
    public static final int DEFAULT_MAX_RETRIES = 1;

    /**
     * The default backoff multiplier
     */
    public static final float DEFAULT_BACKOFF_MULT = 1f;


    DefaultRetryPolicy() {

        this(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT);
    }

    DefaultRetryPolicy(int timeout, int retries, float backoff) {
        this.mCurrentTimeoutMs = timeout;
        this.mMaxNumRetries = retries;
        this.mBackoff = backoff;
    }

    @Override
    public void retry(Exception error) {

    }

    @Override
    public int getTimeOutMs() {
        return mCurrentTimeoutMs;
    }


}
