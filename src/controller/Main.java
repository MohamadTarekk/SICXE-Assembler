package controller;

import com.sun.javafx.UnmodifiableArrayList;
import javafx.application.Application;
import javafx.stage.Stage;
import model.utility.Utility;
import view.Assembler;

import java.util.ArrayList;
import java.util.Arrays;

public class Main extends Application {

	private Assembler assembler = new Assembler();

	public static void main(String[] args) {
		/*
		System.out.println(Utility.evaluate("*5-6"));
		String expression = "BETA + PLUS #@!$%^* ALPHA/GAMMA*10000";
		ArrayList<String> arr=Utility.getMatches(expression,"(\\S+)");
		StringBuilder result = new StringBuilder();
		for (String str : arr) {
			result.append(str);
		}
		System.out.println(result);
		String[] str = result.toString().split("(?<=[-+/*()])|(?=[-+/*()])");
		System.out.println(Arrays.toString(str));
		*/
		launch(args);
	}

	public void start(Stage primaryStage) throws Exception {

		assembler.initialize(primaryStage);
	}
}