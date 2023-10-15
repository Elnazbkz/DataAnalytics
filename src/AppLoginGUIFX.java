import java.sql.SQLException;

import appException.Exceptions.EmailExistsException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AppLoginGUIFX {
	private TextField EmailTextField;
	private TextField PasswordTextField;
	private Stage primaryStage;
	private AppFirstPage firstPage;
	private AppUserProfile profilePage;
  

    public AppLoginGUIFX(Stage primaryStage, AppFirstPage firstPage) {
        this.primaryStage = primaryStage;
        this.firstPage = firstPage;
    }

	    
	public TextField GetEmailAddress() {
		return EmailTextField;
	}
	
	public TextField GetPassword() {
		return PasswordTextField;
	}
	
	public String getTitle() {
		return "Login to Platform";
	}
	
	public Scene getScene() {

	    // Generate Heading
	    Text heading = new Text("Login");

	    // Generate Labels
	    Label EmailLabel = new Label("Username:");
	    Label passwordLabel = new Label("Password:");

	    // Generate TextFields
	    TextField EmailTextField = new TextField();
	    TextField passwordTextField = new PasswordField();

	    // Generate Buttons
	    Button loginButton = new Button("Login Now");
	    Button backButton = new Button("Back to Home");
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setTitle(firstPage.getTitle());
                primaryStage.setScene(firstPage.getScene());
                
            }
        });
        
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent event) {
        		User user = null;
				user = new User();
        		String EmailAddress = EmailTextField.getText();
        		int UserID = 0;
				try {
					UserID = user.userEmailExists(EmailAddress);
				} catch (EmailExistsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		if(UserID != 0) {
					AppUserProfile profileScene = new AppUserProfile(primaryStage, UserID);
				    primaryStage.setTitle(profileScene.getTitle());
				    primaryStage.setScene(profileScene.getScene());
					
				}
        	}
        });

	    GridPane formGridPane = new GridPane();
	    formGridPane.setAlignment(Pos.CENTER);
	    formGridPane.setHgap(10); // Added gap between columns
	    formGridPane.setVgap(10); // Added gap between rows

	    // Add Heading
	    formGridPane.add(heading, 0, 0, 2, 1); // Spanning 2 columns

	    // UserName
	    formGridPane.add(EmailLabel, 0, 1);
	    formGridPane.add(EmailTextField, 1, 1);

	    // Password
	    formGridPane.add(passwordLabel, 0, 2);
	    formGridPane.add(passwordTextField, 1, 2);

	    // Buttons
	    formGridPane.add(backButton, 0, 3);
	    formGridPane.add(loginButton, 1, 3);
	    

	    Scene scene = new Scene(formGridPane, 400, 300); // Added width and height
	    return scene;
	}
	
}


//class CalculateButtonEventHandler implements EventHandler<ActionEvent> {
//	
//	private SalaryLabelGUIFX guiInterface;
//
//	public CalculateButtonEventHandler(SalaryLabelGUIFX guiInterface) {
//		this.guiInterface = guiInterface;
//	}
//	
//	@Override
//	public void handle(ActionEvent arg0) {
//		double width = Double.parseDouble(guiInterface.getWidthTextField().getText());
//		double height = Double.parseDouble(guiInterface.getHeightTextField().getText());
//		
//		double area = width * height;
//		
//		guiInterface.getOutputLabel().setText(Double.toString(area));
//	}
//}



