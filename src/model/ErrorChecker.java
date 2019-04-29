package model;

import model.tables.ErrorTable;

public class ErrorChecker {
	private static ErrorChecker instance = null;
	private String error;

	public static ErrorChecker getInstance() {
		if (instance == null)
			instance = new ErrorChecker();
		return instance;
	}

	public int verifyLine(String instruction, Line line) {
		verifyIfMisplaced(instruction, line);
		verifyLabel(line);
		verifyMnemonic(line);
		verifyOperands(line);
		verifyEndStatement(line);
		setLineError(line);
		return 0;
	}

	private void verifyIfMisplaced(String instruction, Line line) {
		String label = instruction.substring(0, 9);
		String mnemonic = instruction.substring(10, 16);
		/*
		 * String operand = instruction.substring(18, 36); String comment =
		 * instruction.substring(36,67);
		 */
		if (checkIfMisplaced(label, line.getLabel()))
			error = ErrorTable.errorList[ErrorTable.MISPLACED_LABEL];
		if (checkIfMisplaced(mnemonic, line.getMnemonic()))
			error = ErrorTable.errorList[ErrorTable.MISSING_MISPLACED_OPERATION_MNEMONIC];
		error = ErrorTable.errorList[ErrorTable.NO_ERROR];

		/*
		 * TODO: MISSING_MISPLACED_OPERAND_FIELD
		 */
	}

	private void verifyLabel(Line line) {
		/*
		 * DUPLICATE_LABEL_DEFINITION 
		 * LABEL_STARTING_WITH_DIGIT
		 */
	}

	private void verifyMnemonic(Line line) {
		/*
		 * WRONG_OPERATION_PREFIX
		 * UNRECOGNIZED_OPERATION_CODE
		 * CANT_BE_FORTMAT4_INSTRUCTION
		 * STATEMENT_CANT_HAVE_LABEL
		 * STATEMENT_CANT_HAVE_OPERAND
		 */
	}

	private void verifyOperands(Line line) {
		/*
		 * UNDEFINED_SYMBOL_IN_OPERAND 
		 * NOT_HEXADECIMAL_STRING
		 * ILLEGAL_ADDRESS_FOR_REGISTER 
		 * WRONG_OPERAND
		 */

	}

	private void verifyEndStatement(Line line) {
		/*
		 * MISSING_END_STATEMENT
		 */
	}
	
	private void setLineError(Line line) {
		line.setError(error);
	}

	private boolean checkIfMisplaced(String input, String correctVal) {
		return !input.equals(correctVal);
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
