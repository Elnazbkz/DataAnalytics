import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class AppLoginGUIFX {
	private TextField UsernameTextField;
	private TextField PasswordTextField;
	
	
	public TextField GetUserName() {
		return UsernameTextField;
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
	    Label usernameLabel = new Label("Username:");
	    Label passwordLabel = new Label("Password:");

	    // Generate TextFields
	    TextField usernameTextField = new TextField();
	    TextField passwordTextField = new TextField();

	    // Generate Buttons
	    Button loginButton = new Button("Sign In");
	    Button registerButton = new Button("Sign Up");

	    GridPane formGridPane = new GridPane();
	    formGridPane.setAlignment(Pos.CENTER);
	    formGridPane.setHgap(10); // Added gap between columns
	    formGridPane.setVgap(10); // Added gap between rows

	    // Add Heading
	    formGridPane.add(heading, 0, 0, 2, 1); // Spanning 2 columns

	    // UserName
	    formGridPane.add(usernameLabel, 0, 1);
	    formGridPane.add(usernameTextField, 1, 1);

	    // Password
	    formGridPane.add(passwordLabel, 0, 2);
	    formGridPane.add(passwordTextField, 1, 2);

	    // Buttons
	    formGridPane.add(loginButton, 0, 3);
	    formGridPane.add(registerButton, 1, 3);

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



