package controller;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import model.*;
import model.enums.Format;
import model.tables.*;
import model.utility.Utility;

public class Controller {

	private CommandInfo CI;

	private ArrayList<Line> lineList;
	private ArrayList<Integer> recordLengths = new ArrayList<>();

	@SuppressWarnings("unused")
	HashMap<String, Instruction> instructionTable;

	private String path;
	private String base;
	private String displacement;
	private String BASE_ERROR = "Base Error";
	private boolean noErrorsInPassOne = false;

	public boolean isNoErrors() {
		return noErrorsInPassOne;
	}

	@SuppressWarnings("unused")
	public void setNoErrors(boolean noErrors) {
		this.noErrorsInPassOne = noErrors;
	}

	private void loadInstructionTable() {

		InstructionTable.loadInstructionTable();
	}

	private void loadDirectiveTable() {

		DirectiveTable.loadDirectiveTable();
	}

	private void loadErrorList() {

		ErrorTable.loadErrorList();
	}

	private void loadRegisterTable() {

		RegisterTable.loadRegisterTable();
	}

	public void prepareData() {

		loadInstructionTable();
		loadDirectiveTable();
		loadErrorList();
		loadRegisterTable();
	}

	private void prepareListFile() {

		final String lineSeparator = "-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-";
		final String startPassOne = "\n-_-_-_-_-_-_-_-_-_- S   T   A   R   T      O   F      P   A   S   S   1 -_-_-_-_-_-_-_-_-_-_-";
		final String TABLE_FORM = "LINES" + Utility.getSpaces(7) + "ADDRESS" + Utility.getSpaces(5) + "LABEL"
				+ Utility.getSpaces(7) + "MNEMONIC" + Utility.getSpaces(4) + "ADDR_MODE" + Utility.getSpaces(3)
				+ "OPERAND1" + Utility.getSpaces(4) + "OPERAND2" + Utility.getSpaces(4) + "COMMENTS\n";
		String toBePrintedInListFile = lineSeparator;
		toBePrintedInListFile += startPassOne + "\n\n";
		toBePrintedInListFile += TABLE_FORM;

		int len = CI.getLinesList().size();
		for (int i = 0; i < len; i++) {
			String lineCount = String.valueOf(i);
			// noinspection StringConcatenationInLoop
			toBePrintedInListFile += lineCount + Utility.getSpaces(12 - lineCount.length())
			+ CI.getLinesList().get(i).toString() + "\n";
		}
		// textArea.setText(toBePrintedInTextArea);

		Utility.writeFile(toBePrintedInListFile, "res/LIST/listFile.txt");
		Utility.writeFile(SymbolTable.getString(), "res/LIST/symTable.txt");
	}

	private void fillSymbolTable() {
		Symbol symbol;
		for (Line line : lineList) {
			if (!line.getLabel().equals("") && !line.getLabel().equals("(~)")) {
				symbol = new Symbol(line.getLabel(), line.getLocation());
				SymbolTable.symbolTable.put(symbol.getSymbol(), symbol);
			}
		}
	}

	private void fillLiteralsTable() {
		Literal literal;
		int startingAddress = ProgramCounter.getInstance().getProgramCounter();
		for (Line line : lineList) {
			if (!line.getFirstOperand().equals("")) {
				if (line.getMnemonic().equalsIgnoreCase("LTORG")) {
					if (Utility.isNumeric(line.getFirstOperand())) {
						startingAddress = (int) Long.parseLong(line.getFirstOperand());
						continue;
					}
				}
			}
			if (!line.getFirstOperand().equals("")) {
				if (line.getFirstOperand().charAt(0) == '=') {
					literal = new Literal(line.getFirstOperand(), Utility.convertToHexa(startingAddress));
					startingAddress += literal.calculateLength();
					LiteralTable.literalTable.put(literal.getOperand(), literal);
				}
			}
		}
	}

