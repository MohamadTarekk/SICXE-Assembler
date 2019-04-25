package model;

import java.util.HashMap;

public class ErrorChecker {

	public final int NO_ERROR = 0;
	public final int MISPLACED_LABEL = 1;
	public final int MISSING_MISPLACED_OPERATION_MNEMONIC = 2;
	public final int MISSING_MISPLACED_OPERAND_FIELD = 3;
	public final int DUPLICATE_LABEL_DEFINITION = 4;
	public final int STATEMENT_CANT_HAVE_LABEL = 5;
	public final int STATEMENT_CANT_HAVE_OPERAND = 6;
	public final int WRONG_OPERATION_PREFIX = 7;
	public final int UNRECOGNIZED_OPERATION_CODE = 8;
	public final int UNDEFINED_SYMBOL_IN_OPERAND = 9;
	public final int NOT_HEXADECIMAL_STRING = 10;
	public final int CANT_BE_FORTMAT4_INSTRUCTION = 11;
	public final int ILLEGAL_ADDRESS_FOR_REGISTER = 12;
	public final int MISSING_END_STATEMENT = 13;

	private static ErrorChecker instance = null;

	private HashMap<String, Integer> commandsMap;
	private HashMap<String, Boolean> labelTable;

	public static ErrorChecker getInstance() {
		if (instance == null)
			instance = new ErrorChecker();
		return instance;
	}

	private ErrorChecker() {
		commandsMap = SourceReader.getInstance().getInstructionOpCodeTable("res/SIC-XE Instructions Opcode.txt");
	};

	public int verifyInstructionsRestricted(SourceReader.CommandInfo commandInfo) {
		labelTable = new HashMap<>();
		int len = commandInfo.getWholeInstruction().size();
		for (int i = 0; i < len; i++) {
			int errVal = checkInstructionRestricted(commandInfo, i);
			if (errVal != 0)
				return errVal;
		}
		return NO_ERROR;
	}

	private int checkInstructionRestricted(SourceReader.CommandInfo ci, int lineNum) {
		String label = ci.getWholeInstruction().get(lineNum).substring(0, 9);
		String command = ci.getWholeInstruction().get(lineNum).substring(10, 16);
		String operand = ci.getWholeInstruction().get(lineNum).substring(18, 36);

		if (checkIfMisplaced(label, ci.getLabels().get(lineNum)))
			return MISPLACED_LABEL;
		if (checkIfMisplaced(command, ci.getCommands().get(lineNum)))
			return MISSING_MISPLACED_OPERATION_MNEMONIC;
		String op1op2 = ci.getAddressMode().get(lineNum) + ci.getOperand1().get(lineNum) + ',' + ci.getOperand2().get(lineNum);
		if (checkIfMisplaced(operand, op1op2))
			return MISSING_MISPLACED_OPERAND_FIELD;
		if (labelTable.get(label) != null)
			return DUPLICATE_LABEL_DEFINITION;
		// 9spaces
		if (!label.equals("         ") && !checkCanHaveLabel(label))
			return STATEMENT_CANT_HAVE_LABEL;
		// 18 spaces
		if (!operand.equals("                  ") && !checkCanHaveOperand(operand))
			return STATEMENT_CANT_HAVE_OPERAND;
		if (checkIfWrongOperationPrefix(command))
			return WRONG_OPERATION_PREFIX;
		if (commandsMap.get(command) == null)
			return UNRECOGNIZED_OPERATION_CODE;
		if (checkIfUndefinedSymbolInOperand(operand))
			return UNDEFINED_SYMBOL_IN_OPERAND;
		if (ci.getOperand2().get(lineNum).charAt(0)=='X' && !checkIfHexadecimalString(ci.getOperand2().get(lineNum)))
			return NOT_HEXADECIMAL_STRING;
		// TODO ? SHOULD IT BE OPERAND ?
		if (checkIfIllegalAddressForRegister(operand))
			return ILLEGAL_ADDRESS_FOR_REGISTER;
		if (checkIfItsFormat4Instruction(command))
			return CANT_BE_FORTMAT4_INSTRUCTION;

		if (lineNum == ci.getWholeInstruction().size() && !command.equals("END"))
			return MISSING_END_STATEMENT;

		labelTable.put(label, true);
		return NO_ERROR;
	}

	private boolean checkIfMisplaced(String input, String correctVal) {
		return !input.equals(correctVal);
	}

	// TODO IMPLEMENT ANY FUNCTION THAT IS NOT IMPLEMENTED YET
	private boolean checkCanHaveLabel(String input) {
		return true;
	}

	private boolean checkCanHaveOperand(String input) {
		return true;
	}

	private boolean checkIfWrongOperationPrefix(String input) {
		return true;
	}

	private boolean checkIfUndefinedSymbolInOperand(String input) {
		return true;
	}

	private boolean checkIfHexadecimalString(String input) {
		try {
			Integer.parseInt(input, 16);
		} catch (Exception e) {
			// exception then not hexa string
			return false;
		}
		return true;
	}

	private boolean checkIfIllegalAddressForRegister(String input) {
		return true;
	}

	private boolean checkIfItsFormat4Instruction(String input) {
		return true;
	}
}
