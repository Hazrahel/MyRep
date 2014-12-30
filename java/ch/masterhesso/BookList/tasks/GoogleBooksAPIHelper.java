package ch.masterhesso.BookList.tasks;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import ch.masterhesso.BookList.R;

/**
 * Helper to interact with Google Books' public API.
 * 
 * @author Cédric Droguet
 *
 */
public class GoogleBooksAPIHelper {

    private static final String googleBookAPIQueryUrlBase =
        "https://www.googleapis.com/books/v1/volumes?q=";
    private static final String googleBookAPIQueryUrlLangRestrict = "&langRestrict=";
    private static final String googleBookAPIQueryUrlIemsSelected = "&libraryRestrict=no-restrict&"+
                "maxResults=40&orderBy=relevance&printType=books&fields=items(volumeInfo(description%2Ccategories%2C"+
                "language%2CpublishedDate%2CimageLinks(medium%2Csmall)%2Ctitle%2CindustryIdentifiers%2Cauthors%2CcanonicalVolumeLink%2C"+
                "ratingsCount%2Cpublisher%2CaverageRating))";
    private static final String googleBookAPIQueryUrlKey = "&key=AIzaSyDkiaqg-zjl8I05s41-Mhj1VvFsBxJlQn4";

    private static final int HTTP_STATUS_OK = 200;
	private static byte[] buff = new byte[1024];
	private static final String logTag = "GoogleBooksAPIHelper";

	public static class ApiException extends Exception {
		private static final long serialVersionUID = 1L;

		public ApiException (String msg)
		{
			super (msg);
		}

		public ApiException (String msg, Throwable thr)
		{
			super (msg, thr);
		}
	}

	/**
	 * download most popular tracks in given metro.
	 * @param params search strings
	 * @return Array of json strings returned by the API. 
	 * @throws ApiException
	 */
	protected static synchronized String downloadFromServer (String... params)
	throws ApiException
	{
		String retval = null;

        String searchParams;
        String searchLanguage = googleBookAPIQueryUrlLangRestrict;

        try
        {
            searchParams = URLEncoder.encode(params[0], "UTF-8"); //= "Tolkien";
        }catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
            throw new ApiException(R.string.encoding_error + e.getMessage());
        }

        // type of search
        if(params[1] != "" && params[1] != null)
        {
            searchParams = params[1] + searchParams;
        }


        // search language
        if(params[2] != "" && params[2] != null)
        {
            searchLanguage += params[2];
        }else
        {
            searchLanguage += Locale.getDefault().getLanguage();
        }

        Log.d(logTag, searchLanguage);

		String url = googleBookAPIQueryUrlBase + searchParams + searchLanguage + googleBookAPIQueryUrlIemsSelected+googleBookAPIQueryUrlKey ;

        //String url = "https://www.googleapis.com/books/v1/volumes?maxResults=40&orderBy=relevance&printType=all&fields=items(etag%2Cid%2Ckind%2CrecommendedInfo%2CselfLink%2CvolumeInfo(description%2Ccategories%2Clanguage%2CpublishedDate%2CimageLinks%2Ctitle%2CindustryIdentifiers%2Csubtitle%2Cauthors%2CcanonicalVolumeLink%2CinfoLink%2CratingsCount%2Cpublisher%2CmainCategory%2CaverageRating))&key=AIzaSyDkiaqg-zjl8I05s41-Mhj1VvFsBxJlQn4&q=inauthor%3ATolkien&langRestrict=fr";
        //String url = "https://www.googleapis.com/books/v1/volumes?maxResults=40&orderBy=relevance&printType=all&fields=items(etag%2Cid%2Ckind%2CrecommendedInfo%2CselfLink%2CvolumeInfo(description%2Ccategories%2Clanguage%2CpublishedDate%2CimageLinks%2Ctitle%2CindustryIdentifiers%2Csubtitle%2Cauthors%2CcanonicalVolumeLink%2CinfoLink%2CratingsCount%2Cpublisher%2CmainCategory%2CaverageRating))&key=AIzaSyDkiaqg-zjl8I05s41-Mhj1VvFsBxJlQn4&q=id%3Az2aQ7Tkfb5wC&langRestrict=fr";

		Log.d(logTag,"Fetching " + url);
		
		// create an http client and a request object.
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);

		try {

			// execute the request
			HttpResponse response = client.execute(request);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HTTP_STATUS_OK) {
				// handle error here
				throw new ApiException("Invalid response from Google Books API" +
						status.toString());
			}

			// process the content. 
			HttpEntity entity = response.getEntity();
			InputStream ist = entity.getContent();
			ByteArrayOutputStream content = new ByteArrayOutputStream();

			int readCount = 0;
			while ((readCount = ist.read(buff)) != -1) {
				content.write(buff, 0, readCount);
			}
			retval = new String (content.toByteArray());

		} catch (Exception e) {
			throw new ApiException("Problem connecting to the server " + 
					e.getMessage(), e);
		}

		return retval;
	}
}
