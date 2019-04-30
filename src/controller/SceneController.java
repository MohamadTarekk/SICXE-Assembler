package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import model.CommandInfo;
import model.SourceReader;
import model.utility.Utility;

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
        Utility.writeFile(textArea.getText(),"res/functionality/ASSEMBLING");
        CommandInfo CI = SourceReader.getInstance()
                .processFile(SourceReader.getInstance().readFile("res/functionality/ASSEMBLING"),restricted.isSelected());
        CI.addToLineList();

        int len = CI.getLinesList().size();
        for (int i = 0; i < len; i++) {
            System.out.println(CI.getLinesList().get(i).toString());
        }


    }
    @FXML
    private void loadFileOnAction(){
        ArrayList<String> arr=SourceReader.getInstance().readFile("res/SIC files/SIC_1.txt");
        String append="";
        for(String s:arr){
            append+=s+"\n";
        }

        textArea.setText(append);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        restricted.setSelected(true);
        textArea.setPromptText("Write your code here / load file to load default file.");
    }


}
