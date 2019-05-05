package controller;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import model.CommandInfo;
import model.Instruction;
import model.Line;
import model.SourceReader;
import model.Symbol;
import model.tables.DirectiveTable;
import model.tables.ErrorTable;
import model.tables.InstructionTable;
import model.tables.RegisterTable;
import model.tables.SymbolTable;
import model.utility.Utility;

public class Controller {

	CommandInfo CI;

	ArrayList<Line> lineList;
	HashMap<String, Instruction> instructionTable;

	private String path;
	private boolean noErrors = false;

	public boolean isNoErrors() {
		return noErrors;
	}

	public void setNoErrors(boolean noErrors) {
		this.noErrors = noErrors;
	}

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
		Symbol symbol;
		for(Line line : lineList) {
			if(!line.getLabel().equals("") && !line.getLabel().equals("(~)")) {
				symbol = new Symbol(line.getLabel(), line.getLocation());
				SymbolTable.symbolTable.put(symbol.getSymbol(), symbol);
			}
		}
		Utility.writeFile(SymbolTable.getString(), "res/LIST/symTable.txt");
	}

	public void passOne(String program, boolean restricted) {

		Utility.writeFile(program, "res/functionality/ASSEMBLING");
		CI = SourceReader.getInstance()
				.processFile(SourceReader.getInstance().readFile("res/functionality/ASSEMBLING"), restricted);

		boolean firstPassDone = CI.addToLineList();
		lineList = CI.getLinesList();
		if(firstPassDone)
			prepareListFile();
		noErrors = CI.checkForErrors();
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
			if(line.getMnemonic().equalsIgnoreCase("END")) {
				endOfProgram = "00" + line.getLocation();
				break;
			}
		}
		return endOfProgram;
	}

	public String getSizeOfProgram(String startOfProgram, String endOfProgram) {

		return "00" + Utility.convertToHexa(Utility.hexToDecimal(endOfProgram) - Utility.hexToDecimal(startOfProgram) + 1);
	}

	public String getProgramName() {

		String name = "";
		for(Line line : lineList) {
			if(line.getMnemonic().equalsIgnoreCase("START")) {
				name = line.getLabel();
				break;
			}
		}
		int length = name.length();
		if(length > 6)
			name = name.substring(0, 6);
		if(length < 6)
			name += Utility.getSpaces(6 - length);
		return name;
	}

	public String getAddresOfFirstExcutableInstruction() {

		String label, address;
		label = address = null;
		for(Line line : lineList) {
			if(line.getMnemonic().equalsIgnoreCase("END")) {
				label = line.getFirstOperand();
				break;
			}
		}
		for(Line line : lineList) {
			if(line.getLabel().equalsIgnoreCase(label)) {
				address = line.getLocation();
			}
		}
		return "00" + address;
	}

	public String addToTextRecord(String text) {

		String textRecord = "";
		// TODO: receive text, add it to the text record then format the string
		return textRecord;
	}

	public void passTwo() {

		instructionTable = InstructionTable.instructionTable;
		String startOfProgram = getStartOfProgram();
		String endOfProgram = getEndOfProgram();
		String sizeOfProgram = getSizeOfProgram(startOfProgram, endOfProgram);
		String programName = getProgramName();
		String headerRecord = "H^" + programName + "^" + startOfProgram + "^" + sizeOfProgram;

		String textRecord = "";
		@SuppressWarnings("unused")
		int n, i, x, b, p, e;
		String flagsByte = "";
		String textRecordTemp;
		String addressingMode;
		String firstOperand;
		String secondOperand;
		String mnemonic;
		Instruction currentInstruction;
		for(Line line : lineList) {
			addressingMode = line.getAddressingMode();
			mnemonic = line.getMnemonic();
			currentInstruction = InstructionTable.instructionTable.get(mnemonic);
			if(currentInstruction != null) {
				textRecordTemp = String.format("%1$02X", currentInstruction.getOpcode());
				switch(currentInstruction.getFormat()) {
				case ONE:
					addToTextRecord(textRecordTemp);
					break;
				case TWO:
					firstOperand = Integer.toString(RegisterTable.registerTable.get(line.getFirstOperand()));
					if(currentInstruction.hasSecondOperand())
						secondOperand = Integer.toString(RegisterTable.registerTable.get(line.getSecondOperand()));
					else
						secondOperand = "0";
					addToTextRecord(textRecordTemp + firstOperand + secondOperand);
					break;
				case THREE:
					switch(addressingMode) {
					// set n, i and x flags
					case "#":
						// Not yet supported
						break;
					case "@":
						// Not yet supported
						break;
					default:
						// TODO: set flags byte
						break;						
					}
					// TODO: firstOperand = displacement and set the b, p and e flags, e = 0 (Format 3)
					firstOperand = "";
					addToTextRecord(textRecordTemp + flagsByte + firstOperand);
					break;
				case FOUR:
					// almost same as Format 3
					break;
				default:
					break;
				}
			} else {
				// Directive
				switch(mnemonic) {
				case "WORD":
					addToTextRecord(String.format("%1$06X", Integer.parseInt(line.getFirstOperand())));
					break;
				case "BYTE":
					addToTextRecord(String.format("%1$02X", Integer.parseInt(line.getFirstOperand())));
					break;
				default:
					break;
				}
			}
		}
		// TODO: get the final text record
		textRecord = "";

		String addressOfFirstExcutableInstruction = getAddresOfFirstExcutableInstruction();
		String endRecord = "E^" + addressOfFirstExcutableInstruction;
		
		@SuppressWarnings("unused")
		String objectCode = headerRecord + textRecord + endRecord;
		// Utility.writeFile(objectCode, "/res/LIST/objFile.o");
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