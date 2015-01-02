package ch.masterhesso.BookList.tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.masterhesso.BookList.BookListFragment;
import ch.masterhesso.BookList.R;

import ch.masterhesso.BookList.data.BookData;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * AsyncTask for fetching books from Google Books JSON response handling
 *
 * @author Cedric Droguet
 */
public class GoogleBooksWebAPITask extends AsyncTask<String, Integer, String> {
    private ProgressDialog progDialog;
    private Context context;
    private BookListFragment fragment;
    private static final String debugTag = "GoogleBooksWebAPITask";

    /**
     * Construct a task
     *
     * @param fragment
     */
    public GoogleBooksWebAPITask(BookListFragment fragment) {
        super();
        this.fragment = fragment;
        this.context = this.fragment.getActivity().getApplicationContext();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progDialog = ProgressDialog.show(this.fragment.getActivity(), "Search", this.context.getResources().getString(R.string.looking_for_books), true, false);
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Log.d(debugTag, "Background:" + Thread.currentThread().getName());
            String result = GoogleBooksAPIHelper.downloadFromServer(params);
            return result;
        } catch (Exception e) {
            return new String();
        }
    }

    @Override
    protected void onPostExecute(String result) {

        ArrayList<BookData> bookDatas = new ArrayList<BookData>();

        progDialog.dismiss();

//        Log.d(debugTag, result);

        if (result.length() == 0) {
            this.fragment.alert("Unable to find books data. Try again later.");
            return;
        }

        try {
            JSONObject respObj = new JSONObject(result);

            JSONArray books = respObj.getJSONArray("items");
            for (int i = 0; i < books.length(); i++) {
                JSONObject volume = books.getJSONObject(i);
                JSONObject volumeInfo = volume.getJSONObject("volumeInfo");

                String title = volumeInfo.getString("title");
                String author = "";
                try {
                    JSONArray authors = volumeInfo.getJSONArray("authors");

                    for (int j = 0; j < authors.length(); j++) {
                        author += authors.getString(j) + ", ";
                    }
                } catch (JSONException e) {
    //                    Log.d(debugTag, "No author");
                        author = "";
                }
                if(author.length() > 2)
                {
                    author = author.substring(0, author.length()-2);
                }

                String canonicalVolumeLink;
                try
                {
                    canonicalVolumeLink = volumeInfo.getString("canonicalVolumeLink");
                }catch (JSONException e)
                {
  //                  Log.d(debugTag, "No canonical volume link");
                    canonicalVolumeLink = "";
                }

                double averageRating = 0;
                try
                {
                    averageRating = volumeInfo.getDouble("averageRating");
                }catch (JSONException e)
                {
//                    Log.d(debugTag, "No average rating");
                    averageRating = 0;
                }

                String description;
                try
                {
                    description = volumeInfo.getString("description");
                }catch (JSONException e)
                {
//                    Log.d(debugTag, "No description");
                    description = "No description available";
                }

                String imageURI = "";
                try {
                    JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                    imageURI = imageLinks.getString("thumbnail");
                }catch(JSONException e)
                {
//                    Log.d(debugTag, "No image URI");
                    imageURI = "";
                }

//                Log.d(debugTag, title + ", " + author  + ", " + imageURI + ", " + canonicalVolumeLink + ", " + averageRating + ", " + description);
                bookDatas.add(new BookData(title,author, imageURI, canonicalVolumeLink, averageRating, description));
            }
        } catch (JSONException e) {
            this.fragment.alert("Unable to find books data. Try again later.");
            e.printStackTrace();
        }

        Collections.sort(bookDatas, new Comparator<BookData>() {
            @Override
            public int compare(BookData bookData, BookData bookData2) {
                return (int) (bookData2.getAverageRating()*10d) - (int) (bookData.getAverageRating()*10d);
            }
        });
        this.fragment.setBooks(bookDatas);
    }
}
