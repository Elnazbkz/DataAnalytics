import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import appException.Exceptions;
import appException.Exceptions.PostIDExists;
import appException.Exceptions.PostIDInvalid;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
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

	public Post(int PostID, String PostContent, String Author, int Likes, int Shares, String DateTime) {
		this.PostID = PostID;
		this.PostContent = PostContent;
		this.Author = Author;
		this.Likes = Likes;
		this.Shares = Shares;
		this.dateTime = DateTime;
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

	/// method to check whether user id exsits
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

	// method to delete post by post id
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

	// method to create a new post
	public boolean CreateNewPost(Post post) throws PostIDInvalid, PostIDExists {
		boolean PostExsits = postIDExists(post.getPostID());
		if (PostExsits) { // check if post id exists
			throw new Exceptions().new PostIDExists();
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
			pstmt.setString(6, dateTime);

			int result = pstmt.executeUpdate();

			if (result == 1) { // return true if insert was successfull
				return true;
			}
		} catch (SQLException e) {
			System.out.println("Error while inserting post: " + e.getMessage());
		}
		return false;
	}

	/// method to get post details by id
	public Map<String, String> GetPostDetails(int PostID) {
		Map<String, String> PostMap = null;
		try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM posts WHERE ID = " + PostID)) {
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

	// method to get top N posts by Likes/Shares
	public List<Map<String, String>> GetTopPosts(String Type, String Count) {
		List<Map<String, String>> postList = new ArrayList<>();
		int count = Integer.parseInt(Count);

		try (PreparedStatement stmt = con
				.prepareStatement("SELECT * FROM posts ORDER BY " + Type + " DESC LIMIT " + count)) {

			ResultSet resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				Map<String, String> postMap = new HashMap<>();
				postMap.put("ID", String.valueOf(resultSet.getInt("ID")));
				postMap.put("Content", resultSet.getString("Content"));
				postMap.put("Author", resultSet.getString("Author"));
				postMap.put("Likes", String.valueOf(resultSet.getInt("Likes")));
				postMap.put("Shares", String.valueOf(resultSet.getInt("Shares")));
				postMap.put("DateTime", resultSet.getString("DateTime"));

				postList.add(postMap);
			}
		} catch (SQLException e) {
			System.out.println("Error while searching for the posts: " + e.getMessage());
		}

		return postList;
	}

	// method to get posts by shares in range
	public int GetSharesRange(int Lower, int Upper) {
		int Count = 0;
		String query = "SELECT COUNT(*) FROM posts WHERE Shares >= " + Lower + " AND Shares <= " + Upper;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				Count = resultSet.getInt(1);
			}

		} catch (SQLException e) {
			System.out.println("Error while searching for the post: " + e.getMessage());
		}

		return Count;
	}

	// method to export post data into csv file
	public boolean exportToCSV(int postID) {

		// Create a FileChooser to let the user choose the file location and name
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export Post to CSV");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

		// Show save dialog
		Stage stage = new Stage();
		Writer writer = null;
		boolean result = false;

		try {
			// Show save file dialog
			java.io.File file = fileChooser.showSaveDialog(stage);

			if (file == null) {
				result = false;
			}

			else {
				writer = new FileWriter(file);
				writer.write("PostID,Content,Author,Likes,Shares,DateTime\n"); // CSV header
				try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM posts WHERE ID = ? ")) {
					stmt.setInt(1, postID);
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
				result = true;
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

	// method to import posts from csv file
	public boolean SocialMediaPostImporter() throws IOException, PostIDInvalid, PostIDExists {
		String csvFile = "posts.csv"; // Specify CSV file path
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) { 
			String line; // define line as string
			br.readLine(); // Skip header row
			while ((line = br.readLine()) != null) { // reading rest of lines
				String[] values = line.split(","); // split values of each line by , and put into array
				int id = Integer.parseInt(values[0].trim()); 
				String content = values[1].trim(); 
				String author = values[2].trim(); 
				int likes = Integer.parseInt(values[3].trim()); 
				int shares = Integer.parseInt(values[4].trim()); 
				String dateTime = values[5].trim(); 
				Post post = new Post(id, content, author, likes, shares, dateTime); // create an instance of post
				post.CreateNewPost(post); // create new post in database
			}
			return true;
		} catch (IOException e) {
			System.out.println("Error while searching for the post: " + e.getMessage());
		}
		return false;
	}

	// method to generate piechart
	public PieChart DataVisualization() {
		Post posts = new Post();
		// Count the posts in different share ranges (you should implement this logic)
		int count0To99 = posts.GetSharesRange(0, 99);
		int count100To999 = posts.GetSharesRange(100, 999);
		int count1000Plus = posts.GetSharesRange(1000, Integer.MAX_VALUE);
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
				new PieChart.Data("0-99 Shares", count0To99), new PieChart.Data("100-999 Shares", count100To999),
				new PieChart.Data("1000+ Shares", count1000Plus));
		final PieChart chart = new PieChart(pieChartData);
		return chart;
	}
}
