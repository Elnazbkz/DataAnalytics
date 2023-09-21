
import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) throws IOException {
		
		SalaryLabelGUIFX scene1 = new SalaryLabelGUIFX();
		
		//CalculateAreaScene scene2 = new CalculateAreaScene();
			
		primaryStage.setTitle(scene1.getTitle()); // Set the stage title
		primaryStage.setScene(scene1.getScene()); // Place the scene in the stage
		primaryStage.show(); // Display the stage
	}

	/**
	 * The main method is only needed for the IDE with limited JavaFX support. Not
	 * needed for running from the command line.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}