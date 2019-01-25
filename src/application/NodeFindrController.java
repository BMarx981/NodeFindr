package application;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;
import org.controlsfx.control.ToggleSwitch;
import org.w3c.dom.Node;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class NodeFindrController implements Initializable {
	
	@FXML TextArea ta1 = new TextArea();
	@FXML TextArea ta2 = new TextArea();
	@FXML TextField tf = new TextField();
	@FXML Button fileSelect = new Button();
	@FXML Button analyze = new Button();
	@FXML MenuBar menuBar = new MenuBar();
	@FXML MenuItem saveItem = new MenuItem();
	@FXML MenuItem clearItem = new MenuItem(); 
	@FXML ToggleSwitch toggle = new ToggleSwitch();
	
	private String fileName = new String();
	private String searchNode = new String();
	ArrayList<Node> nodeList = new ArrayList<Node>();
	ArrayList<Node> searchList = new ArrayList<Node>();
	XMLFileProcessor xp = new XMLFileProcessor();
	
	boolean isToggled = false;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		toggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
			isToggled = !isToggled;
		});
	}
	
	public void ta1Entered(ActionEvent e) {
		ta1.getText();
	}
	
	public void ta2Entered(ActionEvent e) {
		fileButtonPressed(e);
	}
	
	public void tfEntered(ActionEvent e) {
		searchNode = tf.getText();
	}
	
	public void analyzeButtonPressed(ActionEvent e) {
		searchNode = tf.getText();
		ArrayList<Node> foundList = new ArrayList<Node>();
		if (!searchNode.isEmpty() && nodeList.size() > 0) {
			for (Node n : nodeList) {
				foundList.addAll(xp.findNodesWith(searchNode, n));
			}
		} else if (searchNode.isEmpty()) {
			tf.setText("Please enter a value here!");
			return;
		} else if (nodeList.size() == 0) {
			tf.setText("Please select a file to analyze.");
		} else if (nodeList.size() == 0 && searchNode.isEmpty()) {
			tf.setText("Please select a file to analyze and a search value here.");
		}
		
		if (searchList.size() == 0) {
			tf.setText("No matching nodes found.");
		} else if (searchList.size() > 0) {
			print(searchList);
		}
	}
	
	public void fileButtonPressed(ActionEvent e) {
		searchNode = tf.getText();
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(FileUtils.getUserDirectory());
		File f = fc.showOpenDialog((Stage) fileSelect.getParent().getScene().getWindow());
		if (f != null) {
			fileName = f.getAbsolutePath();
			processSelectedFile(fileName);
		}
	}
	
	private void processSelectedFile(String fileName) {
		ta2.clear();
		nodeList.clear();
		xp.processXMLNodes(fileName, searchNode, isToggled);
		nodeList = xp.getExtractedNodes();
		print(nodeList);
	}
	
	private void print(ArrayList<Node> list) {
		if (list != null) {
			StringBuilder sb = new StringBuilder();
			for (Node n : list) {
				sb.append(ta2.getText()).append("\n").append(xp.xmlToString(n, true, true));
			}
			ta2.setText(sb.toString());
		}
	}
	
	/************************** Menu bar items ***********************************/
	public void saveItemPressed(ActionEvent e) {
		
	}
	
	public void clearItemPressed(ActionEvent e) {
		ta1.setText("");
		ta2.setText("");
		tf.setText("");
		fileName = new String();
		searchNode = new String();
		nodeList = new ArrayList<Node>();
		searchList = new ArrayList<Node>();
	}

}
