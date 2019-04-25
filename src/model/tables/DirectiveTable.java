package model.tables;

import java.util.HashMap;

import model.Directive;

public class DirectiveTable {

	private HashMap<String, Directive> directiveTable = new HashMap<>();

	public void loadDirectiveTable() {
		directiveTable.put("START", new Directive("START", 0));
		directiveTable.put("END", new Directive("END", 0));
		directiveTable.put("BYTE", new Directive("BYTE", 1));
		directiveTable.put("WORD", new Directive("WORD", 3));
		directiveTable.put("RESB", new Directive("RESB", -1));
		directiveTable.put("RESW", new Directive("RESW", -1));
		directiveTable.put("EQU", new Directive("EQU", 0));
		directiveTable.put("ORG", new Directive("ORG", 0));
		directiveTable.put("BASE", new Directive("BASE", 0));
		directiveTable.put("NOBASE", new Directive("NOBASE", 0));
		directiveTable.put("LTORG", new Directive("START", 0));
	}

	public HashMap<String, Directive> getDirectiveTable() {
		return directiveTable;
	}

	public void setDirectiveTable(HashMap<String, Directive> directiveTable) {
		this.directiveTable = directiveTable;
	}

	/*
	 * Length = 0 -> NONE Length = -1 -> Variable Length
	 */

}
