package model.tables;

import java.util.HashMap;

public class RegisterTable {
	
	private HashMap<String,Integer> registerTable = new HashMap<>();
	
	public void loadRegisterTabkle() {
		registerTable.put("A", 0);
		registerTable.put("X", 1);
		registerTable.put("L", 2);
		registerTable.put("B", 3);
		registerTable.put("S", 4);
		registerTable.put("T", 5);
		registerTable.put("F", 6);
		registerTable.put("PC", 8);
		registerTable.put("SW", 9);
	}

}

	/*
	 * SIC machines have several registers, each 24 bits long and having both a numeric and character representation:
		A (0): Used for basic arithmetic operations; known as the accumulator register.
		X (1): Stores and calculates addresses; known as the index register.
		L (2): Used for jumping to specific memory addresses and storing return addresses; known as the linkage register.
		PC (8): Contains the address of the next instruction to execute; known as the program counter register.
		SW (9): Contains a variety of information, such as carry or overflow flags; known as the status word register.
		In addition to the standard SIC registers, there are also four additional general-purpose registers specific to the SIC/XE machine:
		B (3): Used for addressing; known as the base register.
		S (4): No special use, general purpose register.
		T (5): No special use, general purpose register.
		F (6): Floating point accumulator register (This register is 48-bits instead of 24).
	*/
