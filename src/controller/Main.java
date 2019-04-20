package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.SourceReader;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;



public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Path currentRelativePath = Paths.get("");
        String path = currentRelativePath.toAbsolutePath().toString() + "\\src\\view\\Editor.fxml";
        Parent root = FXMLLoader.load(new File(path).toURI().toURL());
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
