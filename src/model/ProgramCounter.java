package model;

import java.util.ArrayList;

import controller.Controller;
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
	private int literalsStartIndex = 0;

	public int getProgramCounter() {
		return locationCounter;
	}

	public void setLocationCounter(int locationCounter) {
		this.locationCounter = locationCounter;
	}

	public int getLiteralsStartIndex() {
		return literalsStartIndex;
	}

	public void setLiteralsStartIndex(int literalsStartIndex) {
		this.literalsStartIndex = literalsStartIndex;
	}

	public void resetAddresses() {
		locationCounter = 0;
		literalsStartIndex = 0;
	}

	public void updateCounters(Line line, ArrayList<Line> lineList) {
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
			if(Utility.isNumeric(line.getFirstOperand())) {
				System.out.println("test");
				locationCounter = Utility.hexToDecimal(line.getFirstOperand());
			}else {
				for (Line line2 : lineList) {
					if(line2.getLabel().equals(line.getFirstOperand())) {
						locationCounter = Utility.hexToDecimal((line2.getLocation()));
					}
				}
			}
		}
		if (mnemonic.equalsIgnoreCase("LTORG")) {
			Controller.fillLiteralsTable(lineList);
		}

		if (Utility.isDirective(mnemonic)) {
			switch (DirectiveTable.directiveTable.get(mnemonic).getFormat()) {
			case THREE:
				locationCounter += 3;
			case VARIABLE:
				// noinspection IfCanBeSwitch
				if (mnemonic.toUpperCase().equals("RESB")) {
					locationCounter += Integer.parseInt(line.getFirstOperand());
				} else if (mnemonic.toUpperCase().equals("RESW")) {
					locationCounter += 3 * Integer.parseInt(line.getFirstOperand());
				} else if (mnemonic.toUpperCase().equals("BYTE")) {
					String firstOperand = line.getFirstOperand().substring(0, 1).toUpperCase();
					firstOperand = firstOperand + line.getFirstOperand().substring(1);
					switch (firstOperand.charAt(0)) {
					case 'C':
						locationCounter += (firstOperand.length() - 3); // C'EOF' -> EOF -> 3 bytes
						break;
					case 'X':
						locationCounter += Math.ceil((float) (firstOperand.length() - 3) / 2);
						break;
					default:
						// TODO: check that the value doesn't cause an overflow for a byte
						/*
						 * if (Utility.isNumeric(firstOperand)) { int value = (int)
						 * Long.parseLong(firstOperand); if (value >= -128 && value <= 127) {
						 * locationCounter += 1; } else {
						 * 
						 * } } else {
						 * 
						 * }
						 */ locationCounter += 1;
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
