import java.sql.Connection;
import java.sql.SQLException;

import appException.Exceptions.EmailExistsException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sqlitedb.SQLiteJDBC;

public class AppRegisterGUIFX {
    private TextField firstName;
    private TextField lastName;
    private TextField emailTextField;
    private PasswordField passwordField;
    private Stage primaryStage;
    private AppFirstPage firstPage;

    public AppRegisterGUIFX(Stage primaryStage, AppFirstPage firstPage) {
        this.primaryStage = primaryStage;
        this.firstPage = firstPage;
    }

    public TextField getFirstName() {
        return firstName;
    }

    public TextField getLastName() {
        return lastName;
    }

    public TextField getEmailTextField() {
        return emailTextField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public String getTitle() {
        return "Register in Platform";
    }

    public Scene getScene() { 
        Text heading = new Text("Register");

        Label emailLabel = new Label("Email Address:");
        Label firstNameLabel = new Label("First Name:");
        Label lastNameLabel = new Label("Last Name:");
        Label passwordLabel = new Label("Password:");

        firstName = new TextField();
        lastName = new TextField();
        emailTextField = new TextField();
        passwordField = new PasswordField();

        Button registerButton = new Button("Register Now");
        Button backButton = new Button("Back to Home");
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setTitle(firstPage.getTitle());
                primaryStage.setScene(firstPage.getScene());
            }
        });
        
        registerButton.setOnAction(new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent event) {
        		User user = null;
				user = new User();
        		String EmailAddress = emailTextField.getText();
        		Alert alert = new Alert(AlertType.INFORMATION);
        		try {
					if(user.userEmailExists(EmailAddress) != 0) {
						alert.setTitle("Registration Failed.");
						alert.setHeaderText("The email address is already exists.");
						alert.setContentText("Please log in to access your profile.");
						
					}
				} catch (EmailExistsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		String firstNameValue = firstName.getText();
        		String lastNameValue = lastName.getText();
        		String EmailValue = emailTextField.getText();
        		String passwordValue = passwordField.getText();
        		String accountTypeValue = "Basic";
        		int UserID = 0;
        		
        		try {
					User NewUser = new User(UserID, firstNameValue, lastNameValue, EmailValue, passwordValue, accountTypeValue);
					boolean result = user.createUser(NewUser);
					
					
					if(result == true) {
						alert.setTitle("Successfull Registration");
						alert.setHeaderText("Your account created.");
						alert.setContentText("Please log in to access your profile.");
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (EmailExistsException e) {
					alert.setTitle("Registration Failed.");
					alert.setHeaderText("The email address is already exists.");
					alert.setContentText("Please log in to access your profile.");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		alert.showAndWait();
        	}
        });

        GridPane formGridPane = new GridPane();
        formGridPane.setAlignment(Pos.CENTER);
        formGridPane.setHgap(10);
        formGridPane.setVgap(10);

        formGridPane.add(heading, 0, 0, 2, 1);

        formGridPane.add(firstNameLabel, 0, 1);
        formGridPane.add(firstName, 1, 1);

        formGridPane.add(lastNameLabel, 0, 2);
        formGridPane.add(lastName, 1, 2);

        formGridPane.add(emailLabel, 0, 3);
        formGridPane.add(emailTextField, 1, 3);

        formGridPane.add(passwordLabel, 0, 4);
        formGridPane.add(passwordField, 1, 4);

        formGridPane.add(registerButton, 0, 5);
        formGridPane.add(backButton, 1, 5);
        

        Scene scene = new Scene(formGridPane, 400, 300);
        return scene;
    }
}
