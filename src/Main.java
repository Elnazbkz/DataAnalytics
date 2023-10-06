
import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import sqlitedb.DBConnection;
public class Main extends Application {
	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) throws IOException {
		AppFirstPage FirstPage = new AppFirstPage(primaryStage);
		primaryStage.setTitle(FirstPage.getTitle()); // Set the stage title
		primaryStage.setScene(FirstPage.getScene()); // Place the scene in the stage
		primaryStage.setHeight(400);
		primaryStage.setWidth(600);
		primaryStage.show(); // Display the stage
	}
	public static void main(String[] args) {
		DBConnection dbconn = new DBConnection();
		launch(args);
	}
}