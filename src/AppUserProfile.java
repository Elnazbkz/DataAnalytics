import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;

public class AppUserProfile {
    private Label fullNameLabel;
    private Label accountTypeLabel;
    private Stage primaryStage;
    private User user;
    private Map<String, String> userInfo;

    public AppUserProfile(Stage primaryStage, int userID) {
        this.primaryStage = primaryStage;
        user = new User();
        userInfo = user.GetUserInfo(userID);
        if (userInfo != null) {
            initializeLabels();
        } else {
            System.err.println("User information is null for UserID: " + userID);
        }
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
        Label passwordLabel = new Label("Current Password:");
        Label NewpasswordLabel = new Label("New Password:");

        TextField firstName = new TextField(userInfo.get("FirstName"));
        TextField lastName = new TextField(userInfo.get("LastName"));
        TextField emailTextField = new TextField(userInfo.get("EmailAddress"));
        TextField passwordField = new PasswordField();
        TextField NewpasswordField = new PasswordField();
        GridPane editProfileContent = createForm("User Profile");
        Button UpdateProfileButton = new Button("Update Profile");
        // Add the labels to the form
        editProfileContent.add(fullNameLabel, 0, 1);
        editProfileContent.add(accountTypeLabel, 0, 2);
        
        editProfileContent.add(firstNameLabel,0, 3);
        editProfileContent.add(firstName, 1, 3);

        editProfileContent.add(lastNameLabel, 0, 4);
        editProfileContent.add(lastName, 1, 4);

        editProfileContent.add(emailLabel, 0, 5);
        editProfileContent.add(emailTextField, 1, 5);

        editProfileContent.add(passwordLabel, 0, 6);
        editProfileContent.add(passwordField, 1, 6);
        
        editProfileContent.add(NewpasswordLabel, 0, 7);
        editProfileContent.add(NewpasswordField, 1, 7);

        editProfileContent.add(UpdateProfileButton, 0, 8);
        

        tab.setContent(editProfileContent);
        return tab;
    }
    //// End 
    
    /// Tab content for Add new Post
    private Tab createAddPostTab() {
        Tab tab = new Tab("Add Post");
        tab.setContent(createForm("Add Post Content"));
        return tab;
    }
    
    /// End

    private Tab createDeletePostTab() {
        Tab tab = new Tab("Delete Post");
        tab.setContent(createForm("Delete Post Content"));
        return tab;
    }

    private Tab createShowPostDetailsTab() {
        Tab tab = new Tab("Show Post Details");
        tab.setContent(createForm("Show Post Details Content"));
        return tab;
    }

    private Tab createGetTopPostsTab() {
        Tab tab = new Tab("Get Top Posts");
        tab.setContent(createForm("Get Top Posts Content"));
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
