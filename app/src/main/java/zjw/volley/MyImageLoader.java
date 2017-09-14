package zjw.volley;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

/**
 * Created by mac on 2017/9/12.
 */

public class MyImageLoader {

    RequestQueue queue;

    ImageCache imageCache;

    interface ImageCache {


        void put(String key, Bitmap bitmap);

        Bitmap get(String key);
    }

    public ImageContainer get(RequestQueue queue, ImageCache imageCache) {
        this.queue = queue;
        this.imageCache = imageCache;

        Request<Bitmap> newRequest = makeImageRequest(requestUrl, maxWidth, maxHeight, scaleType,
                cacheKey);
        return new ImageContainer()
    }


    public class ImageContainer {
        private Bitmap bitmap;

        ImageContainer(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }
    }
    protected Request<Bitmap> makeImageRequest(String requestUrl, int maxWidth, int maxHeight,
                                               ImageView.ScaleType scaleType, final String cacheKey) {
        return new ImageRequest(requestUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                //onGetImageSuccess(cacheKey, response);
            }
        }, maxWidth, maxHeight, scaleType, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // onGetImageError(cacheKey, error);
            }
        });

}
