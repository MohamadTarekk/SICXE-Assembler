package model;

import java.util.ArrayList;

import model.enums.OperandType;
import model.tables.DirectiveTable;
import model.tables.ErrorTable;
import model.tables.InstructionTable;
import model.utility.Utility;

public class ErrorChecker {

    private static ErrorChecker instance = null;
    private String error;
    private ArrayList<String> labelList = new ArrayList<>();

    private ErrorChecker() {
        /* Private Constructor for Singleton */
    }

    public static ErrorChecker getInstance() {
        if (instance == null)
            instance = new ErrorChecker();
        return instance;
    }

    public void verifyLine(Line line) {
        if (verifyIfMisplaced(line)) {
            setLineError(line);
            return;
        }
        if (verifyLabel(line)) {
            setLineError(line);
            return;
        }
        if (verifyMnemonic(line)) {
            setLineError(line);
            return;
        }
        if (verifyAddressingMode(line)) {
            setLineError(line);
            return;
        }
        if (verifyOperands(line)) {
            setLineError(line);
            return;
        }
        // verifyEndStatement(line);
        setLineError(line);
    }

    private boolean verifyIfMisplaced(Line line) {
        String label = line.getLabel();
        if (label.startsWith(" ")) {
            error = ErrorTable.errorList[ErrorTable.MISPLACED_LABEL];
            return true;
        }
        if (Utility.containsMisplacedLetter(label)) {
            error = ErrorTable.errorList[ErrorTable.MISSING_MISPLACED_OPERATION_MNEMONIC];
            return true;
        }
        if (line.getMnemonic().startsWith(" ")) {
            error = ErrorTable.errorList[ErrorTable.MISSING_MISPLACED_OPERATION_MNEMONIC];
            return true;
        }
        if (Utility.containsMisplacedLetter(line.getMnemonic())) {
            error = ErrorTable.errorList[ErrorTable.MISSING_MISPLACED_OPERAND_FIELD];
            return true;
        }
        if (line.getFirstOperand().startsWith(" ")) {
            error = ErrorTable.errorList[ErrorTable.MISSING_MISPLACED_OPERAND_FIELD];
            return true;
        }
        return false;
    }

    private boolean verifyLabel(Line line) {
        /*
         * DUPLICATE_LABEL_DEFINITION LABEL_STARTING_WITH_DIGIT
         */
        String label = line.getLabel();
        if (!label.equals("") && !label.equals("(~)")) {
            if (labelList.contains(label)) {
                error = ErrorTable.errorList[ErrorTable.DUPLICATE_LABEL_DEFINITION];
                return true;
            } else if (Character.isDigit(label.charAt(0))) {
                error = ErrorTable.errorList[ErrorTable.LABEL_CANT_START_WITH_DIGIT];
                return true;
            } else {
                labelList.add(label);
                return false;
            }

        }

        return false;
    }

    private boolean verifyMnemonic(Line line) {
        String mnemonic = line.getMnemonic();
        String label = line.getLabel();
        if (mnemonic.equals("NOP"))
            return false;
        // UNRECOGNIZED_OPERATION_CODE
        if (!Utility.isInstruction(mnemonic) && !Utility.isDirective(mnemonic)) {
            error = ErrorTable.errorList[ErrorTable.UNRECOGNIZED_OPERATION_CODE];
            return true;
        }
        // WRONG_OPERATION_PREFIX - CANT_BE_FORMAT4_INSTRUCTION
        if (Utility.isInstruction(mnemonic)) {
            switch (InstructionTable.instructionTable.get(mnemonic).getFormat()) {
                case FOUR:
                    if (!mnemonic.startsWith("+")) {
                        error = ErrorTable.errorList[ErrorTable.WRONG_OPERATION_PREFIX];
                        return true;
                    }
                    break;
                default:
                    if (mnemonic.startsWith("+")) {
                        error = ErrorTable.errorList[ErrorTable.CANT_BE_FORTMAT4_INSTRUCTION];
                        return true;
                    }
            }
        }
        // STATEMENT_CANT_HAVE_LABEL
        if (Utility.isDirective(mnemonic)) {
            switch (DirectiveTable.directiveTable.get(mnemonic).getDirective()) {
                case "END":
                case "ORG":
                case "BASE":
                case "NOBASE":
                case "LTORG":
                    if (!label.equals("")) {
                        error = ErrorTable.errorList[ErrorTable.STATEMENT_CANT_HAVE_LABEL];
                        return true;
                    }
                    break;
                case "EQU":
                    if (label.equals("") || label.equals("(~)")) {
                        error = ErrorTable.errorList[ErrorTable.STATEMENT_MUST_HAVE_LABEL];
                        return true;
                    }
                    break;
            }
        }
        error = ErrorTable.errorList[ErrorTable.NO_ERROR];
        return false;
    }

