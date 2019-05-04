package controller;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import model.CommandInfo;
import model.Instruction;
import model.Line;
import model.SourceReader;
import model.tables.DirectiveTable;
import model.tables.ErrorTable;
import model.tables.InstructionTable;
import model.tables.RegisterTable;
import model.utility.Utility;

public class Controller {
	
	String path;
	
	CommandInfo CI;
	ArrayList<Line> lineList;
	HashMap<String, Instruction> instructionTable;

	public void loadInstructionTable() {

		InstructionTable.loadInstructionTable("res/SIC-XE Instructions Opcode.txt");
	}

	public void loadDirectiveTable() {

		DirectiveTable.loadDirectiveTable();
	}

	public void loadErrorList() {

		ErrorTable.loadErrorList();
	}

	public void loadRegisterTable() {

		RegisterTable.loadRegisterTable();
	}

	public void prepareData() {

		loadInstructionTable();
		loadDirectiveTable();
		loadErrorList();
		loadRegisterTable();
	}

	public void prepareListFile() {

		final String lineSeparator = "-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-";
		final String startPassOne = "\n-_-_-_-_-_-_-_-_-_- S   T   A   R   T      O   F      P   A   S   S   1 -_-_-_-_-_-_-_-_-_-_-";
		final String TABLE_FORM = "LINES" + Utility.getSpaces(7) + "ADDRESS" + Utility.getSpaces(5) + "LABEL" + Utility.getSpaces(7) +
				"MNEMONIC" + Utility.getSpaces(4) + "ADDR_MODE" + Utility.getSpaces(3) + "OPERAND1" + Utility.getSpaces(4) + "OPERAND2" + Utility.getSpaces(4) + "COMMENTS\n";
		String toBePrintedInListFile = lineSeparator;
		toBePrintedInListFile += startPassOne + "\n\n";
		toBePrintedInListFile += TABLE_FORM;

		int len = CI.getLinesList().size();
		for (int i = 0; i < len; i++) {
			String lineCount = String.valueOf(i);
			toBePrintedInListFile += lineCount + Utility.getSpaces(12 - lineCount.length()) + CI.getLinesList().get(i).toString() + "\n";
		}
		// textArea.setText(toBePrintedInTextArea);
		Utility.writeFile(toBePrintedInListFile, "res/LIST/listFile.txt");
		String address = "";
		for (int i = 0; i < CI.getLinesList().size(); i++) {
			if(!CI.getLinesList().get(i).getLabel().equals("")&&!CI.getLinesList().get(i).getLabel().equals("(~)")){
				address +=CI.getLinesList().get(i).getLabel();
				address +=Utility.getSpaces(12-CI.getLinesList().get(i).getLabel().length());
				address +=CI.getLinesList().get(i).getLocation();
				address += "\n";
			}
		}
		Utility.writeFile(address, "res/LIST/symTable.txt");
	}

	public boolean passOne(String program, boolean restricted) {

		Utility.writeFile(program, "res/functionality/ASSEMBLING");
		CI = SourceReader.getInstance()
				.processFile(SourceReader.getInstance().readFile("res/functionality/ASSEMBLING"), restricted);

		boolean firstPassDone = CI.addToLineList();
		if(firstPassDone)
			prepareListFile();
		return firstPassDone;
	}
	
	public String getStartOfProgram() {
		
		String startOfProgram = "000000";
		for(Line line : lineList) {
			if(line.getMnemonic().equalsIgnoreCase("START")) {
				startOfProgram = "00" + line.getLocation();
				break;
			}
		}
		return startOfProgram;
	}
	
	public String getEndOfProgram() {
		
		String endOfProgram = null;
		for(Line line : lineList) {
			if(line.getMnemonic().equalsIgnoreCase("END"))
				endOfProgram = "00" + line.getLocation();
		}
		return endOfProgram;
	}
	
	public String getSizeOfProgram(String startOfProgram, String endOfProgram) {
		
		return "00" + Utility.convertToHexa(Utility.hexToDecimal(endOfProgram) - Utility.hexToDecimal(startOfProgram) + 1);
	}
	
	public String getProgramName() {
		
		String name = "";
		for(Line line : lineList) {
			if(line.getMnemonic().equalsIgnoreCase("START"))
				name = line.getLabel();
		}
		int length = name.length();
		if(length > 6)
			name = name.substring(0, 6);
		if(length < 6)
			name += Utility.getSpaces(6 - length);
		return name;
	}

	public void passTwo() {

		instructionTable = InstructionTable.instructionTable;
		lineList = CI.getLinesList();
		String startOfProgram = getStartOfProgram();
		String endOfProgram = getEndOfProgram();
		String sizeOfProgram = getSizeOfProgram(startOfProgram, endOfProgram);
		String programName = getProgramName();
		String headerRecord = "H^" + programName + "^" + startOfProgram + "^" + sizeOfProgram;
		System.out.println(headerRecord);
	}
	

	public void assemble(String program, boolean restricted) {

		passOne(program, restricted);
		passTwo();
		Utility.clearAll();
	}
	
	public String getListFile() {
		
		path = Paths.get(".").toAbsolutePath().normalize().toString() + "/res/LIST/listFile.txt";
        ArrayList<String> arr = SourceReader.getInstance().readFile(path);
        String append = "";
        for (String s : arr) {
            append += s + "\n";
        }
        return append;
	}
	
	public String loadFile(String path) {
		
		this.path = path;
        ArrayList<String> arr = SourceReader.getInstance().readFile(path);
        String append = "";
        for (String s : arr) {
            append += s + "\n";
        }
        return append;
	}
}