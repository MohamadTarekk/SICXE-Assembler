package model.tables;

import java.util.HashMap;

import model.Symbol;

public class SymbolTable {

	public static HashMap<String, Symbol> symbolTable = new HashMap<>();

	public static String getString() {

		StringBuilder table = new StringBuilder();
		for (HashMap.Entry<String, Symbol> symbol : symbolTable.entrySet()) {
			table.append(symbol.getValue().toString());
		}
		return table.toString();
	}
	
	

}