import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
//import appException.Exceptions.FaildUpdateException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import appException.Exceptions.FailedUpdateException;
import appException.Exceptions.PostIDExists;
import appException.Exceptions.PostIDInvalid;

public class AppUserProfile {
	private Label fullNameLabel;
	private Label accountTypeLabel;
	private Stage primaryStage;
	private User user;
	private Post post;
	private Map<String, String> userInfo;
	private Map<String, String> postInfo;
	private String UserVIPStatus;

	public AppUserProfile(Stage primaryStage, int userID) {
		this.primaryStage = primaryStage;
		user = new User();
		userInfo = user.GetUserInfo(userID);

		if (userInfo != null) {

			initializeLabels();
		} else {
			System.err.println("User information is null for UserID: " + userID);
		}

		post = new Post();
	}

	// Helper method to display an error alert
	private void showAlert(Alert alert, String Title, String headerText, String contentText) {
		alert.setAlertType(AlertType.INFORMATION);
		if (Title == "Error") {
			alert.setAlertType(AlertType.ERROR);
		}
		alert.setTitle(Title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
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
		int userID = Integer.parseInt(userInfo.get("ID"));
		UserVIPStatus = user.UserVIPStatus(userID);
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); // Disable tab closing
		if (UserVIPStatus.equals("Basic")) {
			tabPane.getTabs().addAll(createEditProfileTab(), createAddPostTab(), createDeletePostTab(),
					createShowPostDetailsTab(), createGetTopPostsTab(), createPostExporterTab(), createPostImportTab(),
					createDataVisualizationTab());
		} else {
			tabPane.getTabs().addAll(createEditProfileTab(), createAddPostTab(), createDeletePostTab(),
					createShowPostDetailsTab(), createGetTopPostsTab(), createPostExporterTab(), createPostImportTab(),
					createDataVisualizationTab());
		}
		VBox vBox = new VBox(tabPane);
		Scene scene = new Scene(vBox, 800, 600);
		return scene;
	}

	//// Tab content for Edit Profile
	private Tab createEditProfileTab() {
		Tab tab = new Tab("Edit Profile");

		Label emailLabel = new Label("Email Address:");
		Label firstNameLabel = new Label("First Name:");
		Label lastNameLabel = new Label("Last Name:");

		int userID = Integer.parseInt(userInfo.get("ID"));
		TextField firstName = new TextField(userInfo.get("FirstName"));
		TextField lastName = new TextField(userInfo.get("LastName"));
		TextField emailTextField = new TextField(userInfo.get("EmailAddress"));
		Button UpdateProfileButton = new Button("Update Profile");
		Button logOut = new Button("Logout");
		GridPane editProfileContent = createForm("User Profile");

		// Add the labels and text fields to the form
		editProfileContent.add(fullNameLabel, 0, 1);
		editProfileContent.add(logOut, 1, 1);
		editProfileContent.add(accountTypeLabel, 0, 2);

		editProfileContent.add(firstNameLabel, 0, 3);
		editProfileContent.add(firstName, 1, 3);

		editProfileContent.add(lastNameLabel, 0, 4);
		editProfileContent.add(lastName, 1, 4);

		editProfileContent.add(emailLabel, 0, 5);
		editProfileContent.add(emailTextField, 1, 5);
 
		editProfileContent.add(UpdateProfileButton, 0, 7);
		UserVIPStatus = user.UserVIPStatus(userID);
		if (UserVIPStatus.equals("Basic")) {
			Label VIP = new Label("Would you like to subscribe to the application for a monthly fee of $0?");
			Button changeToVIP = new Button("Change to VIP");
			editProfileContent.add(VIP, 0, 8);
			editProfileContent.add(changeToVIP, 1, 8);

			/// button click handler
			changeToVIP.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					int UserID = Integer.parseInt(userInfo.get("ID"));
					Alert alert = new Alert(AlertType.INFORMATION);
					if (UserID != 0) {
						try {
							boolean VIPUpdate = user.SetUserVIP(UserID);

							if (VIPUpdate) {
								showAlert(alert, "Success", "Your account successfully updated to VIP",
										"Please log out and log in again to access VIP functionalities.");

							} else {
								showAlert(alert, "Error", "Failed to update to VIP",
										"Something went wrong, please try again");
							}
							alert.showAndWait();
						} catch (FailedUpdateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
		}

		tab.setContent(editProfileContent);

		logOut.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				AppFirstPage FirstPage = new AppFirstPage(primaryStage);
				primaryStage.setTitle(FirstPage.getTitle()); // Set the stage title
				primaryStage.setScene(FirstPage.getScene()); // Place the scene in the stage
				primaryStage.setHeight(600);
				primaryStage.setWidth(800);
				primaryStage.show(); // Display the stage
			}
			
			});
		
