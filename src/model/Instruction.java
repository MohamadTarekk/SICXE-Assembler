package model;

import model.enums.*;

public class Instruction {

	private String name;
	private String opcode;
	private OperandType firstOperand;
	private OperandType secondOperand;
	private Format format;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOpcode() {
		return opcode;
	}

	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}

	public OperandType getFirstOperand() {
		return firstOperand;
	}

	public void setFirstOperand(OperandType firstOperand) {
		this.firstOperand = firstOperand;
	}

	public OperandType getSecondOperand() {
		return secondOperand;
	}

	public void setSecondOperand(OperandType secondOperand) {
		this.secondOperand = secondOperand;
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}

}
