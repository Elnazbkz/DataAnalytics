import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import appException.Exceptions;
import appException.Exceptions.EmailExistsException;
import appException.Exceptions.PostIDInvalid;
import sqlitedb.SQLiteJDBC;

public class Post {
	private static final Map<String, String> PostMap = null;
	private int PostID;
	private String PostContent;
	private String Author;
	private int Likes;
	private int Shares;
	private String dateTime;
	SQLiteJDBC dbConnection = new SQLiteJDBC();
    Connection con = dbConnection.getConnection();
	
	public Post (int PostID, String PostContent, String Author, int Likes, int Shares, String dateTime) {
		this.PostID = PostID;
		this.PostContent = PostContent;
		this.Author = Author;
		this.Likes = Likes;
		this.Shares = Shares;
		this.dateTime = dateTime;
	}
	
	public Post() {
		
	}
	
	public int getPostID() {
		return PostID;
	}
	
	public String getPostContent() {
		return PostContent;
	}
	
	public String getAuthor() {
		return Author;
	}
	public int getLikes() {
		return Likes;
	}
	public int getShares() {
		return Shares;
	}
	public String getDateTime() {
		return dateTime;
	}
	
	public boolean postIDExists(int postID) {
	    try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM posts WHERE ID = ?")) {
	        stmt.setInt(1, postID);
	        ResultSet resultSet = stmt.executeQuery();

	        // Check if the result set has any rows
	        if (resultSet.next()) {
	            return true;
	        }
	    } catch (SQLException e) {
	        System.out.println("Error while searching for the post: " + e.getMessage());
	    }

	    return false;
	}
	
	public boolean CreateNewPost(Post post) throws PostIDInvalid{
		boolean PostExsits = postIDExists(post.getPostID());
		if (PostExsits) {
            Exceptions exceptions = new Exceptions();  // Create an instance of Exceptions
            throw exceptions.new PostIDInvalid("Post ID exists, please choose a unique post ID.");
        }
		int postID = post.getPostID();
		String PostContent = post.getPostContent();
		String Author = post.getAuthor();
		int Likes = post.getLikes();
		int Shares = post.getShares();
		String dateTime = post.getDateTime();
		try {
            String query = "INSERT INTO posts (ID, Content, Author, Likes, Shares, DateTime) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, postID);
            pstmt.setString(2, PostContent);
            pstmt.setString(3, Author);
            pstmt.setInt(4, Likes);
            pstmt.setInt(5, Shares);
            pstmt.setString(5, dateTime);

            int result = pstmt.executeUpdate();

            if (result == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error while inserting post: " + e.getMessage());
        }
		return false;
	}
	
	public Map<String, String> GetPostDetails(int PostID) {
    	Map<String, String> userMap = null;
        try ( PreparedStatement stmt = con.prepareStatement("SELECT * FROM posts WHERE ID = " + PostID)) {
            ResultSet resultSet = stmt.executeQuery();
            HashMap PostMap = new HashMap<>();
            while (resultSet.next()) {
            	PostMap.put("ID", String.valueOf(resultSet.getInt("ID")));
            	PostMap.put("PostContent", String.valueOf(resultSet.getString("PostContent")));
            	PostMap.put("Author", String.valueOf(resultSet.getString("Author")));
            	PostMap.put("Likes", String.valueOf(resultSet.getString("Likes")));
            	PostMap.put("Shares", String.valueOf(resultSet.getString("Shares")));
            	PostMap.put("DateTime", String.valueOf(resultSet.getString("DateTime")));
            }

        } catch (SQLException e) {
        	System.out.println("Error while searching for the post: " + e.getMessage());
        }
        
		return PostMap;
    }
}
