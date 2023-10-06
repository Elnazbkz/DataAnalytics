import javafx.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
public class AppUserProfile {
	private Label FirstName;
	private Label LastName;
	private Label EmailAddress;
	private Label AccountType;
	private Stage PrimaryStage;
	
	 public AppUserProfile(Stage primaryStage) {
        this.PrimaryStage = primaryStage;
    }
	 public String GetFirstName() {
		 return FirstName.toString();
	 }
	 public String GetLastName() {
		 return LastName.toString();
	 }
	 public String GetEmailAddress() {
		 return EmailAddress.toString();
	 }
	 public String GetAccountType() {
		 return AccountType.toString();
	 }
	 
	 public String getTitle() {
		 return "User Profile";
	 }
	 
	 public Scene getScene() {
		 
		// Generate Heading
		 Text heading = new Text("Dear User, Welcome back.");
		 Text subHeading = new Text("You can edit your details using the form below");


	    GridPane formGridPane = new GridPane();
	    formGridPane.setAlignment(Pos.CENTER);
	    formGridPane.setHgap(10); // Added gap between columns
	    formGridPane.setVgap(10); // Added gap between rows

	    // Add Heading
	    formGridPane.add(heading, 0, 0, 2, 1); // Spanning 2 columns
	    formGridPane.add(subHeading, 0, 0, 2, 1); // Spanning 2 columns


	    Scene scene = new Scene(formGridPane, 400, 300); // Added width and height
	    return scene;
		 
		 
	 }
}