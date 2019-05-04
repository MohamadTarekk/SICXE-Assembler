package view;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import controller.Controller;
import model.SourceReader;

public class Assembler {

    public Stage window;
    FileChooser fileChooser;
    String path;

    public MenuItem clearResult;
    public MenuItem loadFile;
    public MenuItem saveFile;
    public MenuItem saveAsFile;
    public MenuItem assemble;
    public CheckMenuItem restricted;
    public Label restrictedMsgLabel;

    public TextArea textArea;
    
    Controller controller = new Controller();

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

    public void assembleOnAction() {

        controller.assemble(textArea.getText(), restricted.isSelected());
    }
    
    public void showListFile() {
    	
        textArea.setText(controller.getListFile());
    }

    public void loadFileOnAction() {

        fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString() + "/res/SIC files";
        fileChooser.setInitialDirectory(new File(currentPath));
        File file = fileChooser.showOpenDialog(window);
        if (file != null) {
            path = file.getAbsolutePath();
            textArea.setText(controller.loadFile(path));
        }
    }

    public void clearResultOnAction() {
        ArrayList<String> arr = SourceReader.getInstance().readFile("res/functionality/ASSEMBLING");
        String append = "";
        for (String s : arr) {
            append += s + "\n";
        }
        textArea.setText(append);
    }

    public void setRestrictedMsg() {
    	
        restrictedMsgLabel.setVisible(!restricted.isSelected());
    }
}