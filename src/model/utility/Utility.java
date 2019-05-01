package model.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.CommandInfo;
import model.ErrorChecker;
import model.ProgramCounter;
import model.tables.DirectiveTable;
import model.tables.InstructionTable;
import model.tables.RegisterTable;

public class Utility {

    public static void clearAll() {
        ErrorChecker.getInstance().getLabelList().clear();
        ProgramCounter.getInstance().resetAddresses();
    }

    public static boolean isDirective(String directiveMnemonic) {
        if (DirectiveTable.directiveTable.containsKey(directiveMnemonic.toUpperCase()))
            return true;
        return false;
    }

    public static boolean isInstruction(String instructionMnemonic) {
        if (InstructionTable.instructionTable.containsKey(instructionMnemonic.toUpperCase()))
            return true;
        return false;
    }

    public static boolean isRegister(String registerName) {
        if (RegisterTable.registerTable.containsKey(registerName.toUpperCase()))
            return true;
        return false;

    }

    public static boolean isLabel(String labelName) {
        if (CommandInfo.labelList.contains(labelName))
            return true;
        return false;

    }

    public static boolean isComment(String line) {
        if (line.startsWith("."))
            return true;
        return false;
    }

    public static String getSpaces(int count) {
        String s = new String();
        for (int i = 0; i < count; i++)
            s += " ";
        return s;
    }
    public static String getMatch(String input,String regex){
        Pattern reg = Pattern.compile(regex);
        Matcher m=reg.matcher(input);
        if(m.find()){
            return m.group(1);
        }
        return "";
    }
    public static ArrayList<String> getMatches(String input, String regex){
        ArrayList<String> matchArr=new ArrayList<>();
        Pattern reg = Pattern.compile(regex);

        Matcher m=reg.matcher(input);
        while(m.find()){
            matchArr.add(m.group(1));
        }
        return matchArr;
    }
    public static int getNumberOfMatches(String input,String regex){
        /** compile the regex using Java regex engine */
        Pattern reg = Pattern.compile(regex);
        int numSpaces=0;
        Matcher m=reg.matcher(input);

        while(m.find()){
            numSpaces++;
        }
        return numSpaces;
    }
    public static String removeExtraSpaces(String input) {
        /**
         *  keep matching spaces if there are 3 or more spaces don't trim them and return
         *  e.g:_LABEL_LDA__ (underscores are spaces)
         *  and also return if label end has letter
         *  e.g : LABEL__LDA
         */
        int numSpaces=getNumberOfMatches(input,"(\\s+)");
        if(numSpaces>=3||input.charAt(input.length()-1)!=' ')return input;
        /**
         * get the index for first char after several spaces to keep misplaced label for example exist
         */

        int firstCharIdx = 0;
        for (int i = 0; i < input.length(); i++) {
            firstCharIdx = i;
            if (input.charAt(i) != ' ')
                break;

        }
        String s = "";
        if (firstCharIdx == input.length() - 1)
            return s;
        if (firstCharIdx > 0)
            s = input.substring(0, firstCharIdx);
        for (int i = firstCharIdx; i < input.length(); i++) {
            if (input.charAt(i) == ' ')
                break;
            s += input.charAt(i);
        }
        return s;
    }

    public static void writeFile(String s, String filePath) {
        File file = new File(filePath);

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(s);
            bw.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void extractAddressingModeFromOperand(String s, CommandInfo CI) {
        if (s.charAt(0) == '#' || s.charAt(0) == '@') {
            CI.addAddressMode(s.substring(0, 1));
            CI.addOperand1(s.substring(1, s.length()));
        } else {
            CI.addAddressMode("");
            CI.addOperand1(s);
        }
    }

    public static void processOperand(String input, CommandInfo CI) {
        String[] op1op2 = input.split(",");
        if (op1op2.length > 1) {
            extractAddressingModeFromOperand(op1op2[0], CI);
            CI.addOperand2(op1op2[1]);
        } else {
            extractAddressingModeFromOperand(op1op2[0], CI);
            CI.addOperand2("");
        }
    }

    public static boolean containsMisplacedLetter(String s) {

        int len = s.length();
        for (int i = 0; i < len; i++) {
            if (s.charAt(i) == ' ') return true;
        }
        return false;
    }

    public static boolean isThatStringEqualAnyDirective(String input) {

        return (input.equalsIgnoreCase("byte") || input.equalsIgnoreCase("resb") || input.equalsIgnoreCase("word")
                || input.equalsIgnoreCase("resw") || input.equalsIgnoreCase("equ") || input.equalsIgnoreCase("start")
                || input.equalsIgnoreCase("org") || input.equalsIgnoreCase("base") || input.equalsIgnoreCase("nobase")
                || input.equalsIgnoreCase("ltorg"));
    }
}
