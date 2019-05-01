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
import model.CommandInfo;
import model.SourceReader;
import model.tables.DirectiveTable;
import model.tables.ErrorTable;
import model.tables.InstructionTable;
import model.tables.RegisterTable;
import model.utility.Utility;

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

        InstructionTable.loadInstructionTable("res/SIC-XE Instructions Opcode.txt");
        DirectiveTable.loadDirectiveTable();
        ErrorTable.loadErrorList();
        RegisterTable.loadRegisterTabkle();
    }

    public void assembleOnAction() {
        Utility.writeFile(textArea.getText(), "res/functionality/ASSEMBLING");
        CommandInfo CI = SourceReader.getInstance()
                .processFile(SourceReader.getInstance().readFile("res/functionality/ASSEMBLING"), restricted.isSelected());

        boolean firstPassDone = CI.addToLineList();
        if (firstPassDone) {
            String toBePrintedInTextArea = textArea.getText();
            final String lineSeparator = "\n-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-";
            final String startPassOne = "\n    S   T   A   R   T      O   F      P   A   S   S   1";
            toBePrintedInTextArea += lineSeparator;
            toBePrintedInTextArea += startPassOne;
            toBePrintedInTextArea += lineSeparator + "\n";
            final String TABLE_FORM = "LINES" + Utility.getSpaces(7) + "ADDRESS" + Utility.getSpaces(5) + "LABEL" + Utility.getSpaces(7) +
                    "MNEMONIC" + Utility.getSpaces(4) + "ADDR_MODE" + Utility.getSpaces(3) + "OPERAND1" + Utility.getSpaces(4) + "OPERAND2" + Utility.getSpaces(4) + "COMMENTS\n";
            toBePrintedInTextArea += TABLE_FORM;
            String toBePrintedInListFile = lineSeparator;
            toBePrintedInListFile += startPassOne;
            toBePrintedInListFile += lineSeparator + "\n";
            toBePrintedInListFile += TABLE_FORM;

            int len = CI.getLinesList().size();
            for (int i = 0; i < len; i++) {
                String lineCount = String.valueOf(i);
                toBePrintedInTextArea += lineCount + Utility.getSpaces(12 - lineCount.length()) + CI.getLinesList().get(i).toString() + "\n";
                toBePrintedInListFile += lineCount + Utility.getSpaces(12 - lineCount.length()) + CI.getLinesList().get(i).toString() + "\n";
            }
            textArea.setText(toBePrintedInTextArea);
            Utility.writeFile(toBePrintedInListFile, "res/LIST/listFile.txt");
            String address = "";
            for (int i = 0; i < CI.getLinesList().size(); i++) {
                if(!CI.getLinesList().get(i).getLabel().equals("")&&!CI.getLinesList().get(i).getLabel().equals("(~)")){
                    address +=CI.getLinesList().get(i).getLabel();
                    address +=Utility.getSpaces(12-CI.getLinesList().get(i).getLabel().length());
                    address +=CI.getLinesList().get(i).getLocation();
                    address += "\n";
                }

			}
			Utility.writeFile(address, "res/LIST/symTable.txt");
            Utility.clearAll();
        }
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
            ArrayList<String> arr = SourceReader.getInstance().readFile(path);
            String append = "";
            for (String s : arr) {
                append += s + "\n";
            }
            textArea.setText(append);
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