package model;

import model.tables.DirectiveTable;
import model.tables.InstructionTable;
import model.utility.Utility;

public class ProgramCounter {

	private static ProgramCounter instance = null;

	private ProgramCounter() {
	}

	public static ProgramCounter getInstance() {
		if (instance == null)
			instance = new ProgramCounter();
		return instance;
	}

	private int locationCounter = 0;

	public int getProgramCounter() {
		return locationCounter;
	}

	public void resetAddresses() {
		locationCounter = 0;
	}

	public void updateCounters(Line line) {
		String mnemonic = line.getMnemonic();
		String hexaValue = Utility.convertToHexa(locationCounter);
		line.setLocation(hexaValue);
		if (mnemonic == null || mnemonic.equals(""))
			return;
		if (mnemonic.equals("Start".toUpperCase())) {
			locationCounter = Utility.hexToDecimal(line.getFirstOperand());
			hexaValue = Utility.convertToHexa(locationCounter);
			line.setLocation(hexaValue);
		}

		if (mnemonic.equals("Org".toUpperCase())) {
			locationCounter = Utility.hexToDecimal(line.getFirstOperand());
		}

		if (Utility.isDirective(mnemonic)) {
			switch (DirectiveTable.directiveTable.get(mnemonic).getLength()) {
			case THREE:
				locationCounter += 3;
			case VARIABLE:
				// noinspection IfCanBeSwitch
				if (mnemonic.toUpperCase().equals("RESB")) {
					locationCounter += Integer.parseInt(line.getFirstOperand());
				} else if (mnemonic.toUpperCase().equals("RESW")) {
					locationCounter += 3 * Integer.parseInt(line.getFirstOperand());
				} else if (mnemonic.toUpperCase().equals("BYTE")) {
					String firstOperand = line.getFirstOperand();
					switch (firstOperand.charAt(0)) {
					case 'C':
						locationCounter += (firstOperand.length() - 3); // C'EOF' -> EOF -> 3 bytes
						break;
					case 'X':
						locationCounter += (firstOperand.length() - 3);
						break;
					}
				}
				break;
			default:
				break;
			}
		} else if (Utility.isInstruction(mnemonic)) {
			switch (InstructionTable.instructionTable.get(mnemonic).getFormat()) {
			case ONE:
				locationCounter += 1;
				break;
			case TWO:
				locationCounter += 2;
				break;
			case THREE:
				locationCounter += 3;
				break;
			case FOUR:
				locationCounter += 4;
				break;
			default:
				break;
			}
		}
	}

}
