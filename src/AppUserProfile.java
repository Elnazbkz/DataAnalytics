import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.SQLException;
import java.util.Map;
//import appException.Exceptions.FaildUpdateException;

import appException.Exceptions;
import appException.Exceptions.EmailExistsException;
import appException.Exceptions.FailedUpdateException;
import appException.Exceptions.PostIDInvalid;

public class AppUserProfile {
	private Label fullNameLabel;
	private Label accountTypeLabel;
	private Stage primaryStage;
	private User user;
	private Post post;
	private Map<String, String> userInfo;
	private Map<String, String> postInfo;
	private boolean isVIPUser = false;

	public AppUserProfile(Stage primaryStage, int userID) {
		this.primaryStage = primaryStage;
		user = new User();
		userInfo = user.GetUserInfo(userID);
		if (userInfo != null) {
			initializeLabels();
			isVIPUser = user.isVIPUser(userID);
		} else {
			System.err.println("User information is null for UserID: " + userID);
		}

		post = new Post();
	}

	private void initializeLabels() {
		fullNameLabel = new Label("Welcome back: " + userInfo.get("FirstName") + " " + userInfo.get("LastName"));
		accountTypeLabel = new Label("To edit your profile, please use the form below.");
	}

	public String getTitle() {
		return "User Profile";
	}

	public Scene getScene() {
		// Call initializeLabels to create the labels
		initializeLabels();

		TabPane tabPane = new TabPane();
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); // Disable tab closing
		if (!isVIPUser) {
			tabPane.getTabs().addAll(createEditProfileTab(), createAddPostTab(), createDeletePostTab(),
					createShowPostDetailsTab(), createGetTopPostsTab(), createPostExporterTab());
		} else {
			tabPane.getTabs().addAll(createEditProfileTab(), createAddPostTab(), createDeletePostTab(),
					createShowPostDetailsTab(), createGetTopPostsTab(), createPostExporterTab(),
					createDataVisualizationTab());
		}

