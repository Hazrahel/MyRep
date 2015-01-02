package ch.masterhesso.BookList.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A simple wrapper for our books (filled with JSON Data)
 */
public class BookData implements Parcelable{
	
	private String title = "";
	private String author = "";
    private String description = "";
	private String imageUrl = "";
	private String volumeUrl = "";
    private double averageRating = 0d;

	public BookData(String title, String authors, String imageUrl, String volumeUrl, double averageRating, String description) {
		super();
		this.title = title;
		this.author = authors;
        this.description = description;
		this.imageUrl = imageUrl;
		this.volumeUrl = volumeUrl;
        this.averageRating = averageRating;
    }

    /**
     * this would only be used by BookDataCreator for serialisation
     *
     * @param parcel
     */
    public BookData(Parcel parcel)
    {
        super();

        this.title = parcel.readString();
        this.author = parcel.readString();
        this.description = parcel.readString();
        this.imageUrl = parcel.readString();
        this.volumeUrl = parcel.readString();
        this.averageRating = parcel.readDouble();
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getVolumeUrl() {
		return volumeUrl;
	}

	public void setVolumeUrl(String volumeUrl) {
		this.volumeUrl = volumeUrl;
	}

    public double getAverageRating()
    {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Required by Parcelable
     *
     * @return
     */
    @Override
    public int describeContents() {
        return this.hashCode();
    }

    /** Serialisation !
     *
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(description);
        dest.writeString(imageUrl);
        dest.writeString(volumeUrl);
        dest.writeDouble(averageRating);
    }
}
