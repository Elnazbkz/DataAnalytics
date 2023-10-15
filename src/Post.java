import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

import appException.Exceptions.EmailExistsException;
import appException.Exceptions.FailedUpdateException.PostException;
import sqlitedb.SQLiteJDBC;

public class Post {
	private static final Map<String, String> PostMap = null;
	private int PostID;
	private String PostContent;
	private String Author;
	private int Likes;
	private int Shares;
	private LocalDateTime dateTime;
	SQLiteJDBC dbConnection = new SQLiteJDBC();
    Connection con = dbConnection.getConnection();
	
	public Post (int PostID, String PostContent, String Author, int Likes, int Shares, LocalDateTime dateTime) {
		this.PostID = PostID;
		this.PostContent = PostContent;
		this.Author = Author;
		this.Likes = Likes;
		this.Shares = Shares;
		this.dateTime = dateTime;
	}
	
	public Post() {
		
	}
	
//	public int GetPostByID(int PostID) throws PostException{
//		try ( PreparedStatement stmt = con.prepareStatement("SELECT * FROM posts WHERE ID = ?")) {
//
//            stmt.setInt(1, PostID);
//            ResultSet resultSet = stmt.executeQuery();
//
//            // Check if the result set has any rows
//            return resultSet.getInt("ID");
//
//        } catch (SQLException e) {
//            System.out.println("Error while searching for the user: " + e.getMessage());
//            return 0;
//        }
//	}
	
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
