package model;

import model.tables.LiteralTable;

import java.util.ArrayList;
import java.util.HashMap;

import model.tables.ErrorTable;

public class CommandInfo {

	// private static CommandInfo instance = null;

	// public CommandInfo() {
	// }

	private ProgramCounter pc = ProgramCounter.getInstance();

	// info for each line command
	private ArrayList<String> wholeInstruction = new ArrayList<>();
	public static ArrayList<String> labelList = new ArrayList<>();
	private ArrayList<String> mnemonicList = new ArrayList<>();
	private ArrayList<String> addressingModeList = new ArrayList<>();
	private ArrayList<String> operand1List = new ArrayList<>();
	private ArrayList<String> operand2List = new ArrayList<>();
	private ArrayList<String> commentList = new ArrayList<>();

	// All of the lines
	private ArrayList<Line> linesList = new ArrayList<>();

	public void addDefaults() {
		addLabel("(~)");
		addCommand("NOP");
		addOperand1("");
		addAddressMode("");
		addOperand2("");

	}

	public boolean checkForErrors() {

		for (Line line : linesList) {
			if (!line.getError().equals("")) {
				return false;
			}
		}
		return true;
	}

	public void addLiteralsToPool() {
		Line line;
		for (HashMap.Entry<String, Literal> literal : LiteralTable.literalTable.entrySet()) {
			line = new Line("", "", "", literal.getValue().getOperand(), "", "");
			line.setLocation(literal.getValue().getAddress());
			linesList.add(line);
		}
	}

	public boolean addToLineList() {
		int length = wholeInstruction.size();
		Line line;
		for (int i = 0; i < length; i++) {
			line = new Line(labelList.get(i), mnemonicList.get(i).toUpperCase(), addressingModeList.get(i),
					operand1List.get(i), operand2List.get(i), commentList.get(i));
			ErrorChecker.getInstance().verifyLine(line);
			pc.updateCounters(line);
			linesList.add(line);
		}
		verifyEndAndStartStatements();
		return true;
	}

	public void verifyEndAndStartStatements() {
		int endCounter = 0;
		int startCounter = 0;
		for (Line line : linesList) {
			String mnemonic = line.getMnemonic();
			if (mnemonic.equalsIgnoreCase("END")) {
				endCounter++;
				if (endCounter > 1)
					line.setError(ErrorTable.errorList[ErrorTable.MORE_THAN_ONE_END]);
			}
			if (mnemonic.equalsIgnoreCase("START")) {
				startCounter++;
				if (startCounter > 1)
					line.setError(ErrorTable.errorList[ErrorTable.MORE_THAN_ONE_START]);
			}
		}

		if (endCounter < 1) {
			System.out.println(linesList.get(linesList.size() - 1));
			linesList.get(linesList.size() - 1).setError(ErrorTable.errorList[ErrorTable.MISSING_END_STATEMENT]);
		}
	}

	public ArrayList<String> getCommentList() {
		return commentList;
	}

	public void setCommentList(ArrayList<String> commentList) {
		this.commentList = commentList;
	}

	public ArrayList<String> getLabelList() {
		return labelList;
	}

	public void setLabelList(ArrayList<String> labelList) {
		CommandInfo.labelList = labelList;
	}

	public ArrayList<String> getMnemonicList() {
		return mnemonicList;
	}

	public void setMnemonicList(ArrayList<String> mnemonicList) {
		this.mnemonicList = mnemonicList;
	}

	public ArrayList<String> getAddressingModeList() {
		return addressingModeList;
	}

	public void setAddressingModeList(ArrayList<String> addressingModeList) {
		this.addressingModeList = addressingModeList;
	}

	public ArrayList<String> getOperand1List() {
		return operand1List;
	}

	public void setOperand1List(ArrayList<String> operand1List) {
		this.operand1List = operand1List;
	}

	public ArrayList<String> getOperand2List() {
		return operand2List;
	}

	public void setOperand2List(ArrayList<String> operand2List) {
		this.operand2List = operand2List;
	}

	public ArrayList<Line> getLinesList() {
		return linesList;
	}

	public void setLinesList(ArrayList<Line> linesList) {
		this.linesList = linesList;
	}

	public ArrayList<String> getWholeInstruction() {
		return wholeInstruction;
	}

	public void setWholeInstruction(ArrayList<String> wholeInstruction) {
		this.wholeInstruction = wholeInstruction;
	}

	public void addWholeInstruction(String s) {
		if (s == null) {
			wholeInstruction.add("");
			return;
		}
		wholeInstruction.add(s);
	}

	public void addLabel(String s) {
		if (s == null) {
			labelList.add("");
			return;
		}
		labelList.add(s);
	}

	public void addCommand(String s) {
		if (s == null) {
			mnemonicList.add("");
			return;
		}
		mnemonicList.add(s);
	}

	public void addAddressMode(String s) {
		if (s == null) {
			addressingModeList.add("");
			return;
		}
		addressingModeList.add(s);
	}

	public void addOperand1(String s) {
		if (s == null) {
			operand1List.add("");
			return;
		}
		operand1List.add(s);
	}

	public void addOperand2(String s) {

		if (s == null) {
			operand2List.add("");
			return;
		}
		operand2List.add(s);
	}

	public void addComment(String s) {
		if (s == null) {
			commentList.add("");
			return;
		}
		commentList.add(s);
	}

}