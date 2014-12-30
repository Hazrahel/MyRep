package ch.masterhesso.BookList.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Image cache and async fetcher task for Last.FM images.
 * 
 * @author Cédric Droguet
 *
 * largely inspired from resources found on the internet
 */
public class GoogleBooksAPIIconTask {
	
	private static final String debugTag = "ImageWorkerTask";

    private HashMap<String, Drawable> imageCache;
    private static Drawable DEFAULT_ICON = null;
    private BaseAdapter adapt;
    
    
    public GoogleBooksAPIIconTask(Context context)
    {
        imageCache = new HashMap<String, Drawable>();
    }
    
    public Drawable loadImage (BaseAdapter adapter, ImageView view)
    {
        this.adapt = adapter;
        String url = (String) view.getTag();
        if (imageCache.containsKey(url))
        {
            return imageCache.get(url);
        }
        else {
            new ImageTask().execute(url);
            return DEFAULT_ICON;
        }
    }
    
    private class ImageTask extends AsyncTask<String, Void, Drawable>
    {
        private String s_url;

        @Override
        protected Drawable doInBackground(String... params) {
            s_url = params[0];
            Log.d(debugTag, s_url);
            if(s_url != "") {
                InputStream stream;
                try {
                    Log.d(debugTag, "Fetching: " + s_url);
                    URL url = new URL(s_url);
                    stream = url.openStream();
                } catch (MalformedURLException e) {
                    Log.d(debugTag, "MalformedURL: " + e.getMessage());
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    Log.d(debugTag, "I/O error : " + e.getMessage());
                    throw new RuntimeException(e);
                }
                return Drawable.createFromStream(stream, "src");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Drawable result) {
            super.onPostExecute(result);
            synchronized (this) {
                imageCache.put(s_url, result);
            }
            adapt.notifyDataSetChanged();
        }
        
    }
}