    private boolean verifyOperands(Line line) {
        String mnemonic = line.getMnemonic();
        /*
         * MISSING_MISPLACED_OPERAND_FIELD UNDEFINED_SYMBOL_IN_OPERAND
         * NOT_HEXADECIMAL_STRING ILLEGAL_ADDRESS_FOR_REGISTER WRONG_OPERAND_TYPE
         * STATEMENT_CANT_HAVE_OPERAND
         */
        if (Utility.isDirective(mnemonic)) {
            return verifyDirectiveOperands(line);
        } else {
            return verifyInstructionOperands(line);
        }
    }

    private boolean verifyInstructionOperands(Line line) {
        String mnemonic = line.getMnemonic();
        if (mnemonic.equals("NOP"))
            return false;
        if (InstructionTable.instructionTable.get(mnemonic).hasFirstOperand()) {
            if (line.getFirstOperand().equals("")) {
                error = ErrorTable.errorList[ErrorTable.MISSING_FIRST_OPERAND];
                return true;
            }
            if (InstructionTable.instructionTable.get(mnemonic).getFirstOperand() == OperandType.REGISTER) {
                if (!Utility.isRegister(line.getFirstOperand())) {
                    error = ErrorTable.errorList[ErrorTable.ILLEGAL_ADDRESS_FOR_REGISTER];
                    return true;
                }
            } else if (InstructionTable.instructionTable.get(mnemonic).getFirstOperand() == OperandType.VALUE) {
                if (!Utility.isRegister(line.getFirstOperand()) && !Utility.isLabel(line.getFirstOperand())
                        && !isNumeric(line.getFirstOperand()) && !Utility.isLiteral(line.getFirstOperand())
                        && !Utility.isExpression(line.getFirstOperand())) {
                    error = ErrorTable.errorList[ErrorTable.WRONG_OPERAND_TYPE];
                    return true;
                }
                if (line.getAddressingMode().equals("#")) {
                    if (!isNumeric(line.getFirstOperand()) && !Utility.isLabel(line.getFirstOperand())) {
                        error = ErrorTable.errorList[ErrorTable.WRONG_OPERAND_TYPE];
                        return true;
                    }
                }
            }
        } else {
            if (!line.getFirstOperand().equals("")) {
                error = ErrorTable.errorList[ErrorTable.CANT_HAVE_FIRST_OPERAND];
                return true;
            }
        }

        if (InstructionTable.instructionTable.get(mnemonic).hasSecondOperand()) {
            if (line.getSecondOperand().equals("")) {
                error = ErrorTable.errorList[ErrorTable.MISSING_SECOND_OPERAND];
                return true;
            }
            if (InstructionTable.instructionTable.get(mnemonic).getSecondOperand() == OperandType.REGISTER) {
                if (!Utility.isRegister(line.getSecondOperand())) {
                    error = ErrorTable.errorList[ErrorTable.ILLEGAL_ADDRESS_FOR_REGISTER];
                    return true;
                }
            } else if (InstructionTable.instructionTable.get(mnemonic).getSecondOperand() == OperandType.VALUE) {
                if (!Utility.isRegister(line.getSecondOperand()) && !Utility.isLabel(line.getSecondOperand())) {
                    error = ErrorTable.errorList[ErrorTable.WRONG_OPERAND_TYPE];
                    return true;
                }
            }
        } else {
            if (!line.getSecondOperand().equals("") && !line.getSecondOperand().equalsIgnoreCase("X")) {
                error = ErrorTable.errorList[ErrorTable.CANT_HAVE_SECOND_OPERAND];
                return true;
            }
        }
        error = ErrorTable.errorList[ErrorTable.NO_ERROR];
        return false;
    }

