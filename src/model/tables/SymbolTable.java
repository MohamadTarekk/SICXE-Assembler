package model.tables;

import java.util.HashMap;

import model.Symbol;

public class SymbolTable {
	
	public static HashMap<String, Symbol> symbolTable = new HashMap<>();

	public static HashMap<String, Symbol> getSymbolTable() {
		return symbolTable;
	}

	public static void setSymbolTable(HashMap<String, Symbol> symbolTable) {
		SymbolTable.symbolTable = symbolTable;
	}
	
	public static String getString() {
		
		String table = "";
		for(HashMap.Entry<String, Symbol> symbol : symbolTable.entrySet()) {
			table += symbol.getValue().toString();
		}
		return table;
	}
	
	
}