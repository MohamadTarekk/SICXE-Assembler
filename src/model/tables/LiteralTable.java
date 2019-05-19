package model.tables;

import model.Literal;

import java.util.HashMap;

public class LiteralTable {

	public static HashMap<String, Literal> literalTable = new HashMap<>();

	public static String getString() {
		StringBuilder listAsString = new StringBuilder();
		for (HashMap.Entry<String, Literal> literal : literalTable.entrySet()) {
			listAsString.append(literal.getValue().toString());
		}
		return listAsString.toString();
	}

}