		VBox vBox = new VBox(tabPane);
		Scene scene = new Scene(vBox, 800, 600);
		return scene;
	}

	//// Tab content for Edit Profile
	private Tab createEditProfileTab() {
		Tab tab = new Tab("Edit Profile");

		Label emailLabel = new Label("Email:");
		Label firstNameLabel = new Label("First Name:");
		Label lastNameLabel = new Label("Last Name:");

		int CurrentUserID = Integer.parseInt(userInfo.get("ID"));
		Label VIPLabel = new Label("Would you like to subscribe to the application for a monthly fee of $0?");
		Button changeToVIP = new Button("Change to VIP");
		Text VIPResult = new Text();

		TextField firstName = new TextField(userInfo.get("FirstName"));
		TextField lastName = new TextField(userInfo.get("LastName"));
		TextField emailTextField = new TextField(userInfo.get("EmailAddress"));
		Button UpdateProfileButton = new Button("Update Profile");
		Text Result = new Text();
		GridPane editProfileContent = createForm("User Profile");

		// Add the labels and text fields to the form
		editProfileContent.add(fullNameLabel, 0, 1);
		editProfileContent.add(accountTypeLabel, 0, 2);

		editProfileContent.add(firstNameLabel, 0, 3);
		editProfileContent.add(firstName, 1, 3);

		editProfileContent.add(lastNameLabel, 0, 4);
		editProfileContent.add(lastName, 1, 4);

		editProfileContent.add(emailLabel, 0, 5);
		editProfileContent.add(emailTextField, 1, 5);

		editProfileContent.add(Result, 0, 6);
		editProfileContent.add(UpdateProfileButton, 0, 7);

		if (!isVIPUser) {
			editProfileContent.add(VIPLabel, 0, 8);
			editProfileContent.add(changeToVIP, 1, 8);
			editProfileContent.add(VIPResult, 0, 9);

			/// button click handler
			changeToVIP.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					int UserID = Integer.parseInt(userInfo.get("ID"));
					try {
						boolean changeToVipResult = user.UpdateUserToVIP(CurrentUserID);
						if (changeToVipResult) {
							VIPResult.setText("Please log out and log in again to access VIP functionalities.");
						} else {
							VIPResult.setText("update failed. Please try again");
						}

					} catch (FailedUpdateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
		}

		tab.setContent(editProfileContent);

		/// button click handler
		UpdateProfileButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int UserID = Integer.parseInt(userInfo.get("ID"));

				if (UserID != 0) {
					String FirstName = firstName.getText();
					String LastName = lastName.getText();
					String EmailAddress = emailTextField.getText();
					String Password = userInfo.get("Password");
					String AccountType = userInfo.get("AccountType");

					User newUser = null;
					try {
						newUser = new User(UserID, FirstName, LastName, EmailAddress, Password, AccountType);
					} catch (SQLException e) {
						e.printStackTrace();
					}

					boolean updateUser = false;
					try {
						updateUser = newUser.UpdateUser(newUser);
					} catch (FailedUpdateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (updateUser) {
						Result.setText("User updated successfully");
					}
				} else {
					Result.setText("Invalid User ID!");
				}
			}
		});

		return tab;
	}
	//// End

	/// Tab content for Add new Post
	private Tab createAddPostTab() {
		Tab tab = new Tab("Add Post");

		Label PostIDLabel = new Label("Post ID:");
		Label PostContentLabel = new Label("Post Content:");
		Label AuthorLabel = new Label("Author:");
		Label LikesLabel = new Label("Likes:");
		Label SharesLabel = new Label("Shares:");
		Label DateTimeLabel = new Label("Date&Time(Format: dd/MM/yyyy HH:mm)");

		TextField PostIDField = new TextField();
		TextField PostContentField = new TextField();
		TextField AuthorField = new TextField();
		TextField LikesField = new TextField();
		TextField SharesField = new TextField();
		TextField DateField = new TextField();
		Button AddPostButton = new Button("Add Post");
		Text Result = new Text();

		GridPane AddPostContent = createForm("User Profile");

		AddPostContent.add(PostIDLabel, 0, 1);
		AddPostContent.add(PostIDField, 1, 1);

		AddPostContent.add(PostContentLabel, 0, 2);
		AddPostContent.add(PostContentField, 1, 2);

		AddPostContent.add(AuthorLabel, 0, 3);
		AddPostContent.add(AuthorField, 1, 3);

		AddPostContent.add(LikesLabel, 0, 4);
		AddPostContent.add(LikesField, 1, 4);

		AddPostContent.add(SharesLabel, 0, 5);
		AddPostContent.add(SharesField, 1, 5);

		AddPostContent.add(DateTimeLabel, 0, 6);
		AddPostContent.add(DateField, 1, 6);

		AddPostContent.add(AddPostButton, 0, 7);

		AddPostContent.add(Result, 0, 8);

		tab.setContent(AddPostContent);

		AddPostButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					int postID = Integer.parseInt(PostIDField.getText());

					if (postID == 0) {
						Result.setText("Post ID cannot be zero.");
						return;
					}

					postInfo = post.GetPostDetails(postID);
					if (post.postIDExists(postID)) {
						Result.setText("Post with ID " + postID + " already exists.");
						return;
					}

					String postContent = PostContentField.getText();
					String author = AuthorField.getText();

					int likes = 0;
					int shares = 0;

					try {
						likes = Integer.parseInt(LikesField.getText());
						shares = Integer.parseInt(SharesField.getText());
					} catch (NumberFormatException e) {
						Result.setText("Likes and Shares must be non-negative integers.");
						return;
					}

					if (likes < 0 || shares < 0) {
						Result.setText("Likes and Shares must be non-negative integers.");
						return;
					}

					String dateTime = DateField.getText();

					Post newPost = new Post(postID, postContent, author, likes, shares, dateTime);
					boolean result = post.CreateNewPost(newPost);

					if (result) {
						Result.setText("Post with ID " + postID + " successfully added.");
					} else {
						Result.setText("Failed to add post with ID " + postID);
					}

				} catch (PostIDInvalid e) {
					Result.setText("Invalid input for Post ID, Likes, or Shares.");
				}
			}
		});

		return tab;
	}

	/// End

	private Tab createDeletePostTab() {
		Tab tab = new Tab("Delete Post");

		Label PostIDLabel = new Label("Post ID:");

		TextField PostIDField = new TextField();

		Text Result = new Text();

		Button DeletePostButton = new Button("Delete Post");

		GridPane DeletePostContent = createForm("Delete Post by ID");

		DeletePostContent.add(PostIDLabel, 0, 1);
		DeletePostContent.add(PostIDField, 1, 1);
		DeletePostContent.add(DeletePostButton, 0, 2);
		DeletePostContent.add(Result, 0, 3);

		tab.setContent(DeletePostContent);

		DeletePostButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					int postID = Integer.parseInt(PostIDField.getText());
					Post post = new Post();

					if (!post.postIDExists(postID)) {
						Result.setText("Post ID is not valid or not exists.");
						return;
					}

					boolean QueryResult = post.DeletePost(postID);
					if (QueryResult) {
						Result.setText("Post deleted successfully");
					}

				} catch (NumberFormatException e) {
					Result.setText("Invalid input for Post ID.");
				}
			}
		});

		return tab;
	}

	private Tab createShowPostDetailsTab() {
		Tab tab = new Tab("Show Post Details");

		Label PostIDLabel = new Label("Post ID:");

		TextField PostIDField = new TextField();

		Text Result = new Text();

		Button GetPostButton = new Button("Get Post details");

		GridPane PostDetailsTab = createForm("Show Post Details by ID");

		PostDetailsTab.add(PostIDLabel, 0, 1);
		PostDetailsTab.add(PostIDField, 1, 1);
		PostDetailsTab.add(GetPostButton, 0, 2);
		PostDetailsTab.add(Result, 0, 3);

		tab.setContent(PostDetailsTab);

		GetPostButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String postIDText = PostIDField.getText();

				if (!postIDText.equals("")) {
					int postID = Integer.parseInt(postIDText);
					postInfo = post.GetPostDetails(postID);
					if (!postInfo.isEmpty()) {
						String postContent = postInfo.get("Content");
						String author = postInfo.get("Author");
						int likes = Integer.parseInt(postInfo.get("Likes"));
						int shares = Integer.parseInt(postInfo.get("Shares"));
						String dateTimeString = postInfo.get("DateTime");

						Result.setText("ID: " + postID + "\n" + "Post Content: " + postContent + "\n" + "Author: "
								+ author + "\n" + "Likes: " + likes + "\n" + "Shares: " + shares + "\n"
								+ "Date & Time: " + dateTimeString);
					} else {
						Result.setText("Post with ID " + postID + " does not exist.");
					}
				} else {
					Result.setText("Please enter a valid Post ID.");
				}
			}
		});

		return tab;
	}

	private Tab createGetTopPostsTab() {
		Tab tab = new Tab("Get Top Posts");

		Label GetTopPostByLabel = new Label("Select option to retrieve data");

		Label NumberofPostsLabel = new Label("Number of posts to retrieve");

		TextField NumberofPostsField = new TextField("2");

		ComboBox<String> comboBox = new ComboBox<>();
		ObservableList<String> options = FXCollections.observableArrayList("Likes", "Shares");
		comboBox.setItems(options);

		Button GetTopPostsButton = new Button("Get Posts");

		Text Result = new Text();

		GridPane GetTopPosts = createForm("Get Top Posts by likes or shares");

		GetTopPosts.add(GetTopPostByLabel, 0, 1);
		GetTopPosts.add(comboBox, 1, 1);
		GetTopPosts.add(NumberofPostsLabel, 0, 2);
		GetTopPosts.add(NumberofPostsField, 0, 3);
		GetTopPosts.add(GetTopPostsButton, 0, 4);
		GetTopPosts.add(Result, 0, 5);

		tab.setContent(GetTopPosts);

		GetTopPostsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String Type = comboBox.getValue();
				String Count = "2";
				if (NumberofPostsField != null && !NumberofPostsField.getText().isEmpty()) {
					Count = NumberofPostsField.getText();
				}
				if (!Type.equals("")) {
					postInfo = post.GetTopPosts(Type, Count);
					if (!(postInfo == null)) {
						int postID = Integer.parseInt(postInfo.get("ID"));
						String postContent = postInfo.get("Content");
						String author = postInfo.get("Author");
						int likes = Integer.parseInt(postInfo.get("Likes"));
						int shares = Integer.parseInt(postInfo.get("Shares"));
						String dateTimeString = postInfo.get("DateTime");

						Result.setText("ID: " + postID + "\n" + "Post Content: " + postContent + "\n" + "Author: "
								+ author + "\n" + "Likes: " + likes + "\n" + "Shares: " + shares + "\n"
								+ "Date & Time: " + dateTimeString);
					} else {
						Result.setText("Nothing found!");
					}
				} else {
					Result.setText("Please Select Type");
				}
			}
		});
		return tab;

	}

	private Tab createPostExporterTab() {

		Tab tab = new Tab("Export Post");

		Label PostIDLabel = new Label("Post ID");

		TextField PostIDField = new TextField("2");

		Button GetTopPostsButton = new Button("Export into csv file");

		Text Result = new Text();

		GridPane GetTopPosts = createForm("Get Top Posts by likes or shares");

		GetTopPosts.add(PostIDLabel, 0, 1);
		GetTopPosts.add(PostIDField, 1, 1);
		GetTopPosts.add(GetTopPostsButton, 0, 2);
		GetTopPosts.add(Result, 0, 3);

		tab.setContent(GetTopPosts);

		GetTopPostsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int postID = Integer.parseInt(PostIDField.getText());

				String ExportResult = post.exportToCSV(postID);
				Result.setText(ExportResult);
			}
		});
		return tab;

	}

	private Tab createDataVisualizationTab() {
		Tab tab = new Tab("Data Visualization");

		Label PostIDLabel = new Label("Post ID");

		TextField PostIDField = new TextField("2");

		Button GetTopPostsButton = new Button("Export into csv file");

		Text Result = new Text();

		GridPane GetTopPosts = createForm("Get Top Posts by likes or shares");

		GetTopPosts.add(PostIDLabel, 0, 1);
		GetTopPosts.add(PostIDField, 1, 1);
		GetTopPosts.add(GetTopPostsButton, 0, 2);
		GetTopPosts.add(Result, 0, 3);

		tab.setContent(GetTopPosts);

		GetTopPostsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int postID = Integer.parseInt(PostIDField.getText());

				String ExportResult = post.exportToCSV(postID);
				Result.setText(ExportResult);
			}
		});
		return tab;

	}

	private GridPane createForm(String contentText) {
		GridPane formGridPane = new GridPane();
		formGridPane.setAlignment(Pos.CENTER);
		formGridPane.setHgap(10);
		formGridPane.setVgap(10);

		// Add form components (labels, text fields, buttons, etc.)
		Label contentLabel = new Label(contentText);
		formGridPane.add(contentLabel, 0, 0);

		// Add other form components as needed

		return formGridPane;
	}
}