	private void processArithmeticExpressions() {
		for (Line line : lineList) {
			if (!line.getMnemonic().equals("NOP")) {
				Format format;
				if (Utility.isInstruction(line.getMnemonic())) {
					format = InstructionTable.instructionTable.get(line.getMnemonic()).getFormat();
				} else {	// Directive
					format = DirectiveTable.directiveTable.get(line.getMnemonic()).getFormat();
				}
				// Only if formats 3 & 4
				if (format == Format.THREE || format == Format.FOUR
					|| line.getMnemonic().equals("ORG") || line.getMnemonic().equals("EQU") || line.getMnemonic().equals("LTORG")	) {
					// ONLY if addressing mode is direct with/without indexing
					if (!line.getAddressingMode().equals("#") && !line.getAddressingMode().equals("@")) {
						ArrayList<String> expressionList = Utility.splitExpression(line.getFirstOperand());
						// If operand is not an expression, size after splitting will be 1
						if (expressionList.size() == 1)
							continue;
						// Verify labels in the expression
						if (Utility.verifyExpression(expressionList)) {
							// Replace labels by the numeric value of their addresses
							Utility.evaluateLabels(expressionList);
							// Check syntax of arithmetic expression
							if (Utility.validateNumericExpression(expressionList)) {
								// Evaluate the expression
								String expression = Utility.getNumericExpression(expressionList);
								String operand = Utility.evaluateExpression(expression);
								System.out.println("Done evaluating! " + line.getFirstOperand() + " = " + operand + "\t\t\t" + expression);
								line.setFirstOperand(operand);
							} else {
								// Wrong Arithmetic expression format
								line.setError(ErrorTable.errorList[ErrorTable.WRONG_OPERAND_TYPE]);
							}
						} else {
							line.setError(ErrorTable.errorList[ErrorTable.WRONG_OPERAND_TYPE]);
						}
					}
				}
			}
		}
	}

	private void passOne(String program, boolean restricted) {

		Utility.writeFile(program, "res/functionality/ASSEMBLING");
		CI = SourceReader.getInstance().processFile(SourceReader.getInstance().readFile("res/functionality/ASSEMBLING"),
				restricted);

		boolean firstPassDone = CI.addToLineList();
		lineList = CI.getLinesList();
		if (firstPassDone) {
			prepareListFile();
			fillSymbolTable();
			processArithmeticExpressions();
			fillLiteralsTable();
		}
		noErrorsInPassOne = CI.checkForErrors();
	}

	private String getStartOfProgram() {

		String startOfProgram = "000000";
		for (Line line : lineList) {
			if (line.getMnemonic().equalsIgnoreCase("START")) {
				startOfProgram = "00" + line.getLocation();
				break;
			}
		}
		return startOfProgram;
	}

	private String getEndOfProgram() {

		String endOfProgram = null;
		for (Line line : lineList) {
			if (line.getMnemonic().equalsIgnoreCase("END")) {
				endOfProgram = "00" + line.getLocation();
				break;
			}
		}
		return endOfProgram;
	}

	private String getSizeOfProgram(String startOfProgram, String endOfProgram) {

		return "00"
				+ Utility.convertToHexa(Utility.hexToDecimal(endOfProgram) - Utility.hexToDecimal(startOfProgram) - 1);
	}

	private String getProgramName() {

		String name = "";
		for (Line line : lineList) {
			if (line.getMnemonic().equalsIgnoreCase("START")) {
				name = line.getLabel();
				break;
			}
		}
		int length = name.length();
		if (length > 6)
			name = name.substring(0, 6);
		if (length < 6)
			name += Utility.getSpaces(6 - length);
		return name;
	}

	private String getHeaderRecord() {

		String startOfProgram = getStartOfProgram();
		String endOfProgram = getEndOfProgram();
		String sizeOfProgram = getSizeOfProgram(startOfProgram, endOfProgram);
		String programName = getProgramName();
		return "H^" + programName + "^" + startOfProgram + "^" + sizeOfProgram; // Return header Record
	}

	private String addToTextRecord(String opcode, String flags, String disp, Format format) {

		String record;
		record = Utility.hexToBin(opcode).substring(12, 18); // take 6 bits only
		record += Utility.hexToBin(flags).substring(14);
		record += format == Format.THREE ? Utility.hexToBin(disp).substring(8) : Utility.hexToBin(disp);
		record = Utility.binToHex(record, format);
		return record;
	}

	private String getNIX(Line line) {

		String nix;
		switch (line.getAddressingMode()) {
		// set n, i and x flags
		case "#":
			// case immediate
			// nix = 010
			nix = "010";
			break;
		case "@":
			// case indirect
			// nix = 100
			nix = "100";
			break;
		default:
			// case direct
			// nix = indexing? 111 : 110;
			if (!line.getSecondOperand().equals("")) {
				// indexed
				nix = "111";
			} else {
				// non indexed
				nix = "110";
			}
			break;
		}
		return nix;
	}

