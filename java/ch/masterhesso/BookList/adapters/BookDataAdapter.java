package ch.masterhesso.BookList.adapters;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import ch.masterhesso.BookList.BookListFragment;
import ch.masterhesso.BookList.R;


import ch.masterhesso.BookList.BookListFragment.MyViewHolder;
import ch.masterhesso.BookList.data.BookData;
import ch.masterhesso.BookList.tasks.GoogleBooksAPIIconTask;

/**
 * A custom adapter to fill the Book list.
 * 
 * @author Cédric Droguet
 *
 */
public class BookDataAdapter extends BaseAdapter implements OnClickListener {
	
	private static final String debugTag = "BookDataAdapter";
	private BookListFragment activity;
	private GoogleBooksAPIIconTask imgFetcher;
	private LayoutInflater layoutInflater;
	private ArrayList<BookData> books;
	
	
    public BookDataAdapter(BookListFragment a, GoogleBooksAPIIconTask i, LayoutInflater l, ArrayList<BookData> data)
    {
    	this.activity = a;
    	this.imgFetcher = i;
    	this.layoutInflater = l;
    	this.books = data;
    }
    
    @Override
    public int getCount() {
        return this.books.size();
    }

    @Override
    public boolean areAllItemsEnabled () 
    {
    	return true;
    }
    
    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate (R.layout.bookrow, parent, false);
            holder = new MyViewHolder();
            holder.bookTitle = (TextView) convertView.findViewById(R.id.book_title);
            holder.authorName = (TextView) convertView.findViewById(R.id.author_name);
            holder.bookDescription = (TextView) convertView.findViewById(R.id.book_description);
            holder.icon = (ImageView) convertView.findViewById(R.id.book_icon);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.averRat);
            convertView.setTag(holder);
        }
        else {
            holder = (MyViewHolder) convertView.getTag();
        }
        
   		convertView.setOnClickListener(this);
   		
   		BookData book = books.get(pos);
   		holder.book = book;
   		holder.bookTitle.setText(book.getTitle());
   		holder.authorName.setText(book.getAuthor());
        holder.bookDescription.setText(book.getDescription());
        holder.ratingBar.setRating((float)book.getAverageRating());
   		if(book.getImageUrl() != "") {
   			holder.icon.setTag(book.getImageUrl());
   			Drawable dr = imgFetcher.loadImage(this, holder.icon);
   			if(dr != null) {
   				holder.icon.setImageDrawable(dr);
   			}
   		} else {
   			holder.icon.setImageResource(R.drawable.filler_icon);
   		}
   		
        return convertView;
    }
    
    @Override
	public void onClick(View v) {
		MyViewHolder holder = (MyViewHolder) v.getTag();
		if (v instanceof Button) {
			
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse(holder.book.getVolumeUrl()));
				this.activity.startActivity(intent);

		} else if (v instanceof View) {
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse(holder.book.getVolumeUrl()));
			this.activity.startActivity(intent);
		}
   		Log.d(debugTag,"OnClick pressed.");

	}
    
}