package ch.masterhesso.BookList;

import android.app.Activity;
import android.os.Bundle;

import ch.masterhesso.BookList.R;

/**
 * Created by Cédric Droguet on 01.01.2015.
 */
public class BookListActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}