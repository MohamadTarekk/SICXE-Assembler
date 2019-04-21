package model;

import java.util.HashMap;

public class ErrorChecker {


    private HashMap<String,Integer> commandsMap;

    public static ErrorChecker getInstance(){
        if(instance==null)instance=new ErrorChecker();
        return instance;
    }

    private ErrorChecker() {
        commandsMap=SourceReader.getInstance().getInstructionOpCodeTable("res/SIC-XE Instructions Opcode.txt");


    };

    private static ErrorChecker instance=null;

}
