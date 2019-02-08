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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
	@FXML MenuItem nextItem = new MenuItem(); 
	@FXML ToggleSwitch toggle = new ToggleSwitch();
	@FXML Label toggleLabel = new Label();
	
	private String fileName = new String();
	private String searchNode = new String();
	ArrayList<Node> nodeList = new ArrayList<Node>();
	XMLFileProcessor xp = new XMLFileProcessor();
	
	boolean isToggled = false;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		toggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
			isToggled = !isToggled;
			changeToggleLabel();
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
		nodeList.clear();
		searchNode = tf.getText();
		xp.processXMLNodes(fileName, searchNode, isToggled);
		nodeList.clear();
		nodeList = xp.getExtractedNodes();
		
		if (fileName.equals("")) {
			tf.setText("Please select a file to analyze");
		} else if (nodeList.size() == 0) {
			tf.setText("No matching nodes found.");
			ta2.setText("");
		} else if (nodeList.size() > 0) {
			ta2.setText(nodeList.size() + " out of " + xp.getNodesCount() + " nodes found.");
			print(nodeList);
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
	
	private void changeToggleLabel() {
		toggleLabel.setText(isToggled ? "Content" : "Node");
	}
	
	private void processSelectedFile(String fileName) {
		ta2.clear();
		nodeList.clear();
		ta2.setText(xp.processStringXML(fileName));
	}
	
	private void print(ArrayList<Node> list) {
		if (list != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(nodeList.size() + " out of " + xp.getNodesCount() + " nodes found.");
			for (Node n : list) {
				sb.append("\n").append(xp.xmlToString(n, true, true));
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
	}
	
	public void nextItemPressed(ActionEvent e) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/application/NodeScene2.fxml"));
			Scene2Controller controller = loader.getController();
			controller.setDoc(xp.getDocument(fileName));
			controller.setFileName(fileName);
			Stage stage = (Stage) fileSelect.getScene().getWindow();
			Parent root = loader.load();
			root.getStylesheets().add("application/application.css");
			Scene setupScene = new Scene(root);
			stage.setScene(setupScene);
			stage.setTitle("Compr ");
			
			stage.show();

		} catch (Exception ex) {
			System.out.println("scene fail\n" + ex);
			ex.printStackTrace();
		}
	}

}
