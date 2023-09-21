import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class Interfaceß  {
	
	private TextField widthTextField;
	private TextField heightTextField;
	
	private Label outputLabel;
	
	public Label getOutputLabel() {
		return outputLabel;
	}
	
	public String getTitle() {
		return "Calculate Area";
	}
	
	public TextField getWidthTextField() {
		return widthTextField;
	}
	
	public TextField getHeightTextField() {
		return heightTextField;
	}
	
	public Scene getScene() {
		
		// create 2 Label objects
		Label widthLabel = new Label("Width:");
		Label heightLabel = new Label("Height:");
		
		outputLabel = new Label("Test");
		
		// create 2 TextField objects
		widthTextField = new TextField();
		heightTextField = new TextField();
		
		// calculate button
		Button calculateButton = new Button("Calculate");
		
		calculateButton.setOnAction(new CalculateButtonEventHandler(this));
		
		// create a GridPane object
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(10, 10, 10, 10));
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		
		// add the labels to GridPane
		gridPane.add(widthLabel, 0, 0); // col, row
		gridPane.add(heightLabel, 0, 1);
		gridPane.add(outputLabel, 1, 2);
		
		
		// add the text fields to GridPane
		gridPane.add(widthTextField, 1, 0); // col, row
		gridPane.add(heightTextField, 1, 1); // col, row
		
		// add button to gridPane
		gridPane.add(calculateButton, 1, 3);
		
		// create a Scene object and pass the gridpane
		// to the constructor of the Scene
		Scene scene = new Scene(gridPane);

		// return the Scene
		return scene;
		
	}
}


class CalculateButtonEventHandler implements EventHandler<ActionEvent> {
	
	private SalaryLabelGUIFX guiInterface;

	public CalculateButtonEventHandler(SalaryLabelGUIFX guiInterface) {
		this.guiInterface = guiInterface;
	}
	
	@Override
	public void handle(ActionEvent arg0) {
		double width = Double.parseDouble(guiInterface.getWidthTextField().getText());
		double height = Double.parseDouble(guiInterface.getHeightTextField().getText());
		
		double area = width * height;
		
		guiInterface.getOutputLabel().setText(Double.toString(area));
	}
}