package controller;

import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.CommandInfo;
import model.SourceReader;
import model.tables.DirectiveTable;
import model.tables.InstructionTable;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Path currentRelativePath = Paths.get("");
		// TODO DELETE THE FOLLOWING LINE
		// String path = currentRelativePath.toAbsolutePath().toString() +
		// "/src/view/Editor.fxml";

		Parent root = FXMLLoader.load(new File("src/view/Editor.fxml").toURI().toURL());
		primaryStage.setTitle("SIC/XE Assembler");
		primaryStage.setScene(new Scene(root, 300, 275));
		primaryStage.show();


		InstructionTable.loadInstructionTable("res/SIC-XE Instructions Opcode.txt");
		DirectiveTable.loadDirectiveTable();
		CommandInfo CI = SourceReader.getInstance()
				.processFile(SourceReader.getInstance().readFile("res/SIC files/SIC_1.txt"));
		CI.addToLineList();
		
		int len = CI.getLinesList().size();
		for (int i = 0; i < len; i++) {
			System.out.println(CI.getLinesList().get(i).toString());
		}
		
		/*for (String name : InstructionTable.instructionTable.keySet()) {
			String key = name.toString();
			String value = InstructionTable.instructionTable.get(name).toString();
			System.out.println(key + " " + value);
		}
		
		for (String name : DirectiveTable.directiveTable.keySet()) {
			String key = name.toString();
			String value = DirectiveTable.directiveTable.get(name).toString();
			System.out.println(key + " " + value);
		}*/
	}

	public static void main(String[] args) {
		launch(args);
	}
}
