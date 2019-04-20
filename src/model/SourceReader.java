package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SourceReader {
   private static SourceReader instance=null;
    private SourceReader(){}
    public static SourceReader getInstance(){
        if(instance==null)instance=new SourceReader();
        return instance;
    }
    public ArrayList<String> readFile(String filePath){
        File file = new File(filePath);

        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            ArrayList<String> lines=new ArrayList<>();
            String currentLine;
            while ((currentLine = br.readLine()) != null){lines.add(currentLine);}
            return lines;
        }catch(Exception e){
            System.err.println(e);
        }
        return null;
    }
    public CommandInfo processFile(ArrayList<String> fileInfo){
        //need to match for commands , labels , ...
        CommandInfo CI=new CommandInfo();
        CI.setCommands(getCommands(fileInfo));
        CI.setLabels(getLabels(fileInfo));
        return CI;
    }
    private ArrayList<String> getCommands(ArrayList<String> fileInfo){
        ArrayList<String> commands=new ArrayList<>();
        String regex="";
        Pattern reg=Pattern.compile(regex);
        Matcher m ;

        return commands;
    }
    private ArrayList<String> getLabels(ArrayList<String> fileInfo){
        ArrayList<String> commands=new ArrayList<>();
        String regex="";
        Pattern reg=Pattern.compile(regex);
        Matcher m ;

        return commands;
    }

    public class CommandInfo{//info for each line command

        public ArrayList<String> getCommands() {
            return commands;
        }
        public void setCommands(ArrayList<String> commands) {
            this.commands = commands;
        }
        public ArrayList<String> getLabels() {
            return labels;
        }
        public void setLabels(ArrayList<String> labels) {
            this.labels = labels;
        }
        private ArrayList<String> commands;
        private ArrayList<String> labels;

    }


}