    private boolean verifyDirectiveOperands(Line line) {
        String mnemonic = line.getMnemonic();
        switch (DirectiveTable.directiveTable.get(mnemonic).getDirective()) {
            case "NOBASE":
            case "LTORG":
                if (!line.getFirstOperand().equals("")) {
                    error = ErrorTable.errorList[ErrorTable.STATEMENT_CANT_HAVE_OPERAND];
                    return true;
                }
                break;
            case "EQU":
                if (!line.getAddressingMode().equals("")){
                    error = ErrorTable.errorList[ErrorTable.WRONG_OPERAND_TYPE];
                    return true;
                }
                /*if (line.getAddressingMode().equals("#")) {
                    if (!isNumeric(line.getFirstOperand()) && !Utility.isLabel(line.getFirstOperand())) {
                        error = ErrorTable.errorList[ErrorTable.WRONG_OPERAND_TYPE];
                        return true;
                    }
                }*/
                if (line.getFirstOperand().equals("")) {
                    error = ErrorTable.errorList[ErrorTable.MISSING_FIRST_OPERAND];
                    return true;
                }
                if ( !Utility.isLabel(line.getFirstOperand()) && !isNumeric(line.getFirstOperand()) &&
                     !Utility.isExpression(line.getFirstOperand())) {
                    error = ErrorTable.errorList[ErrorTable.WRONG_OPERAND_TYPE];
                    return true;
                }
            break;
            case "END":
                if (!Utility.isLabel(line.getFirstOperand())) {
                    error = ErrorTable.errorList[ErrorTable.WRONG_OPERAND_TYPE];
                    return true;
                }
                break;
            case "BYTE":
                if (line.getFirstOperand().equals("")) {
                    error = ErrorTable.errorList[ErrorTable.MISSING_MISPLACED_OPERAND_FIELD];
                    return true;
                }
                if ((line.getFirstOperand().startsWith("X") || line.getFirstOperand().startsWith("x"))) {
                    if (line.getFirstOperand().length() < 4 || line.getFirstOperand().length() > 17) {
                        error = ErrorTable.errorList[ErrorTable.INCORRECT_OPERAND_FORMAT];
                        return true;
                    }
                    if (!Utility.isHex(line.getFirstOperand().substring(2, line.getFirstOperand().length() - 1))) {
                        error = ErrorTable.errorList[ErrorTable.NOT_HEXADECIMAL_STRING];
                        return true;
                    }
                }
                if ((line.getFirstOperand().startsWith("C") || line.getFirstOperand().startsWith("c"))
                        && line.getFirstOperand().length() < 4 || line.getFirstOperand().length() > 18) {
                    error = ErrorTable.errorList[ErrorTable.INCORRECT_OPERAND_FORMAT];
                    return true;
                }
                break;
            case "WORD":
                if (line.getFirstOperand().equals("")) {
                    error = ErrorTable.errorList[ErrorTable.MISSING_MISPLACED_OPERAND_FIELD];
                    return true;
                }
                if (Utility.getNumberOfDigits(line.getFirstOperand()) > 4) {
                    error = ErrorTable.errorList[ErrorTable.OPERAND_EXCEEDED_NUMBER_OF_DECIMAL_DIGITS];
                    return true;
                }
                break;
            default:
                if (line.getFirstOperand().equals("")) {
                    error = ErrorTable.errorList[ErrorTable.MISSING_MISPLACED_OPERAND_FIELD];
                    return true;
                }
        }
        error = ErrorTable.errorList[ErrorTable.NO_ERROR];
        return false;
    }

    private boolean verifyAddressingMode(Line line) {
        switch (line.getAddressingMode()) {
            case "":
            case "@":
            case "#":
                error = ErrorTable.errorList[ErrorTable.NO_ERROR];
                return false;
            default:
                error = ErrorTable.errorList[ErrorTable.WRONG_ADDRESSING_MODE];
                return true;
        }
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void setLineError(Line line) {
        line.setError(error);
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ArrayList<String> getLabelList() {
        return labelList;
    }

    public void setLabelList(ArrayList<String> labelList) {
        this.labelList = labelList;
    }

}
