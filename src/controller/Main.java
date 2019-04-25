package controller;

import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.ErrorChecker;
import model.SourceReader;



public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Path currentRelativePath = Paths.get("");
        //TODO DELETE THE FOLLOWING LINE
        //String path = currentRelativePath.toAbsolutePath().toString() + "/src/view/Editor.fxml";

        Parent root = FXMLLoader.load(new File("src/view/Editor.fxml").toURI().toURL());
        primaryStage.setTitle("SIC/XE Assembler");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        SourceReader.CommandInfo CI=SourceReader.getInstance().processFile(SourceReader.getInstance().readFile("res/SIC files/SIC_1.txt"));
        int len=CI.getWholeInstruction().size();
         for(int i=0;i<len;i++){
               //System.out.print(CI.getWholeInstruction().get(i));
               System.out.print("\t"+CI.getLabel().get(i));
               System.out.print("\t"+CI.getCommand().get(i));
               System.out.print("\t"+CI.getAddressMode().get(i));
               System.out.print("\t"+CI.getOperand1().get(i));
               System.out.print("\t"+CI.getTypeOperand().get(i));
               System.out.print("\t"+CI.getOperand2().get(i));
               System.out.println();
        }
        ErrorChecker.getInstance();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
