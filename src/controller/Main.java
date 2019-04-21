package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.ErrorChecker;
import model.SourceReader;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;



public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Path currentRelativePath = Paths.get("");
        //TODO DELETE THE FOLLOWING LINE
        String path = currentRelativePath.toAbsolutePath().toString() + "/src/view/Editor.fxml";

        Parent root = FXMLLoader.load(new File("src/view/Editor.fxml").toURI().toURL());
        primaryStage.setTitle("SIC/XE Assembler");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        SourceReader.CommandInfo CI=SourceReader.getInstance().processFile(SourceReader.getInstance().readFile("res/SIC files/SIC_1.txt"));
       //int len=CI.getWholeInstruction().size();
       //for(int i=0;i<len;i++){
       //        System.out.println(CI.getWholeInstruction().get(i));
       //        System.out.println(CI.getLabel().get(i));
       //        System.out.println(CI.getCommand().get(i));
       //        System.out.println(CI.getAddressMode().get(i));
       //        System.out.println(CI.getOperand1().get(i));
       //        System.out.println(CI.getTypeOperand().get(i));
       //        System.out.println(CI.getOperand2().get(i));
       //}
        ErrorChecker.getInstance();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
