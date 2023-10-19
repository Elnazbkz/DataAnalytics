import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

import appException.Exceptions;
import appException.Exceptions.EmailExistsException;
import appException.Exceptions.PostIDInvalid;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sqlitedb.SQLiteJDBC;

public class Post {
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
	    try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM posts WHERE ID = " + postID)) {
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
	
	public boolean DeletePost(int postID) {
	    try (PreparedStatement stmt = con.prepareStatement("DELETE FROM posts WHERE ID = ?")) {
	        stmt.setInt(1, postID);
	        int affectedRows = stmt.executeUpdate();

	        return affectedRows > 0;

	    } catch (SQLException e) {
	        System.out.println("Error while deleting the post: " + e.getMessage());
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
		Map<String, String> PostMap = null;
    	try ( PreparedStatement stmt = con.prepareStatement("SELECT * FROM posts WHERE ID = " + PostID)) {
            ResultSet resultSet = stmt.executeQuery();
            PostMap = new HashMap<>();
            while (resultSet.next()) {
            	PostMap.put("ID", String.valueOf(resultSet.getInt("ID")));
            	PostMap.put("Content", String.valueOf(resultSet.getString("Content")));
            	PostMap.put("Author", String.valueOf(resultSet.getString("Author")));
            	PostMap.put("Likes", String.valueOf(resultSet.getInt("Likes")));
            	PostMap.put("Shares", String.valueOf(resultSet.getInt("Shares")));
            	PostMap.put("DateTime", String.valueOf(resultSet.getString("DateTime")));
            }

        } catch (SQLException e) {
        	System.out.println("Error while searching for the post: " + e.getMessage());
        }
        
		return PostMap;
    }
	
	
	public Map<String, String> GetTopPosts(String Type , String Count) {
		Map<String, String> PostMap = null;
    	try ( PreparedStatement stmt = con.prepareStatement("SELECT * FROM posts ORDER BY " + Type + " LIMIT " + Count)) {
            ResultSet resultSet = stmt.executeQuery();
            PostMap = new HashMap<>();
            while (resultSet.next()) {
            	PostMap.put("ID", String.valueOf(resultSet.getInt("ID")));
            	PostMap.put("Content", String.valueOf(resultSet.getString("Content")));
            	PostMap.put("Author", String.valueOf(resultSet.getString("Author")));
            	PostMap.put("Likes", String.valueOf(resultSet.getInt("Likes")));
            	PostMap.put("Shares", String.valueOf(resultSet.getInt("Shares")));
            	PostMap.put("DateTime", String.valueOf(resultSet.getString("DateTime")));
            }

        } catch (SQLException e) {
        	System.out.println("Error while searching for the post: " + e.getMessage());
        }
        
		return PostMap;
    }
	
	
	public String exportToCSV(int postID) {
        // Assume postContent contains the data to be exported
		
        // Create a FileChooser to let the user choose the file location and name
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Post to CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        // Show save dialog
        Stage stage = new Stage();
        Writer writer = null;
        String result = null;

        try {
            // Show save file dialog
            java.io.File file = fileChooser.showSaveDialog(stage);
            
            if (file == null) {
            	result = "Export cancelled";
            }
            
            else {
                writer = new FileWriter(file);
                writer.write("PostID,Content,Author,Likes,Shares,DateTime\n"); // CSV header
                try ( PreparedStatement stmt = con.prepareStatement("SELECT * FROM posts WHERE ID =  " + postID)) {
                    ResultSet resultSet = stmt.executeQuery();
                    while (resultSet.next()) {
                        writer.write(resultSet.getInt("ID") + ",");
                        writer.write(resultSet.getString("Content") + ",");
                        writer.write(resultSet.getString("Author") + ",");
                        writer.write(resultSet.getInt("Likes") + ",");
                        writer.write(resultSet.getInt("Shares") + ",");
                        writer.write(resultSet.getString("DateTime") + "\n");
                    }

                } catch (SQLException e) {
                	System.out.println("Error while searching for the post: " + e.getMessage());
                }
                // Flush and close the writer
                writer.flush();
                writer.close();
                result = "Post exported successfully";
            } 

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		return result;
    }
	
	
	public boolean SocialMediaPostImporter() throws NumberFormatException, FileNotFoundException, IOException {
		String csvFile = "posts.csv"; // Specify your CSV file path
	    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) { // try catch to get file data and shows message on error
            String line; // define line as string
            br.readLine(); // Skip header row
            while ((line = br.readLine()) != null) { // reading rest of lines
                String[] values = line.split(","); // split values of each line by , and put into array
                int id = Integer.parseInt(values[0].trim()); // get id of post
                String content = values[1].trim(); // get content of post
                String author = values[2].trim(); // get author of post
                int likes = Integer.parseInt(values[3].trim()); // get likes of post
                int shares = Integer.parseInt(values[4].trim()); // get shares of post
                String dateTime = values[5].trim(); // check format of the retrieved datetime
                Post post = new Post(id, content, author, likes, shares, dateTime); // create an instance of post
                post.CreateNewPost(post);
                return true;
            }
        } catch (PostIDInvalid e) {
			e.printStackTrace();
		}
        return false;
	}
	
	
}
