package view;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import controller.Controller;
import model.SourceReader;

public class Assembler {

	private Stage window;

	public MenuItem clearResult;
	public MenuItem loadFile;
	// public MenuItem saveFile;
	// public MenuItem saveAsFile;
	public MenuItem assemble;
	public CheckMenuItem restricted;
	public Label restrictedMsgLabel;

	public TextArea textArea;

	private Controller controller = new Controller();

	public void initialize(Stage primaryStage) {

		window = primaryStage;
		primaryStage.setTitle("SIC/XE Assembler");

		try {
			Parent root = FXMLLoader.load(new File("src/view/scene.fxml").toURI().toURL());
			Scene scene = new Scene(root, 1000, 750);
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

		controller.prepareData();
	}

	private void showAssembleMsgDialog(boolean noErrors) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Note");
		alert.setHeaderText(null);
		String msg;
		msg = noErrors ? "Successful Assembly" : "Incomplete Assembly";
		alert.setContentText(msg);

		alert.showAndWait();
	}

	public void assembleOnAction() {

		controller.assemble(textArea.getText(), restricted.isSelected());
		showAssembleMsgDialog(controller.isNoErrors());
	}

	public void showListFile() {

		textArea.setText(controller.getListFile());
	}

	public void loadFileOnAction() {

		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);
		String currentPath = Paths.get(".").toAbsolutePath().normalize().toString() + "/res/SIC files";
		fileChooser.setInitialDirectory(new File(currentPath));
		File file = fileChooser.showOpenDialog(window);
		if (file != null) {
			String path = file.getAbsolutePath();
			textArea.setText(controller.loadFile(path));
		}
	}

	public void clearResultOnAction() {
		ArrayList<String> arr = SourceReader.getInstance().readFile("res/functionality/ASSEMBLING");
		String append = "";
		for (String s : arr) {
			// noinspection StringConcatenationInLoop
			append += s + "\n";
		}
		textArea.setText(append);
	}
	
	public void showSymbolTable() {
		ArrayList<String> arr = SourceReader.getInstance().readFile("res/LIST/symTable.txt");
		String append = "";
		for (String s : arr) {
			// noinspection StringConcatenationInLoop
			append += s + "\n";
		}
		textArea.setText(append);
	}
	
	public void showObjectFile() {
		ArrayList<String> arr = SourceReader.getInstance().readFile("res/LIST/objFile.o");
		String append = "";
		for (String s : arr) {
			// noinspection StringConcatenationInLoop
			append += s + "\n";
		}
		textArea.setText(append);
	}
	
	public void setRestrictedMsg() {

		restrictedMsgLabel.setVisible(!restricted.isSelected());
	}
}