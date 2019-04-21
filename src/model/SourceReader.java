package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
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
    public CommandInfo processFile(ArrayList<String> fileInfo) {
        //need to match for commands , labels , ...
        CommandInfo CI = new CommandInfo();
        String regex = "(.+)[ |\\t]+(.+)[ |\\t]+([@#]?)([a-zA-Z0-9_]+)?,?([XC]?)([a-zA-Z0-9_]+)?";
        Pattern reg = Pattern.compile(regex);
        int len = fileInfo.size();
        for (int i = 0; i < len; i++) {
            Matcher m = reg.matcher(fileInfo.get(i));
            if(!m.find())continue;
            CI.addWholeInstruction(m.group(0));
            CI.addLabel(m.group(1));
            CI.addCommand(m.group(2));
            CI.addaddressMode(m.group(3));
            CI.addOperand1(m.group(4));
            CI.addOperand2(m.group(6));
            CI.addType(m.group(5));
        }

        return CI;
    }
    public class CommandInfo{//info for each line command

        public ArrayList<String> getCommands() {
            return command;
        }
        public void setCommands(ArrayList<String> commands) {
            this.command = commands;
        }
        public ArrayList<String> getLabels() {
            return label;
        }
        public void setLabels(ArrayList<String> labels) {
            this.label = labels;
        }
        public ArrayList<String> getCommand() { return command; }
        public void setCommand(ArrayList<String> command) { this.command = command;}
        public ArrayList<String> getLabel() { return label; }
        public void setLabel(ArrayList<String> label) { this.label = label; }
        public ArrayList<String> getOperand1() { return operand1; }
        public void setOperand1(ArrayList<String> operand1) { this.operand1 = operand1; }
        public ArrayList<String> getOperand2() { return operand2; }
        public void setOperand2(ArrayList<String> operand2) { this.operand2 = operand2; }
        public ArrayList<String> getAddressMode() { return addressMode; }
        public void setAddressMode(ArrayList<String> addressMode) { this.addressMode = addressMode; }
        public ArrayList<String> getTypeOperand() { return typeOperand;}
        public void setTypeOperand(ArrayList<String> typeOperand) { this.typeOperand = typeOperand; }
        public ArrayList<String> getWholeInstruction() { return wholeInstruction; }
        public void setWholeInstruction(ArrayList<String> wholeInstruction) { this.wholeInstruction = wholeInstruction; }

        public void addWholeInstruction(String s){wholeInstruction.add(s);}
        public void addLabel(String s){label.add(s);}
        public void addCommand(String s){command.add(s);}
        public void addaddressMode(String s){addressMode.add(s);}
        public void addOperand1(String s){operand1.add(s);}
        public void addOperand2(String s){operand2.add(s);}
        public void addType(String s){typeOperand.add(s);};

        private ArrayList<String> wholeInstruction=new ArrayList<>();
        private ArrayList<String> label=new ArrayList<>();
        private ArrayList<String> command=new ArrayList<>();
        private ArrayList<String> addressMode=new ArrayList<>();
        private ArrayList<String> operand1=new ArrayList<>();
        private ArrayList<String> operand2=new ArrayList<>();
        private ArrayList<String> typeOperand=new ArrayList<>();//hexa , char ,whatever


    }
    public HashMap<String,Integer> getInstructionOpCodeTable(String filePath){
        ArrayList<String> fileInfo=readFile(filePath);
        HashMap<String,Integer> opcodeTable=new HashMap<>();
        String regex = "(.+)[ |\\t]+([a-fA-F0-9]+)";
        Pattern reg = Pattern.compile(regex);
        int len = fileInfo.size();
        for (int i = 0; i < len; i++) {
            Matcher m = reg.matcher(fileInfo.get(i));
            if(!m.find())continue;
            opcodeTable.put(m.group(1),Integer.parseInt(m.group(2),16));
        }

        return opcodeTable;
    }

}
