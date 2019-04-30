package model.utility;

import model.CommandInfo;
import model.tables.DirectiveTable;
import model.tables.InstructionTable;
import model.tables.RegisterTable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class Utility {
	
	public static boolean isDirective(String directiveMnemonic) {
		if(DirectiveTable.directiveTable.containsKey(directiveMnemonic.toUpperCase())) 
			return true;
		return false;
	}
	
	public static boolean isInstruction(String instructionMnemonic) {
		if(InstructionTable.instructionTable.containsKey(instructionMnemonic.toUpperCase()))
			return true;
		return false;
	}
	
	public static boolean isRegister(String registerName) {
		if(RegisterTable.registerTable.containsKey(registerName.toUpperCase()))
			return true;
		return false;

	}
	
	public static boolean isLabel(String labelName) {
		if(CommandInfo.labelList.contains(labelName))
			return true;
		return false;

	}

	
	public static boolean isComment(String line) {
		if(line.startsWith("."))
			return true;
		return false;	
	}
	public static String getSpaces(int count){
		String s=new String();
		for(int i=0;i<count;i++)s+=" ";
		return s;
	}
	public static String removeExtraSpaces(String input){
		String s="";
		for(int i=0;i<input.length();i++){
			if(input.charAt(i)==' ')return s;
			s+=input.charAt(i);
		}
		return s;
	}
	public static void writeFile(String s,String filePath){
		File file = new File(filePath);

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(s);
			bw.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
