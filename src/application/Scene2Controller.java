package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.text.TextFlow;

public class Scene2Controller implements Initializable {
	
	@FXML private TextFlow flow1 = new TextFlow();
	@FXML private TextFlow flow2 = new TextFlow();
	@FXML private Button fileSelectButton = new Button();
	@FXML private Label fileNameLable = new Label();
	@FXML private MenuBar menuBar = new MenuBar();
	@FXML private MenuItem saveItem = new MenuItem();
	@FXML private MenuItem clearItem = new MenuItem();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}
	
	

}
