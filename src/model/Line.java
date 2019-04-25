package model;

public class Line {

	private String label;
	private String mnemonic;
	private String firstOperand;
	private String secondOperand;
	private String comment;

	public Line(String label, String mnemonic, String firstOperand, String secondOperand, String comment) {
		this.label = label;
		this.mnemonic = mnemonic;
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

}
