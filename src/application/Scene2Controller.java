package application;

import java.net.URL;
import java.util.ResourceBundle;

import org.w3c.dom.Document;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class Scene2Controller implements Initializable {
	
	@FXML private TextFlow flow1 = new TextFlow();
	@FXML private TextFlow flow2 = new TextFlow();
	@FXML private Button selectFileButton = new Button();
	@FXML private Button compareButton = new Button();
	@FXML private Label fileNameLable = new Label();
	@FXML private MenuBar menuBar = new MenuBar();
	@FXML private MenuItem saveItem = new MenuItem();
	@FXML private MenuItem clearItem = new MenuItem();
	@FXML private MenuItem findrItem = new MenuItem();
	
	private Document doc = null;
	private String name = "";
	XMLFileProcessor xp = new XMLFileProcessor();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		xp.processStringXML(name);
	}
	
	public void compareButtonPressed(ActionEvent e) {
		
	}
	
	//********************* Menu items *****************************/
	public void findrItemPressed(ActionEvent e) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/application/NodeFindr.fxml"));
			Stage stage = (Stage) compareButton.getScene().getWindow();
			Parent root = loader.load();
			root.getStylesheets().add("application/application.css");
			Scene setupScene = new Scene(root);
			stage.setScene(setupScene);
			stage.setTitle("Findrr ");
			NodeFindrController controller = loader.getController();
			stage.show();

		} catch (Exception ex) {
			System.out.println("scene fail\n" + ex);
			ex.printStackTrace();
		}
	}
	
	public void fileButtonPressed(ActionEvent e) {
		
	}
	
	public void setDoc(Document doc) {
		this.doc = doc;
	}
	
	public void setFileName(String name) {
		this.name = name;
	}


}
