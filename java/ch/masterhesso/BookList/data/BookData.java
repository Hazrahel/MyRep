package ch.masterhesso.BookList.data;

/**
 * A simple wrapper for our books (filled with JSON Data)
 */
public class BookData {
	
	private String title = "";
	private String author = "";
    private double averageRating = 0d;
    private String description = "";
	private String imageUrl = "";
	private String volumeUrl = "";

	public BookData(String title, String authors, String imageUrl, String volumeUrl, double averageRating, String description) {
		super();
		this.title = title;
		this.author = authors;
		this.imageUrl = imageUrl;
		this.volumeUrl = volumeUrl;
        this.averageRating = averageRating;
        this.description = description;
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
}