		/// button click handler
		UpdateProfileButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				int UserID = Integer.parseInt(userInfo.get("ID"));
				Alert alert = new Alert(AlertType.INFORMATION);
				if (UserID != 0) {
					String FirstName = firstName.getText();
					String LastName = lastName.getText();
					String EmailAddress = emailTextField.getText();
					String Password = userInfo.get("Password");
					String AccountType = userInfo.get("AccountType");
					// Regular expression for a simple email format validation
					String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
					Pattern pattern = Pattern.compile(emailRegex);
					Matcher matcher = pattern.matcher(EmailAddress);

					if (!matcher.matches()) {
						alert.setTitle("Login Failed");
						alert.setHeaderText("Invalid Email Format");
						alert.setContentText("Please enter a valid email address.");
						showAlert(alert, "Error", "Invalid Email Format", "Please enter a valid email address.");
						alert.showAndWait();
						return; // Exit the method if the email format is invalid
					}

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
						showAlert(alert, "Success", "Your account details successfully",
								"Please log out and log in again to see your updated details");
					}
				} else {
					showAlert(alert, "Error", "Failed to Your account details",
							"Something went wrong, please try again");

				}
				alert.showAndWait();
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
		Label DateTimeLabel = new Label("Date & Time (Format: dd/MM/yyyy HH:mm)");

		TextField PostIDField = new TextField();
		TextField PostContentField = new TextField();
		TextField AuthorField = new TextField();
		TextField LikesField = new TextField("0");
		TextField SharesField = new TextField("0");
		TextField DateField = new TextField();
		Button AddPostButton = new Button("Add Post");
		Text Result = new Text();

		GridPane AddPostContent = createForm("Add Post");

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
				Alert alert = new Alert(AlertType.INFORMATION);

				String postContent = PostContentField.getText();
				String author = AuthorField.getText();
				int likes;
				int shares;
				String dateTime = DateField.getText();
				int postID = 0;

				try {
					postID = Integer.parseInt(PostIDField.getText());
				} catch (NumberFormatException e) {
					// Parsing as an integer failed
					showAlert(alert, "Error", "Invalid Post ID", "Please enter a valid Post ID.");
					alert.showAndWait();
					return;
				}

				if (postID == 0) {
					showAlert(alert, "Error", "Invalid Post ID", "Post ID cannot be zero.");
					alert.showAndWait();
					return;
				}

				try {
					likes = Integer.parseInt(LikesField.getText());
					shares = Integer.parseInt(SharesField.getText());
				} catch (NumberFormatException e) {
					showAlert(alert, "Error", "Invalid Data", "Likes and Shares must be non-negative integers.");
					alert.showAndWait();
					return;
				}
				if (postContent.isEmpty() || dateTime.isEmpty()) {
					showAlert(alert, "Error", "Invalid Data", "Please fill all fields.");
					alert.showAndWait();
					return;

				}

				// Validate date and time format
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
				try {
					LocalDateTime parsedDateTime = LocalDateTime.parse(dateTime, formatter);
				} catch (DateTimeParseException e) {
					showAlert(alert, "Error", "Invalid Date & Time", "Date & Time format should be dd/MM/yyyy HH:mm.");
					alert.showAndWait();
					return;
				}

				Post newPost = new Post(postID, postContent, author, likes, shares, dateTime);
				boolean result;
				try {
					result = post.CreateNewPost(newPost);
					if (result) {
						showAlert(alert, "Success", "Post successfully added",
								"Post with ID " + postID + " successfully added.");
					} else {
						showAlert(alert, "Error", "Failed to add post", "Something went wrong, please try again.");
					}
				} catch (PostIDInvalid | PostIDExists e) {
					showAlert(alert, "Error", "Invalid Post ID",
							"Post ID is invalid or already exists. Please provide unique Post ID");
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
				alert.showAndWait();
			}
		});

		return tab;
	}

	/// End

	private Tab createDeletePostTab() {
		Tab tab = new Tab("Delete Post");

		Label PostIDLabel = new Label("Post ID:");

		TextField PostIDField = new TextField();

		Button DeletePostButton = new Button("Delete Post");

		GridPane DeletePostContent = createForm("Delete Post by ID");

		DeletePostContent.add(PostIDLabel, 0, 1);
		DeletePostContent.add(PostIDField, 1, 1);
		DeletePostContent.add(DeletePostButton, 0, 2);

		tab.setContent(DeletePostContent);

		DeletePostButton.setOnAction(new EventHandler<ActionEvent>() {
			Alert alert = new Alert(AlertType.INFORMATION);

			@Override
			public void handle(ActionEvent event) {
				try {
					int postID = Integer.parseInt(PostIDField.getText());
					Post post = new Post();

					if (!post.postIDExists(postID)) {
						showAlert(alert, "Error", "Invalid Post ID", "Post ID is not valid or not exists.");
						PostIDField.setText("");
						// return;
					}

					boolean QueryResult = post.DeletePost(postID);
					if (QueryResult) {
						showAlert(alert, "Success", "Post Deleted", "Post with ID " + postID + " Deleted Successfully");
					}

				} catch (NumberFormatException e) {
					showAlert(alert, "Error", "Invalid Post ID", "Invalid input for Post ID.");
				}
				alert.showAndWait();
			}
		});

		return tab;
	}

	private Tab createShowPostDetailsTab() {
		Tab tab = new Tab("Show Post Details");

		Label PostIDLabel = new Label("Post ID:");
		TextField PostIDField = new TextField();
		Button GetPostButton = new Button("Get Post details");
		Text Result = new Text();

		GridPane PostDetailsTab = createForm("Show Post Details by ID");

		PostDetailsTab.add(PostIDLabel, 0, 1);
		PostDetailsTab.add(PostIDField, 1, 1);
		PostDetailsTab.add(GetPostButton, 0, 2);
		PostDetailsTab.add(Result, 0, 3);

		// Create a TableView and define columns
		TableView<Map.Entry<String, String>> tableView = new TableView<>();
		TableColumn<Map.Entry<String, String>, String> column1 = new TableColumn<>("Field Name");
		column1.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getKey()));
		TableColumn<Map.Entry<String, String>, String> column2 = new TableColumn<>("Field Value");
		column2.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue()));

		tableView.getColumns().addAll(column1, column2);

		tableView.setFixedCellSize(30); // Adjust the height as needed

		// Set the maximum number of visible rows
		tableView.setPrefHeight(7 * tableView.getFixedCellSize());

		GetPostButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				tableView.getItems().clear(); // Clear previous data
				String postIDText = PostIDField.getText();

				Alert alert = new Alert(AlertType.INFORMATION); // Create the alert here

				if (!postIDText.isEmpty()) {
					try {
						int postID = Integer.parseInt(postIDText);
						postInfo = post.GetPostDetails(postID);

						if (!postInfo.isEmpty()) {
							// Create an observable list to hold the data
							ObservableList<Map.Entry<String, String>> data = FXCollections
									.observableArrayList(postInfo.entrySet());

							// Set the data in the TableView
							tableView.setItems(data);
						} else {
							showAlert(alert, "Error", "Invalid Post ID", "Post with ID " + postID + " does not exist.");
							alert.showAndWait();
						}
					} catch (NumberFormatException e) {
						showAlert(alert, "Error", "Invalid Post ID", "Please enter a valid Post ID.");
						alert.showAndWait();
					}
				} else {
					showAlert(alert, "Error", "Invalid Post ID", "Please enter a valid Post ID");
					alert.showAndWait();
				}
			}
		});

		// Place the TableView under the input fields
		PostDetailsTab.add(tableView, 0, 4);

		tab.setContent(PostDetailsTab);

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
		comboBox.setValue("Likes");

		Button GetTopPostsButton = new Button("Get Posts");

		GridPane GetTopPosts = createForm("Get Top Posts by likes or shares");

		GetTopPosts.add(GetTopPostByLabel, 0, 1);
		GetTopPosts.add(comboBox, 1, 1);
		GetTopPosts.add(NumberofPostsLabel, 0, 2);
		GetTopPosts.add(NumberofPostsField, 1, 2);
		GetTopPosts.add(GetTopPostsButton, 0, 3);

		tab.setContent(GetTopPosts);

		// Create a TableView and define columns
		TableView<PostData> tableView = new TableView<>();
		TableColumn<PostData, String> column1 = new TableColumn<>("ID");
		column1.setCellValueFactory(new PropertyValueFactory<>("id"));
		TableColumn<PostData, String> column2 = new TableColumn<>("Post Content");
		column2.setCellValueFactory(new PropertyValueFactory<>("postContent"));
		TableColumn<PostData, String> column3 = new TableColumn<>("Author");
		column3.setCellValueFactory(new PropertyValueFactory<>("author"));
		TableColumn<PostData, String> column4 = new TableColumn<>("Likes");
		column4.setCellValueFactory(new PropertyValueFactory<>("likes"));
		TableColumn<PostData, String> column5 = new TableColumn<>("Shares");
		column5.setCellValueFactory(new PropertyValueFactory<>("shares"));
		TableColumn<PostData, String> column6 = new TableColumn<>("Date & Time");
		column6.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

		tableView.getColumns().addAll(column1, column2, column3, column4, column5, column6);

		tableView.setSortPolicy(param -> false);

		GetTopPostsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				tableView.getItems().clear(); // Clear previous data
				String Type = comboBox.getValue();
				String Count = "2";

				if (NumberofPostsField != null && !NumberofPostsField.getText().isEmpty()) {
					Count = NumberofPostsField.getText();
				}

				List<Map<String, String>> postList = post.GetTopPosts(Type, Count);

				if (postList != null && !postList.isEmpty()) {
					// Create an observable list to hold the data
					ObservableList<PostData> data = FXCollections.observableArrayList();

					for (Map<String, String> postInfo : postList) {
						PostData postData = new PostData(postInfo.get("ID"), postInfo.get("Content"),
								postInfo.get("Author"), postInfo.get("Likes"), postInfo.get("Shares"),
								postInfo.get("DateTime"));

						data.add(postData);
					}

					// Set the data in the TableView
					tableView.setItems(data);
				} else {
					showAlert(new Alert(AlertType.ERROR), "Error", "Not found", "No post found");
				}

			}
		});

		// Place the TableView under the input fields
		GetTopPosts.add(tableView, 0, 4);

		return tab;
	}

	private Tab createPostExporterTab() {

		Tab tab = new Tab("Export Post");

		Label PostIDLabel = new Label("Post ID");

		TextField PostIDField = new TextField("2");

		Button GetTopPostsButton = new Button("Export into csv file");

		GridPane GetTopPosts = createForm("Get Top Posts by likes or shares");

		GetTopPosts.add(PostIDLabel, 0, 1);
		GetTopPosts.add(PostIDField, 1, 1);
		GetTopPosts.add(GetTopPostsButton, 0, 2);

		tab.setContent(GetTopPosts);

		GetTopPostsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Alert alert = new Alert(AlertType.INFORMATION);

				try {
					int postID = Integer.parseInt(PostIDField.getText());

					// Check if the postID exists
					if (!post.postIDExists(postID)) {
						showAlert(alert, "Error", "Invalid Post ID not exists",
								"Please enter a valid Post ID as a number."); // Throw PostIDInvalid exception if it
																				// doesn't exist
						alert.showAndWait();
						return;
					}

					// The postID is valid, proceed to export
					boolean exportResult = post.exportToCSV(postID);

					if (exportResult) {
						showAlert(alert, "Success", "Post ID " + postID + " exported.",
								"Post data successfully exported into CSV file.");
					} else {
						showAlert(alert, "Error", "Failed to export Post ID " + postID,
								"Failed to export post data, please try again.");
					}

				} catch (NumberFormatException e) {
					showAlert(alert, "Error", "Invalid Post ID", "Please enter a valid Post ID as a number.");
				}

				alert.showAndWait();
			}
		});

		return tab;

	}

	private Tab createPostImportTab() {
		Tab tab = new Tab("Import Posts");

		Button ImportPosts = new Button("Click here to import posts");

		GridPane ImportPostContent = createForm("Import posts into database");

		ImportPostContent.add(ImportPosts, 0, 1);

		tab.setContent(ImportPostContent);
		Alert alert = new Alert(AlertType.INFORMATION);
		ImportPosts.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				boolean ExportResult;
				try {
					ExportResult = post.SocialMediaPostImporter();
					if (ExportResult) {
						showAlert(alert, "Success", "Successfull Import", "Posts successfully imported into databse");
					}
				} catch (IOException e) {
					showAlert(alert, "Error", "Failed Import", "Something went wrong, please try again");
				} catch (PostIDInvalid e) {
					// TODO Auto-generated catch block
				} catch (PostIDExists e) {
					showAlert(alert, "Error", "Failed Import", "Post ID(s) not unique.");
					// TODO Auto-generated catch block
				}
				alert.showAndWait();
			}
		});

		return tab;
	}

	private Tab createDataVisualizationTab() {
		Tab tab = new Tab("Data Visualization");

		PieChart Data = post.DataVisualization();

		GridPane DataVisualizationContent = createForm("Get Top Posts by likes or shares");

		DataVisualizationContent.add(Data, 0, 1);

		tab.setContent(DataVisualizationContent);

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
