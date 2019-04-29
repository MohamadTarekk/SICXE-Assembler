package model.tables;

public class ErrorTable {

	public static final int MISPLACED_LABEL = 0;
	public static final int MISSING_MISPLACED_OPERATION_MNEMONIC = 1;
	public static final int MISSING_MISPLACED_OPERAND_FIELD = 2;
	public static final int DUPLICATE_LABEL_DEFINITION = 3;
	public static final int STATEMENT_CANT_HAVE_LABEL = 4;
	public static final int STATEMENT_CANT_HAVE_OPERAND = 5;
	public static final int WRONG_OPERATION_PREFIX = 6;
	public static final int UNRECOGNIZED_OPERATION_CODE = 7;
	public static final int UNDEFINED_SYMBOL_IN_OPERAND = 8;
	public static final int NOT_HEXADECIMAL_STRING = 9;
	public static final int CANT_BE_FORTMAT4_INSTRUCTION = 10;
	public static final int ILLEGAL_ADDRESS_FOR_REGISTER = 11;
	public static final int MISSING_END_STATEMENT = 12;
	public static final int LABEL_STARTING_WITH_DIGIT = 13;
	public static final int LABELS_CANT_HAVE_SPACES = 14;
	public static final int NO_ERROR = 100;


	private static String[] errorList = new String[16];

	public void loadErrorList() {
		errorList[MISPLACED_LABEL] = "ERROR: Misplaced label";
		errorList[MISSING_MISPLACED_OPERATION_MNEMONIC] = "ERROR: Missing or misplaced operation mnemonic";
		errorList[MISSING_MISPLACED_OPERAND_FIELD] = "ERROR: Missing or misplaced operand field";
		errorList[DUPLICATE_LABEL_DEFINITION] = "ERROR: Duplicate label definition";
		errorList[STATEMENT_CANT_HAVE_LABEL] = "ERROR: This statement can’t have a label";
		errorList[STATEMENT_CANT_HAVE_OPERAND] = "ERROR: This statement can’t have an operand";
		errorList[WRONG_OPERATION_PREFIX] = "ERROR: Wrong operation prefix";
		errorList[UNRECOGNIZED_OPERATION_CODE] = "ERROR: Unrecognized operation code";
		errorList[UNDEFINED_SYMBOL_IN_OPERAND] = "ERROR: Undefined symbol in operand";
		errorList[NOT_HEXADECIMAL_STRING] = "ERROR: Not a hexadecimal string";
		errorList[CANT_BE_FORTMAT4_INSTRUCTION] = "ERROR: Can’t be format 4 instruction";
		errorList[ILLEGAL_ADDRESS_FOR_REGISTER] = "ERROR: Illegal address for a register";
		errorList[MISSING_END_STATEMENT] = "ERROR: Missing END statement";
		errorList[LABEL_STARTING_WITH_DIGIT] = "ERROR: Labels can't start with a digit";
	}

}
