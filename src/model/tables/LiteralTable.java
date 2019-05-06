package model.tables;

import model.Literal;

import java.util.ArrayList;

public class LiteralTable {

	public static ArrayList<Literal> literalList = new ArrayList<>();

	public static String getString() {

		StringBuilder listAsString = new StringBuilder();
		for (Literal literal : literalList) {
			listAsString.append(literal.toString());
		}
		return listAsString.toString();
	}

}
