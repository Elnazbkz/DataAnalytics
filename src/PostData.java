import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PostData {
    private final StringProperty id;
    private final StringProperty postContent;
    private final StringProperty author;
    private final StringProperty likes;
    private final StringProperty shares;
    private final StringProperty dateTime;

    public PostData(String id, String postContent, String author, String likes, String shares, String dateTime) {
        this.id = new SimpleStringProperty(id);
        this.postContent = new SimpleStringProperty(postContent);
        this.author = new SimpleStringProperty(author);
        this.likes = new SimpleStringProperty(likes);
        this.shares = new SimpleStringProperty(shares);
        this.dateTime = new SimpleStringProperty(dateTime);
    }

    // Getter methods for each property
    public String getId() {
        return id.get();
    }

    public String getPostContent() {
        return postContent.get();
    }

    public String getAuthor() {
        return author.get();
    }

    public String getLikes() {
        return likes.get();
    }

    public String getShares() {
        return shares.get();
    }

    public String getDateTime() {
        return dateTime.get();
    }
}