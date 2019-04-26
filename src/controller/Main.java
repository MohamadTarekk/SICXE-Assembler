package controller;

import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.CommandInfo;
import model.ErrorChecker;
import model.SourceReader;
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

		CommandInfo CI = CommandInfo.getInstance()
				.processFile(SourceReader.getInstance().readFile("res/SIC files/SIC_1.txt"));
		CI.addToLineList();
		/*
		 * int len = CI.getLinesList().size(); for (int i = 0; i < len; i++) {
		 * System.out.println(CI.getLinesList().get(i).toString()); }
		 */

		InstructionTable.getInstructionOpCodeTable("res/SIC-XE Instructions Opcode.txt");
		InstructionTable it = new InstructionTable();
		for (String name : it.getInstructionTable().keySet()) {
			String key = name.toString();
			String value = it.getInstructionTable().get(name).toString();
			System.out.println(key + " " + value);
		}
		ErrorChecker.getInstance();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
