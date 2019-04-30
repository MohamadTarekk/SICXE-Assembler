package model;

import java.util.ArrayList;

public class CommandInfo {

	private static CommandInfo instance = null;

	public CommandInfo() {
	}

	
	ProgramCounter pc = ProgramCounter.getInstance();

	// info for each line command
	private ArrayList<String> wholeInstruction = new ArrayList<>();
	private ArrayList<String> matchedInstruction = new ArrayList<>();
	public static ArrayList<String> labelList = new ArrayList<>();
	private ArrayList<String> mnemonicList = new ArrayList<>();
	private ArrayList<String> addressingModeList = new ArrayList<>();
	private ArrayList<String> operand1List = new ArrayList<>();
	private ArrayList<String> operand2List = new ArrayList<>();
	private ArrayList<String> commentList = new ArrayList<>();

	// All of the lines
	private ArrayList<Line> linesList = new ArrayList<Line>();

	public void addDefaults() {
		addMatchedInstruction("NOMATCH");
	    addLabel("(~)");
		addCommand("NOP");
		addOperand1("");
		addAddressMode("");
		addOperand2("");

	}

	public void addToLineList() {
		int length = wholeInstruction.size();
		for (int i = 0; i < length; i++) {
			Line line = new Line(labelList.get(i).toUpperCase(), mnemonicList.get(i).toUpperCase(), 
					addressingModeList.get(i).toUpperCase(),operand1List.get(i).toUpperCase(),
					operand2List.get(i).toUpperCase(), commentList.get(i));
			ErrorChecker.getInstance().verifyLine(line);
			pc.updateCounters(line);
			linesList.add(line);
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

	public static void setInstance(CommandInfo instance) {
		CommandInfo.instance = instance;
	}

	public ArrayList<String> getWholeInstruction() {
		return wholeInstruction;
	}

	public void setWholeInstruction(ArrayList<String> wholeInstruction) {
		this.wholeInstruction = wholeInstruction;
	}

	public ArrayList<String> getMatchedInstruction() {
		return matchedInstruction;
	}

	public void setMatchedInstruction(ArrayList<String> matchedInstruction) {
		this.matchedInstruction = matchedInstruction;
	}

	public void addWholeInstruction(String s) {
		if (s == null) {
			wholeInstruction.add("");
			return;
		}
		wholeInstruction.add(s);
	}

	public void addMatchedInstruction(String s) {
		if (s == null) {
			matchedInstruction.add("");
			return;
		}
		matchedInstruction.add(s);
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
	public void addComment(String s){
		if(s==null){
			commentList.add("");
		    return;
		}
	    commentList.add(s);
	}

}