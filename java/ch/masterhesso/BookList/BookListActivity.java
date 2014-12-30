package ch.masterhesso.BookList;

import java.util.ArrayList;
import java.util.Objects;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ch.masterhesso.BookList.adapters.BookDataAdapter;
import ch.masterhesso.BookList.data.BookData;
import ch.masterhesso.BookList.tasks.GoogleBooksAPIIconTask;
import ch.masterhesso.BookList.tasks.GoogleBooksWebAPITask;

/**
 * Find/display book list in order of ratings for authors/categories given in search
 * 
 * @author Cédric Droguet
 *
 */
public class BookListActivity extends Activity {

	private ArrayList<BookData> books;
	private ListView bookList;
	private LayoutInflater layoutInflator;
	private Button booksButton;
    private EditText searchParams;
    private InputMethodManager inMgr;
    private GoogleBooksAPIIconTask imgFetcher;
    private Spinner searchSpinner;
    private Spinner languageSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        this.inMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        this.bookList = (ListView) findViewById(R.id.book_list);
        this.imgFetcher = new GoogleBooksAPIIconTask(this);
        this.layoutInflator = LayoutInflater.from(this);
        this.booksButton = (Button)this.findViewById(R.id.book_button);
        this.searchParams = (EditText)this.findViewById(R.id.search_query);
        this.searchSpinner = (Spinner)this.findViewById(R.id.search_pattern);
        this.languageSpinner = (Spinner)this.findViewById(R.id.search_language);
        
        this.booksButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				inMgr.hideSoftInputFromWindow(booksButton.getWindowToken(), 0);
		        GoogleBooksWebAPITask bookTask = new GoogleBooksWebAPITask(BookListActivity.this);
		        try {
                    String searchCriteria = "";
                    String searchLanguage = "";
                    switch(searchSpinner.getSelectedItemPosition())
                    {
                        case 1:
                            searchCriteria = "inauthor:";
                            break;
                        case 2:
                            searchCriteria = "intitle:";
                            break;
                        default:
                            searchCriteria = "";
                    }

                    switch(languageSpinner.getSelectedItemPosition())
                    {
                        case 1:
                            searchLanguage = "en";
                            break;
                        case 2:
                            searchLanguage = "fr";
                            break;
                        case 3:
                            searchLanguage = "de";
                            break;
                        default:
                            searchLanguage = "";
                    }

                    bookTask.execute(searchParams.getText().toString(), searchCriteria, searchLanguage);
		        }
		        catch (Exception e)
		        {
		            bookTask.cancel(true);
		            alert (getResources().getString(R.string.no_books));
		        }
		        
				
			}
		});  
        
        // Restore any already fetched data on orientation change.
        final Object[] data = (Object[]) getLastNonConfigurationInstance();
        if(data != null) {
        	this.books = (ArrayList<BookData>) data[0];
        	this.imgFetcher = (GoogleBooksAPIIconTask)data[1];
         	bookList.setAdapter(new BookDataAdapter(this, this.imgFetcher, this.layoutInflator, this.books));
        }
    }

    /**
     * Handy dandy alerter.
     * @param msg the message to toast.
     */
    public void alert (String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    /**
     * Save any fetched book data for orientation changes.
     */
/*    @Override
    public Object onRetainNonConfigurationInstance() {
    	Object[] myStuff = new Object[2];
    	myStuff[0] = this.books;
    	myStuff[1] = this.imgFetcher;
    	return myStuff;
    }
*/
    
    /**
     * Bundle to hold refs to row items views.
     *
     */
    public static class MyViewHolder {
        public TextView bookTitle, authorName, bookDescription;
        public RatingBar ratingBar;
        public ImageView icon;
        public BookData book;
    }

	public void setBooks(ArrayList<BookData> books) {
		this.books = books;
		this.bookList.setAdapter(new BookDataAdapter(this, this.imgFetcher, this.layoutInflator, this.books));
	}

    
}