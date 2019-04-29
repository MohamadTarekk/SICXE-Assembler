package model;

import javax.swing.plaf.synth.SynthSpinnerUI;

import model.enums.OperandType;
import model.tables.DirectiveTable;
import model.tables.ErrorTable;
import model.tables.InstructionTable;
import model.utility.Utility;

public class ErrorChecker {
	private static ErrorChecker instance = null;
	private String error;

	public static ErrorChecker getInstance() {
		if (instance == null)
			instance = new ErrorChecker();
		return instance;
	}

	public void verifyLine(String instruction, Line line) {
		//verifyIfMisplaced(instruction, line);
		verifyLabel(line);
		verifyMnemonic(line);
		verifyAddressingMode(line);
		verifyOperands(line);
		verifyEndStatement(line);
		setLineError(line);
	}

	private void verifyIfMisplaced(String instruction, Line line) {
		String label = instruction.substring(0, 9);
		String mnemonic = instruction.substring(10, 16);
		/*
		 * String operand = instruction.substring(18, 36); 
		 * String comment = instruction.substring(36,67);
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
		String mnemonic = line.getMnemonic();
		String label = line.getLabel();
		// UNRECOGNIZED_OPERATION_CODE
		if(!Utility.isInstruction(mnemonic) && !Utility.isDirective(mnemonic)) {
			error = ErrorTable.errorList[ErrorTable.UNRECOGNIZED_OPERATION_CODE];
			return;
		}
		// WRONG_OPERATION_PREFIX - CANT_BE_FORTMAT4_INSTRUCTION
		if(Utility.isInstruction(mnemonic)) {
			switch(InstructionTable.instructionTable.get(mnemonic).getFormat()) {
			case FOUR:
				if(!mnemonic.startsWith("+")) {
					error = ErrorTable.errorList[ErrorTable.WRONG_OPERATION_PREFIX];
					return;
				}
			default:
				if(mnemonic.startsWith("+")) {
					error = ErrorTable.errorList[ErrorTable.CANT_BE_FORTMAT4_INSTRUCTION];
					return;
				}
			}
		}
		//STATEMENT_CANT_HAVE_LABEL
		if(Utility.isDirective(mnemonic)) {
			switch(DirectiveTable.directiveTable.get(mnemonic).getDirective()) {
			case "END":
			case "ORG":
			case "BASE":
			case "NOBASE":
			case "LTORG":
				if(!label.equals("")) {
					error = ErrorTable.errorList[ErrorTable.STATEMENT_CANT_HAVE_LABEL];
					return;
				}
			}
		}
		error = ErrorTable.errorList[ErrorTable.NO_ERROR];
	}

	private void verifyOperands(Line line) {
		String mnemonic = line.getMnemonic();
		/*
		 * MISSING_MISPLACED_OPERAND_FIELD
		 * UNDEFINED_SYMBOL_IN_OPERAND 
		 * NOT_HEXADECIMAL_STRING
		 * ILLEGAL_ADDRESS_FOR_REGISTER 
		 * WRONG_OPERAND_TYPE
		 * STATEMENT_CANT_HAVE_OPERAND
		 */
		if(Utility.isDirective(mnemonic)) {
			verifyDirectiveOperands(line);
		}else {
			verifyInstructionOperands(line);
		}
	}
	
	private void verifyInstructionOperands(Line line) {
		String mnemonic = line.getMnemonic();
		if(mnemonic.equals("NOP"))
			return;
			if(InstructionTable.instructionTable.get(mnemonic).hasFirstOperand()) {
			if(line.getFirstOperand().equals("")) {
				error = ErrorTable.errorList[ErrorTable.MISSING_FIRST_OPERAND];
				return;
			}
			if(InstructionTable.instructionTable.get(mnemonic).getFirstOperand() == OperandType.REGISTER) {
				if(!Utility.isRegister(line.getFirstOperand())) {
					error = ErrorTable.errorList[ErrorTable.ILLEGAL_ADDRESS_FOR_REGISTER];
					return;
				}
			}else if(InstructionTable.instructionTable.get(mnemonic).getFirstOperand() == OperandType.VALUE) {
				if(!Utility.isLabel(line.getFirstOperand())) {
					error = ErrorTable.errorList[ErrorTable.WRONG_OPERAND_TYPE];
					return;
				}
			}
		}else {
			if(!line.getFirstOperand().equals("")) {
				error = ErrorTable.errorList[ErrorTable.STATEMENT_CANT_HAVE_OPERAND];
				return;
			}
		}
		
		if(InstructionTable.instructionTable.get(mnemonic).hasSecondOperand()) {
			if(line.getSecondOperand().equals("")) {
				error = ErrorTable.errorList[ErrorTable.MISSING_SECOND_OPERAND];
				return;
			}
			if(InstructionTable.instructionTable.get(mnemonic).getSecondOperand() == OperandType.REGISTER) {
				if(!Utility.isRegister(line.getSecondOperand())) {
					error = ErrorTable.errorList[ErrorTable.ILLEGAL_ADDRESS_FOR_REGISTER];
					return;
				}
			}else if(InstructionTable.instructionTable.get(mnemonic).getSecondOperand() == OperandType.VALUE) {
				if(!Utility.isLabel(line.getSecondOperand())) {
					error = ErrorTable.errorList[ErrorTable.WRONG_OPERAND_TYPE];
					return;
				}
			}
		}else {
			if(!line.getSecondOperand().equals("")) {
				error = ErrorTable.errorList[ErrorTable.STATEMENT_CANT_HAVE_OPERAND];
				return;
			}
		}
		error = ErrorTable.errorList[ErrorTable.NO_ERROR];
	}
	
	private void verifyDirectiveOperands(Line line) {
		String mnemonic = line.getMnemonic();
		switch(DirectiveTable.directiveTable.get(mnemonic).getDirective()) {
		case "NOBASE":
		case "LTORG":
			if(!line.getFirstOperand().equals("")) {
				error = ErrorTable.errorList[ErrorTable.STATEMENT_CANT_HAVE_OPERAND];
				return;
			}
		case "BYTE":	
			if(line.getFirstOperand().equals("")) {
				error = ErrorTable.errorList[ErrorTable.MISSING_MISPLACED_OPERAND_FIELD];
				return;
			}
			if(!isHexa(line.getFirstOperand().substring(2,line.getFirstOperand().length() - 1))) {
				error = ErrorTable.errorList[ErrorTable.NOT_HEXADECIMAL_STRING];
				return;
			}
		default:
			if(line.getFirstOperand().equals("")) {
				error = ErrorTable.errorList[ErrorTable.MISSING_MISPLACED_OPERAND_FIELD];
				return;
			}
		}
		error = ErrorTable.errorList[ErrorTable.NO_ERROR];
	}
	
	private void verifyAddressingMode(Line line) {
		switch(line.getAddressingMode()) {
		case "":
		case "@":
		case "#":
			error = ErrorTable.errorList[ErrorTable.NO_ERROR];
			break;
		default:
			error = ErrorTable.errorList[ErrorTable.WRONG_ADDRESSING_MODE];
			break;
		}
	}

	private void verifyEndStatement(Line line) {
		/*
		 * MISSING_END_STATEMENT
		 */
	}
	
	
	private static boolean isHexa(String value) {
		try {
		    Long.parseLong(value, 16);
		    return true;
		}catch(NumberFormatException ex) {
		    return false;
		}
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
