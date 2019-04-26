package model.utility;

import model.tables.DirectiveTable;
import model.tables.InstructionTable;

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
	
	public static boolean isComment(String line) {
		if(line.startsWith("."))
			return true;
		return false;	
	}

}
