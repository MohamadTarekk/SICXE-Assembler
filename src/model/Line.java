package model;

public class Line {

	private String label;
	private String mnemonic;
	private String addressingMode;
	private String firstOperand;
	private String secondOperand;
	private String comment;

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

	@Override
	public String toString() {
		return "Line [label=" + label + ", mnemonic=" + mnemonic + ", addressingMode=" + addressingMode
				+ ", firstOperand=" + firstOperand + ", secondOperand=" + secondOperand + ", comment=" + comment + "]";
	}

}
