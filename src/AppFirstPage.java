import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
public class AppFirstPage {	
	private Stage primaryStage; // Assuming this is the primary stage for the application
	
	

    public AppFirstPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
	
	public String getTitle() {
		return "Welcome to the Java Platform";
	}
	public Scene getScene() {

	    // Generate Heading
	    Text heading = new Text("Welcome to the Java Platform");

	    // Generate Buttons
	    Button loginButton = new Button("Login to platform");
	    Button registerButton = new Button("Register in platform");
	 // Event handler for login button
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	AppLoginGUIFX LoginScene = new AppLoginGUIFX(primaryStage, AppFirstPage.this);
            	primaryStage.setTitle(LoginScene.getTitle()); // Set the stage title
    			primaryStage.setScene(LoginScene.getScene()); // Place the scene in the stage
                
            }
        });
        
     // Event handler for login button
        registerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	AppRegisterGUIFX RegisterScene = new AppRegisterGUIFX(primaryStage, AppFirstPage.this);
            	primaryStage.setTitle(RegisterScene.getTitle()); // Set the stage title
    			primaryStage.setScene(RegisterScene.getScene()); // Place the scene in the stage
            	
            }
        });

	    GridPane formGridPane = new GridPane();
	    formGridPane.setAlignment(Pos.CENTER);
	    formGridPane.setHgap(10); // Added gap between columns
	    formGridPane.setVgap(10); // Added gap between rows

	    // Add Heading
	    formGridPane.add(heading, 0, 0, 2, 1); // Spanning 2 columns

	    // Buttons
	    formGridPane.add(loginButton, 0, 3);
	    formGridPane.add(registerButton, 1, 3);

	    Scene scene = new Scene(formGridPane, 400, 300); // Added width and height
	    return scene;
	}
	

	
}