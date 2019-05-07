package controller;

import javafx.application.Application;
import javafx.stage.Stage;
import view.Assembler;

public class Main extends Application {

	private Assembler assembler = new Assembler();

	public static void main(String[] args) {

		launch(args);
	}

	public void start(Stage primaryStage) throws Exception {

		assembler.initialize(primaryStage);
	}
}