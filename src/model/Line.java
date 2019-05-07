package model;

import model.utility.Utility;

public class Line {

	private String location;
	private String label;
	private String mnemonic;
	private String addressingMode;
	private String firstOperand;
	private String secondOperand;
	private String comment;
	private String error;

	public Line(String label, String mnemonic, String addressingMode, String firstOperand, String secondOperand,
			String comment) {
		this.label = label;
		this.mnemonic = mnemonic;
		this.addressingMode = addressingMode;
		this.firstOperand = firstOperand;
		this.secondOperand = secondOperand;
		this.comment = comment;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getMnemonic() {
		return mnemonic;
	}

	public void setMnemonic(String mnemonic) {
		this.mnemonic = mnemonic;
	}

	public String getAddressingMode() {
		return addressingMode;
	}

	public void setAddressingMode(String addressingMode) {
		this.addressingMode = addressingMode;
	}

	public String getFirstOperand() {
		return firstOperand;
	}

	public void setFirstOperand(String firstOperand) {
		this.firstOperand = firstOperand;
	}

	public String getSecondOperand() {
		return secondOperand;
	}

	public void setSecondOperand(String secondOperand) {
		this.secondOperand = secondOperand;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		if (label.contains("(~)") || mnemonic.equals("NOP"))
			return comment;
		final int maxSizePerInfo = 12;
		int maxSize = maxSizePerInfo;
		String lineInfo = location;

		lineInfo += Utility.getSpaces(maxSize - lineInfo.length());
		maxSize += maxSizePerInfo;
		lineInfo += label;
		lineInfo += Utility.getSpaces(maxSize - lineInfo.length());
		maxSize += maxSizePerInfo;
		lineInfo += mnemonic;
		lineInfo += Utility.getSpaces(maxSize - lineInfo.length());
		maxSize += maxSizePerInfo;
		lineInfo += addressingMode;
		lineInfo += Utility.getSpaces(maxSize - lineInfo.length());
		maxSize += maxSizePerInfo;
		lineInfo += firstOperand;
		lineInfo += Utility.getSpaces(maxSize - lineInfo.length());
		maxSize += maxSizePerInfo;
		lineInfo += secondOperand;
		lineInfo += Utility.getSpaces(maxSize - lineInfo.length());
		maxSize += maxSizePerInfo;
		lineInfo += comment;
		if (!error.equals(""))
			lineInfo += "\n" + error;

		return lineInfo;
	}
}
