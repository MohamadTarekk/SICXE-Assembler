	package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import model.CommandInfo;
import model.SourceReader;
import model.utility.Utility;

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

        String toBePrinted=textArea.getText();
        toBePrinted+="\n-_-_-_-_-_-_-_-_-_-_--_-_-_-_-_-_-_-_-_-_--_-_-_-_-_-_-_-_-_-_--_-_-_-_-_-_-_-_-_-_--_-_-_-_-_-_-_-_-_-_--_-_-_-_-_-_-_-_-_-";
        toBePrinted+="\n    S   T   A   R   T      O   F      P   A   S   S   1";
        toBePrinted+="\n-_-_-_-_-_-_-_-_-_-_--_-_-_-_-_-_-_-_-_-_--_-_-_-_-_-_-_-_-_-_--_-_-_-_-_-_-_-_-_-_--_-_-_-_-_-_-_-_-_-_--_-_-_-_-_-_-_-_-_-\n";
        String TABLE_FORM="LINES"+Utility.getSpaces(7)+"ADDRESS"+Utility.getSpaces(5)+"LABEL"+Utility.getSpaces(7)+
                "MNEMONIC"+Utility.getSpaces(4)+"ADDR_MODE"+Utility.getSpaces(3)+"OPERAND1"+Utility.getSpaces(4)+"OPERAND2"+Utility.getSpaces(4)+"COMMENTS\n";
       toBePrinted+=TABLE_FORM;
        CI.addToLineList();
        int len = CI.getLinesList().size();
        for (int i = 0; i < len; i++) {
            //System.out.println(CI.getLinesList().get(i).toString());
            String lineCount=String.valueOf(i);
            toBePrinted+= lineCount+Utility.getSpaces(12-lineCount.length())+ CI.getLinesList().get(i).toString()+"\n";
        }
        textArea.setText(toBePrinted);
        Utility.writeFile(toBePrinted,"res/LIST/listFile.txt");

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