	private String getBPE(Line line, Format format) {

		// TODO: set bpe
		// TODO: firstOperand = displacement and set the b, p and e flags, e = 0 for
		// Format 3 and e = 1 for Format 4
		String bpe, bp, e;
		String firstOperand = line.getFirstOperand().toUpperCase();
		int step = format == Format.THREE ? 3 : 4;
		int pc = Utility.hexToDecimal(line.getLocation()) + step;
		int loc, disp;
		Symbol symbol = SymbolTable.symbolTable.get(firstOperand);
		Literal literal = LiteralTable.literalTable.get(firstOperand);
		if (symbol != null || literal != null) {
			loc = literal == null ? Utility.hexToDecimal(symbol.getAddress()) : Utility.hexToDecimal(literal.getAddress()) ;
			disp = loc - pc;
			if (disp >= -2048 && disp < 2048) {
				// bpe = 010
				bp = "01";
			} else { // try base relative
				if (checkBase()) { // check if base register is available
					int base = getBase();
					disp = loc - base;
					if (disp >= 0 && disp <= 4 * 1024 - 1) {
						// bpe = 100
						bp = "10";
					} else {
						// error
						return BASE_ERROR;
					}
				} else {
					// error
					return BASE_ERROR;
				}
			}
		} else {
			disp = Utility.hexToDecimal(firstOperand);
			bp = "00";
		}
		/*
		 * TODO!!! the literals caused an exception in calculating disp test example
		 * operand: W'123' when the following was added the error was gone and
		 * successful assembly 
		 * if (firstOperand.charAt(0) == '=')
		 *  disp = Utility.hexToDecimal(firstOperand.substring(3, firstOperand.length()-2));
		 * else
		 *  disp = Utility.hexToDecimal(firstOperand);
		 */
		displacement = format == Format.THREE ? String.format("%1$04X", disp) : String.format("%1$05X", disp);
		e = format == Format.THREE ? "0" : "1";
		bpe = bp + e;
		return bpe;
	}

	private void setBase(String base) {

		this.base = base;
	}

	private int getBase() {

		return Utility.hexToDecimal(base);
	}

	private boolean checkBase() {

		for (Line line : lineList) {
			if (line.getMnemonic().equalsIgnoreCase("BASE")) {
				setBase(line.getFirstOperand());
				return true;
			} else if (line.getMnemonic().equalsIgnoreCase("NOBASE"))
				return false;
		}
		return false;
	}

	private String extractOperand(String operand) {

		return operand.substring(2).replace("'", "");
	}

	private String convertToAscii(String data) {

		String res = "";
		char[] charArray = data.toCharArray();
		for (char c : charArray) {
			// noinspection StringConcatenationInLoop
			res += (int) c;
		}
		return res;
	}

	private String formatTextRecord(String textRecord) {

		ArrayList<String> lengths = new ArrayList<>();
		String start = getStartOfProgram();
		String result = "T^" + start + "^^";
		char[] temp = textRecord.toCharArray();
		int sum = 0;
		int index = 0;
		int tempSize = 0;
		for (int n : recordLengths) {
			sum += n;
			if (sum <= 30) {
				int i;
				for (i = index; i < index + n * 2; i++) {
					// noinspection StringConcatenationInLoop
					result += temp[i];
				}
				tempSize = sum;
				index = i;
			} else {
				// noinspection StringConcatenationInLoop
				result += "\nT^";
				start = String.format("%1$06X", Utility.hexToDecimal(start) + sum - n);
				lengths.add(String.format("%1$02X", sum - n));
				result += start;
				result += "^^";
				tempSize = sum - n;
				sum = 0;
			}
		}
		lengths.add(String.format("%1$02X", tempSize));
		int size = result.length();
		index = 0;
		for (int i = 0; i < size; i++) {
			if (result.charAt(i) == 'T') {
				result = result.substring(0, i + 9) + lengths.get(index++) + result.substring(i + 9);
			}
		}
		return result;
	}

