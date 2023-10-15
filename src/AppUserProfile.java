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
        tabPane.getTabs().addAll(
                createEditProfileTab(),
                createAddPostTab(),
                createDeletePostTab(),
                createShowPostDetailsTab(),
                createGetTopPostsTab()
        );

        VBox vBox = new VBox(tabPane);
        Scene scene = new Scene(vBox, 600, 400);
        return scene;
    }
    
    //// Tab content for Edit Profile
    private Tab createEditProfileTab() {
        Tab tab = new Tab("Edit Profile");
        
        Label emailLabel = new Label("Email:");
        Label firstNameLabel = new Label("First Name:");
        Label lastNameLabel = new Label("Last Name:");

        
        TextField firstName = new TextField(userInfo.get("FirstName"));
        TextField lastName = new TextField(userInfo.get("LastName"));
        TextField emailTextField = new TextField(userInfo.get("EmailAddress"));
        Button UpdateProfileButton = new Button("Update Profile");
        Text Result = new Text();
        GridPane editProfileContent = createForm("User Profile");
        
        // Add the labels and text fields to the form
        editProfileContent.add(fullNameLabel, 0, 1);
        editProfileContent.add(accountTypeLabel, 0, 2);
        
        editProfileContent.add(firstNameLabel,0, 3);
        editProfileContent.add(firstName, 1, 3);

        editProfileContent.add(lastNameLabel, 0, 4);
        editProfileContent.add(lastName, 1, 4);

        editProfileContent.add(emailLabel, 0, 5);
        editProfileContent.add(emailTextField, 1, 5);

        editProfileContent.add(Result, 0, 6);
        editProfileContent.add(UpdateProfileButton, 0, 8);
        
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
        Label DateTimeLabel = new Label("Date&Time(dd/MM/yyyy HH:mm format");

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
        
        AddPostButton.setOnAction(event -> {
            try {
                int postID = Integer.parseInt(PostIDField.getText());

                if (postID == 0) {
                    Result.setText("Post ID cannot be zero.");
                    return;
                }

                postInfo = post.GetPostDetails(postID);
                if (postInfo != null) {
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
                int PostID = Integer.parseInt(PostIDField.getText());

				if (PostID != 0) {
					postInfo = post.GetPostDetails(PostID);
					String PostContent = postInfo.get("PostContent");
					String Author = postInfo.get("Author");
					int Likes = Integer.parseInt(postInfo.get("Likes"));
					int Shares = Integer.parseInt(postInfo.get("Shares"));
					String dateTimeString = postInfo.get("DateTime");
					
					Result.setText("ID: " + PostID + "\n" +
                            "Post Content: " + PostContent + "\n" +
                            "Author: " + Author + "\n" +
                            "Likes: " + Likes + "\n" +
                            "Shares: " + Shares + "\n" +
                            "Date & Time: " + dateTimeString);
				}
			}
        });
        return tab;
    }

    private Tab createGetTopPostsTab() {
        Tab tab = new Tab("Get Top Posts");
        
        Label GetTopPostByLabel = new Label("Select option to retrieve data");
        
        Label NumberofPostsLabel = new Label("Number of posts to retrieve");
        
        TextField NumberofPostsField = new TextField();
        
        ComboBox<String> comboBox = new ComboBox<>();
        ObservableList<String> options = FXCollections.observableArrayList(
                "Likes",
                "Shares"
        );
        comboBox.setItems(options);
        
        GridPane GetTopPosts = createForm("Get Top Posts by likes or shares");
        
        GetTopPosts.add(GetTopPostByLabel, 0, 1);
        GetTopPosts.add(comboBox, 1, 1);
        GetTopPosts.add(NumberofPostsLabel, 0, 2);
        GetTopPosts.add(NumberofPostsField, 0, 3);
        
        tab.setContent(GetTopPosts);
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
