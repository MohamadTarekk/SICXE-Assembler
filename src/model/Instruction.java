package model;

import model.enums.*;

public class Instruction {

	private String name;
	private int opcode;
	private OperandType firstOperand;
	private OperandType secondOperand;
	private Format format;

	public Instruction(String name, int opcode) {
		super();
		this.name = name;
		this.opcode = opcode;
	}

	public Instruction(String name, int opcode, OperandType firstOperand, OperandType secondOperand, Format format) {
		this.name = name;
		this.opcode = opcode;
		this.firstOperand = firstOperand;
		this.secondOperand = secondOperand;
		this.format = format;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOpcode() {
		return opcode;
	}

	public void setOpcode(int opcode) {
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

	public boolean hasFirstOperand() {
		if (firstOperand == OperandType.REGISTER || firstOperand == OperandType.VALUE)
			return true;
		return false;
	}

	public boolean hasSecondOperand() {
		if (secondOperand == OperandType.REGISTER || secondOperand == OperandType.VALUE)
			return true;
		return false;
	}

	public int numberOfOperands() {

		int sum = 0;
		if (hasFirstOperand())
			sum++;
		if (hasSecondOperand())
			sum++;
		return sum;
	}

	@Override
	public String toString() {
		return "Instruction [name=" + name + ", opcode=" + opcode + ", firstOperand=" + firstOperand
				+ ", secondOperand=" + secondOperand + ", format=" + format + "]";
	}

}
