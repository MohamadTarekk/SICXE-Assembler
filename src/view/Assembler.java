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

	public MenuItem loadFile;
	public MenuItem saveFile;
	public MenuItem saveAsFile;
	public MenuItem assemble;
	public CheckMenuItem restricted;
	public Label restrictedMsgLabel;

	public TextArea textArea;

	public void initialize(Stage primaryStage) throws Exception {

		window = primaryStage;
		primaryStage.setTitle("SIC/XE Assembler");

		try {
			Parent root = FXMLLoader.load(new File("src/view/scene.fxml").toURI().toURL());
			Scene scene = new Scene(root, 1000, 750);
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
		// Path currentRelativePath = Paths.get("");
		// TODO DELETE THE FOLLOWING LINE
		// String path = currentRelativePath.toAbsolutePath().toString() +
		// "/src/view/Editor.fxml";


		InstructionTable.loadInstructionTable("res/SIC-XE Instructions Opcode.txt");
		DirectiveTable.loadDirectiveTable();
		ErrorTable.loadErrorList();
		RegisterTable.loadRegisterTabkle();
		//CommandInfo CI = SourceReader.getInstance()
		//		.processFile(SourceReader.getInstance().readFile("res/SIC files/SIC_1.txt"),true);
		//CI.addToLineList();
		//
		//int len = CI.getLinesList().size();
		//for (int i = 0; i < len; i++) {
		//	System.out.println(CI.getLinesList().get(i).toString());
		//}

		/*for (String name : InstructionTable.instructionTable.keySet()) {
		String key = name.toString();
		String value = InstructionTable.instructionTable.get(name).toString();
		System.out.println(key + " " + value);
		}*/

		/*for (String name : DirectiveTable.directiveTable.keySet()) {
		String key = name.toString();
		String value = DirectiveTable.directiveTable.get(name).toString();
		System.out.println(key + " " + value);
		}*/
	}

	public void assembleOnAction(){
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
		Utility.clearAll();
	}

	public void loadFileOnAction(){

		fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);
		String currentPath = Paths.get(".").toAbsolutePath().normalize().toString() + "/res/SIC files";
		fileChooser.setInitialDirectory(new File(currentPath));
		File file = fileChooser.showOpenDialog(window);
		if(file != null) {
			path = file.getAbsolutePath();
			ArrayList<String> arr=SourceReader.getInstance().readFile(path);
			String append="";
			for(String s:arr){
				append+=s+"\n";
			}
			textArea.setText(append);
		}
	}
	
	public void setRestrictedMsg() {
		System.out.println(restricted.isSelected());
		restrictedMsgLabel.setVisible(!restricted.isSelected());
	}
}