	private String getTextRecord() {

		String textRecord = "";
		String nix, bpe;
		String flagsByte;
		String textRecordTemp;
		String firstOperand;
		String secondOperand;
		String mnemonic;
		Instruction currentInstruction;
		for (Line line : lineList) {
			mnemonic = line.getMnemonic();
			currentInstruction = InstructionTable.instructionTable.get(mnemonic);
			if (currentInstruction != null) {
				textRecordTemp = String.format("%1$02X", currentInstruction.getOpcode());
				switch (currentInstruction.getFormat()) {
				case ONE:
					// noinspection StringConcatenationInLoop
					textRecord += textRecordTemp;
					recordLengths.add(1);
					break;
				case TWO:
					firstOperand = Integer.toString(RegisterTable.registerTable.get(line.getFirstOperand()));
					if (currentInstruction.hasSecondOperand())
						secondOperand = Integer.toString(RegisterTable.registerTable.get(line.getSecondOperand()));
					else
						secondOperand = "0";
					// noinspection StringConcatenationInLoop
					textRecord += textRecordTemp + firstOperand + secondOperand;
					recordLengths.add(2);
					break;
				case THREE:
					nix = getNIX(line);
					bpe = getBPE(line, Format.THREE);
					if (bpe.equals(BASE_ERROR)) {
						return BASE_ERROR;
					}
					flagsByte = Utility.binToHex(nix + bpe);
					// noinspection StringConcatenationInLoop
					textRecord += addToTextRecord(textRecordTemp, flagsByte, displacement, Format.THREE);
					recordLengths.add(3);
					break;
				case FOUR:
					// noinspection UnusedAssignment
					firstOperand = line.getFirstOperand();
					nix = getNIX(line);
					bpe = getBPE(line, Format.FOUR);
					if (bpe.equals(BASE_ERROR)) {
						return BASE_ERROR;
					}
					flagsByte = Utility.binToHex(nix + bpe);
					// noinspection StringConcatenationInLoop
					textRecord += addToTextRecord(textRecordTemp, flagsByte, displacement, Format.FOUR);
					recordLengths.add(4);
					break;
				default:
					break;
				}
			} else {
				// Directive
				String data = line.getFirstOperand().toUpperCase();
				String[] operands = data.split(",");
				char type;
				switch (mnemonic) {
				case "WORD":
					for (String operand : operands) {
						type = operand.charAt(0);
						switch (type) {
						case 'X':
							// textRecord += Utility.getZeros(6 - operand.length()) +
							// extractOperand(operand);
							break;
						case 'C':
							// textRecord += convertToAscii(extractOperand(operand));
							break;
						default:
							// noinspection StringConcatenationInLoop
							textRecord += String.format("%1$06X", Integer.parseInt(operand));
							recordLengths.add(3);
							break;
						}
					}
					break;
				case "BYTE":
					for (String operand : operands) {
						type = operand.charAt(0);
						switch (type) {
						case 'X':
							operand = extractOperand(operand);
							textRecordTemp = Utility.getZeros(
									(int) Math.ceil((double) (operand.length()) / 2) * 2 - operand.length()) + operand;
							// noinspection StringConcatenationInLoop
							textRecord += textRecordTemp;
							recordLengths.add(textRecordTemp.length() / 2);
							break;
						case 'C':
							operand = extractOperand(operand);
							// noinspection StringConcatenationInLoop
							textRecord += convertToAscii(operand);
							recordLengths.add(operand.length());
							break;
						default:
							// noinspection StringConcatenationInLoop
							textRecord += String.format("%1$02X", Integer.parseInt(operand));
							recordLengths.add(1);
							break;
						}
					}
					break;
				default:
					break;
				}
			}
		}
		return formatTextRecord(textRecord);
	}

	private String getAddressOfFirstExecutableInstruction() {

		String label, address;
		label = address = null;
		for (Line line : lineList) {
			if (line.getMnemonic().equalsIgnoreCase("END")) {
				label = line.getFirstOperand();
				break;
			}
		}
		for (Line line : lineList) {
			if (line.getLabel().equalsIgnoreCase(label)) {
				address = line.getLocation();
			}
		}
		return "00" + address;
	}

	private String getEndRecord() {

		String addressOfFirstExecutableInstruction = getAddressOfFirstExecutableInstruction();
		return "E^" + addressOfFirstExecutableInstruction; // Return endRecord
	}

	private String getObjectCode() {

		String headerRecord = getHeaderRecord();
		String textRecord = getTextRecord();
		if (textRecord.equals(BASE_ERROR))
			return BASE_ERROR;
		String endRecord = getEndRecord();
		return headerRecord + "\n" + textRecord + "\n" + endRecord; // Return objectCode
	}

	private boolean passTwo() {

		String objectCode = getObjectCode();
		if (objectCode.equals(BASE_ERROR))
			return false;
		Utility.writeFile(objectCode, "res/LIST/objFile.o");
		return true;
	}

	public void assemble(String program, boolean restricted) {

		passOne(program, restricted);
		if(noErrorsInPassOne)
			passTwo();
		Utility.clearAll();
	}

	public String getListFile() {

		path = Paths.get(".").toAbsolutePath().normalize().toString() + "/res/LIST/listFile.txt";
		ArrayList<String> arr = SourceReader.getInstance().readFile(path);
		String append = "";
		for (String s : arr) {
			// noinspection StringConcatenationInLoop
			append += s + "\n";
		}
		return append;
	}

	public String loadFile(String path) {

		this.path = path;
		ArrayList<String> arr = SourceReader.getInstance().readFile(path);
		String append = "";
		for (String s : arr) {
			// noinspection StringConcatenationInLoop
			append += s + "\n";
		}
		return append;
	}

}