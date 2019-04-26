package model.tables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Instruction;
import model.SourceReader;
import model.enums.Format;
import model.enums.OperandType;

public class InstructionTable {

	private static HashMap<String, Instruction> instructionTable = new HashMap<>();

	public HashMap<String, Instruction> getInstructionTable() {
		return instructionTable;
	}

	public void setInstructionTable(HashMap<String, Instruction> instructionTable) {
		InstructionTable.instructionTable = instructionTable;
	}
	
	public static void getInstructionOpCodeTable(String filePath) {
		ArrayList<String> fileInfo = SourceReader.getInstance().readFile(filePath);
		String regex = "(.+)[ |\\t]+([a-fA-F0-9]+)[ |\\t]+(\\S+)[ |\\t]+(.+)[ |\\t]+(.+)";
		Pattern reg = Pattern.compile(regex);
		int len = fileInfo.size();
		for (int i = 0; i < len; i++) {
			Matcher m = reg.matcher(fileInfo.get(i));
			if (!m.find())
				continue;
			Instruction instruction = new Instruction(m.group(1), Integer.parseInt(m.group(2), 16));
			String firstOperand = m.group(3);
			String secondOperand = m.group(4);
			String format = m.group(5);
			setInstruction(instruction, firstOperand, secondOperand, format);
			instructionTable.put(m.group(1), instruction);
		}
	}
	
	
	private static void setInstruction(Instruction instruction, String firstOperand, String secondOperand, String format) {
		switch(firstOperand) {
		case "REGISTER":
			instruction.setFirstOperand(OperandType.REGISTER);
			break;
		case "VALUE":
			instruction.setFirstOperand(OperandType.VALUE);
			break;
		case "NONE":
			instruction.setFirstOperand(OperandType.NONE);
			break;
		}
	
		switch(secondOperand) {
		case "REGISTER":
			instruction.setSecondOperand(OperandType.REGISTER);
			break;
		case "VALUE":
			instruction.setSecondOperand(OperandType.VALUE);
			break;
		case "NONE":
			instruction.setSecondOperand(OperandType.NONE);
			break;
		}
		
		switch(format) {
		case "ONE":
			instruction.setFormat(Format.ONE);
			break;
		case "TWO":
			instruction.setFormat(Format.TWO);
			break;
		case "THREE":
			instruction.setFormat(Format.THREE);
			break;
		case "FOUR":
			instruction.setFormat(Format.FOUR);
			break;
		}
	}
}
