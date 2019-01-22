package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class NodeFindrController implements Initializable {
	
	@FXML Label lineLabel = new Label();
	@FXML TextArea ta1 = new TextArea();
	@FXML TextArea ta2 = new TextArea();
	@FXML TextField tf = new TextField();
	@FXML Button fileSelect = new Button();
	@FXML Button analyze = new Button();
	@FXML MenuBar menuBar = new MenuBar();
	@FXML MenuItem saveItem = new MenuItem();
	@FXML MenuItem clearItem = new MenuItem(); 
	
	private String fileName = new String();
	private String searchNode = new String();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	public void ta1Entered(ActionEvent e) {
		ta1.getText();
	}
	
	public void ta2Entered(ActionEvent e) {
		ta2.getText();
	}
	
	public void tfEntered(ActionEvent e) {
		searchNode = tf.getText();
	}
	
	public void fileButtonPressed(ActionEvent e) {
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(FileUtils.getUserDirectory());
		File f = fc.showOpenDialog((Stage) fileSelect.getParent().getScene().getWindow());
		if (f != null) {
			fileName = f.getAbsolutePath();
		}
	}
	
	private void processSelectedFile(String fileName) {
		FileReader fileReader;
		try {
			fileReader = new FileReader(fileName);
			BufferedReader buffer = new BufferedReader(fileReader);
			String line = buffer.readLine();
			ArrayList<String> allNodes = new ArrayList<String>();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/************************** Menu bar items ***********************************/
	public void saveItemPressed(ActionEvent e) {
		
	}
	
	public void clearItemPressed(ActionEvent e) {
		
	}

	
}
