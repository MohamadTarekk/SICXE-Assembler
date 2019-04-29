package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import model.SourceReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SceneController implements Initializable {

    @FXML
    private TextArea textArea;
    @FXML
    private Button assembleButton;
    @FXML
    private Button loadFileButton;
    @FXML
    private CheckBox restricted;
    @FXML
    private void assembleOnAction(){
    }
    @FXML
    private void loadFileOnAction(){
        ArrayList<String> arr=SourceReader.getInstance().readFile("res/SIC files/SIC_1.txt");
        textArea.setText("");
        int len=arr.size();
        for(String s:arr){
            textArea.setText(textArea.getText()+s);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }


}
