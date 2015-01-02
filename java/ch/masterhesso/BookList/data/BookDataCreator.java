package ch.masterhesso.BookList.data;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.Parcelable;

/**
 * Created by Hazrahel on 02.01.2015.
 */
public class BookDataCreator implements Parcelable.Creator {
    @Override
    public BookData createFromParcel(Parcel source) {
        return new BookData(source);
    }

    @Override
    public Object[] newArray(int size) {
        return new BookData[size];
    }
}
