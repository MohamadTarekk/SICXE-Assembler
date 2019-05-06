package model;

import model.utility.Utility;

public class Literal {

	private String type;
	private String operand;
	private String address;

	public Literal(String operand, String address) {

		this.type = "" + operand.charAt(0);
		this.type = this.type.toUpperCase();
		this.operand = operand.substring(2, operand.length() - 2); // W'1234'
		this.address = address;
	}

	public int calculateLength() {

		int length = 0;
		switch (type) {
		case "W":
			length = 3;
			break;
		case "C":
			length = operand.length() - 3;
			break;
		case "X":
			length = (int) Math.ceil(((double) operand.length()) / 2);
			break;
		}
		return length;
	}

	public int getNumericValue() {
		int numericValue = 0;
		switch (type) {
		case "W":
			if (operand.charAt(0) == '-')
				numericValue = -1 * Integer.parseInt(operand.substring(1));
			else
				numericValue = Integer.parseInt(operand);
			break;
		case "C":
			break;
		case "X":
			break;
		}
		return numericValue;
	}

	@Override
	public String toString() {
		return operand + Utility.getSpaces(12 - operand.length()) + address + "\n";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOperand() {
		return operand;
	}

	public void setOperand(String operand) {
		this.operand = operand;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
