package model.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

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

	public static String removeExtraSpaces(String input) {

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

	public static void processOperand(String input, CommandInfo CI) {
		String[] op1op2 = input.split(",");
		if (op1op2.length > 1) {
			if (op1op2[0].charAt(0) == '#' || op1op2[0].charAt(0) == '@') {
				CI.addAddressMode(op1op2[0].substring(0, 1));
				CI.addOperand1(op1op2[0].substring(1, op1op2[0].length()));
			} else {
				CI.addAddressMode("");
				CI.addOperand1(op1op2[0]);
			}
			CI.addOperand2(op1op2[1]);
		} else {
			CI.addOperand1(input);
			CI.addAddressMode("");
			CI.addOperand2("");
		}
	}

	public static boolean isThatStringEqualAnyDirective(String input) {

		return (input.equalsIgnoreCase("byte") || input.equalsIgnoreCase("resb") || input.equalsIgnoreCase("word")
				|| input.equalsIgnoreCase("resw") || input.equalsIgnoreCase("equ") || input.equalsIgnoreCase("start")
				|| input.equalsIgnoreCase("org") || input.equalsIgnoreCase("base") || input.equalsIgnoreCase("nobase")
				|| input.equalsIgnoreCase("ltorg"));
	}
}
