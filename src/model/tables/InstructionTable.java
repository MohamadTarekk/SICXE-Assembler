package model.tables;

import java.util.HashMap;

import model.Instruction;

public class InstructionTable {

	private HashMap<String, Instruction> instructionTable = new HashMap<>();

	public HashMap<String, Instruction> getInstructionTable() {
		return instructionTable;
	}

	public void setInstructionTable(HashMap<String, Instruction> instructionTable) {
		this.instructionTable = instructionTable;
	}

}
