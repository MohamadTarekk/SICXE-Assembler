package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SourceReader {

	private static SourceReader instance = null;

	public static SourceReader getInstance() {
		if (instance == null)
			instance = new SourceReader();
		return instance;
	}

	@SuppressWarnings("resource")
	public ArrayList<String> readFile(String filePath) {
		File file = new File(filePath);

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			ArrayList<String> lines = new ArrayList<>();
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				lines.add(currentLine);
			}
			return lines;
		} catch (Exception e) {
			System.err.println(e);
		}
		return null;
	}

	public CommandInfo processFile(ArrayList<String> fileInfo) {
		// need to match for commands , labels , ...
		CommandInfo CI = new CommandInfo();
		String regex = "(?:[\\s|\\t]+)?([a-zA-Z0-9_]+)?(?:[\\s|\\t]+)([a-zA-Z0-9_']+)(?:[ |\\t]+)?([@#]?)([a-zA-Z0-9_']+)?,?([a-zA-Z0-9_]+)?";
		Pattern reg = Pattern.compile(regex);
		int len = fileInfo.size();
		for (int i = 0; i < len; i++) {

			if(fileInfo.get(i).charAt(0)=='.')continue;
			Matcher m = reg.matcher(fileInfo.get(i));
			CI.addWholeInstruction(fileInfo.get(i));
			if (m.find()){
				if(m.group(4)==null){
					CI.addMatchedInstruction(m.group(0));
					CI.addLabel("");
					CI.addCommand(m.group(1));
					CI.addOperand1(m.group(2));
					CI.addAddressMode("");
					CI.addOperand2("");
				}else{
					CI.addLabel(m.group(1));
					CI.addCommand(m.group(2));
					CI.addAddressMode(m.group(3));
					CI.addOperand1(m.group(4));
					CI.addOperand2(m.group(5));
				}
			}else{
				CI.addDefaults();
			}


		}

		return CI;
	}

	public class CommandInfo {// info for each line command

		public ArrayList<String> getCommands() {
			return command;
		}

		public void setCommands(ArrayList<String> commands) {
			this.command = commands;
		}

		public ArrayList<String> getLabels() {
			return label;
		}

		public void setLabels(ArrayList<String> labels) {
			this.label = labels;
		}

		public ArrayList<String> getCommand() {
			return command;
		}

		public void setCommand(ArrayList<String> command) {
			this.command = command;
		}

		public ArrayList<String> getLabel() {
			return label;
		}

		public void setLabel(ArrayList<String> label) {
			this.label = label;
		}

		public ArrayList<String> getOperand1() {
			return operand1;
		}

		public void setOperand1(ArrayList<String> operand1) {
			this.operand1 = operand1;
		}

		public ArrayList<String> getOperand2() {
			return operand2;
		}

		public void setOperand2(ArrayList<String> operand2) {
			this.operand2 = operand2;
		}

		public ArrayList<String> getAddressMode() {
			return addressMode;
		}

		public void setAddressMode(ArrayList<String> addressMode) {
			this.addressMode = addressMode;
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
			if(s==null){
				wholeInstruction.add("");
				return;
			}
			wholeInstruction.add(s);
		}

		public void addMatchedInstruction(String s) {
			if(s==null){
				matchedInstruction.add("");
				return;
			}
			matchedInstruction.add(s);
		}

		public void addLabel(String s) {
			if(s==null){
				label.add("");
				return;
			}
			label.add(s);
		}

		public void addCommand(String s) {
			if(s==null){
				command.add("");
				return;
			}
			command.add(s);
		}

		public void addAddressMode(String s) {
			if(s==null){
				addressMode.add("");
				return;
			}
			addressMode.add(s);
		}

		public void addOperand1(String s) {
			if(s==null){
				operand1.add("");
				return;
			}
			operand1.add(s);
		}

		public void addOperand2(String s) {

			if(s==null){
				operand2.add("");
				return;
			}
			operand2.add(s);
		}

		public void addDefaults(){
			addMatchedInstruction("");
			addLabel("");
			addCommand("");
			addOperand1("");
			addOperand2("");
			addAddressMode("");
		}

		private ArrayList<String> wholeInstruction = new ArrayList<>();
		private ArrayList<String> matchedInstruction=new ArrayList<>();
		private ArrayList<String> label = new ArrayList<>();
		private ArrayList<String> command = new ArrayList<>();
		private ArrayList<String> addressMode = new ArrayList<>();
		private ArrayList<String> operand1 = new ArrayList<>();
		private ArrayList<String> operand2 = new ArrayList<>();

	}

	public HashMap<String, Integer> getInstructionOpCodeTable(String filePath) {
		ArrayList<String> fileInfo = readFile(filePath);
		HashMap<String, Integer> opcodeTable = new HashMap<>();
		String regex = "(.+)[ |\\t]+([a-fA-F0-9]+)";
		Pattern reg = Pattern.compile(regex);
		int len = fileInfo.size();
		for (int i = 0; i < len; i++) {
			Matcher m = reg.matcher(fileInfo.get(i));
			if (!m.find())
				continue;
			opcodeTable.put(m.group(1), Integer.parseInt(m.group(2), 16));
		}

		return opcodeTable;
	}

}
