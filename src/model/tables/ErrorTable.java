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
	public static final int LABEL_CANT_START_WITH_DIGIT = 13;
	public static final int WRONG_OPERAND_TYPE = 14;
	public static final int WRONG_ADDRESSING_MODE = 15;
	public static final int MISSING_FIRST_OPERAND = 16;
	public static final int MISSING_SECOND_OPERAND = 17;

	public static final int NO_ERROR = 90;


	public static String[] errorList = new String[100];

	public static void loadErrorList() {
		errorList[MISPLACED_LABEL] = "ERROR: Misplaced label";
		errorList[MISSING_MISPLACED_OPERATION_MNEMONIC] = "ERROR: Missing or misplaced operation mnemonic + \n";
		errorList[MISSING_MISPLACED_OPERAND_FIELD] = "ERROR: Missing or misplaced operand field + \n"; //DONE
		errorList[DUPLICATE_LABEL_DEFINITION] = "ERROR: Duplicate label definition \n"; //DONE
		errorList[STATEMENT_CANT_HAVE_LABEL] = "ERROR: This statement can’t have a label \n"; //DONE
		errorList[STATEMENT_CANT_HAVE_OPERAND] = "ERROR: This statement can’t have an operand \n"; //DONE
		errorList[WRONG_OPERATION_PREFIX] = "ERROR: Wrong operation prefix \n"; //DONE
		errorList[UNRECOGNIZED_OPERATION_CODE] = "ERROR: Unrecognized operation code \n"; //DONE
		errorList[UNDEFINED_SYMBOL_IN_OPERAND] = "ERROR: Undefined symbol in operand \n";
		errorList[NOT_HEXADECIMAL_STRING] = "ERROR: Not a hexadecimal string \n"; //DONE
		errorList[CANT_BE_FORTMAT4_INSTRUCTION] = "ERROR: Can’t be format 4 instruction \n"; //DONE
		errorList[ILLEGAL_ADDRESS_FOR_REGISTER] = "ERROR: Illegal address for a register \n"; //DONE
		errorList[MISSING_END_STATEMENT] = "ERROR: Missing END statement \n";
		errorList[LABEL_CANT_START_WITH_DIGIT] = "ERROR: Label can't start with a digit \n"; //DONE
		errorList[WRONG_OPERAND_TYPE] = "ERROR: Wrong operand type \n"; //DONE
		errorList[WRONG_ADDRESSING_MODE] = "ERROR: Wrong addressing Mode \n"; //DONE
		errorList[MISSING_FIRST_OPERAND] = "ERROR: Missing first operand \n"; //DONE
		errorList[MISSING_SECOND_OPERAND] = "ERROR: Missing second operand \n"; //DONE
		errorList[NO_ERROR] = "";
	}

}
