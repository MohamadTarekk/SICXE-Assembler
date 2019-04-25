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
