package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import org.apache.commons.io.FileUtils;
import org.controlsfx.control.ToggleSwitch;
import org.w3c.dom.Node;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
			tf.setPromptText("No matching nodes found.");
			ta2.setText("");
			try {ta2.setText("No matching nodes found \n" + backgroundProcess(fileName).get()); }
			catch (Exception e1) { e1.printStackTrace(); }
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
		try {
			ta2.setText(backgroundProcess(fileName).get());
		} catch (InterruptedException e) {
			System.out.println("That shit was interrupted");
			e.printStackTrace();
		} catch (ExecutionException e) {
			System.out.println("That shit was not executed");
			e.printStackTrace();
		}
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
	
	public Task<String> backgroundProcess(String fileName){

		Task<String> processTask = new Task<String>() {

			@Override
			protected String call() throws Exception {
				FileReader fileReader;
				String processed = new String();
				long total = Files.size(Paths.get(fileName));
				long byteSize = 1;
				try {
					fileReader = new FileReader(fileName);
					BufferedReader buffer = new BufferedReader(fileReader);
					StringBuilder sb = new StringBuilder();
					String line = buffer.readLine();
					
					while((line = buffer.readLine()) != null) {
						sb.append(line).append("\n");
						byte[] ba = line.getBytes();
						byteSize += ba.length;
//						System.out.println(byteSize + " byteSize\t" + total + " total");
//						pb.setProgress(byteSize/total);
						updateProgress(byteSize, total);
						processed = sb.toString();
					}
					
					buffer.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return processed;
			}
		};
		
		ProgressBar pb = new ProgressBar();
		pb.setPrefWidth(200.0d);
		
		VBox updatePane = new VBox();
        updatePane.setPadding(new Insets(10));
        updatePane.setSpacing(5.0d);
        updatePane.getChildren().add(pb);
		
        Stage taskUpdateStage = new Stage(StageStyle.UTILITY);
        taskUpdateStage.initModality(Modality.APPLICATION_MODAL);
        taskUpdateStage.setScene(new Scene(updatePane));
        taskUpdateStage.setTitle("Loading.");
        taskUpdateStage.show();
		
        pb.progressProperty().bind(processTask.progressProperty());
		processTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                taskUpdateStage.hide();
            }
        });
		
        new Thread(processTask).start();
		return processTask;
	}
}
