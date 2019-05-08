package model.tables;

import java.util.HashMap;

import model.Directive;
import model.enums.Format;

public class DirectiveTable {

	public static HashMap<String, Directive> directiveTable = new HashMap<>();

	public static void loadDirectiveTable() {
		directiveTable.put("START", new Directive("START", Format.NONE));
		directiveTable.put("END", new Directive("END", Format.NONE));
		directiveTable.put("BYTE", new Directive("BYTE", Format.VARIABLE));
		directiveTable.put("WORD", new Directive("WORD", Format.THREE));
		directiveTable.put("RESB", new Directive("RESB", Format.VARIABLE));
		directiveTable.put("RESW", new Directive("RESW", Format.VARIABLE));
		directiveTable.put("EQU", new Directive("EQU", Format.NONE));
		directiveTable.put("ORG", new Directive("ORG", Format.NONE));
		directiveTable.put("BASE", new Directive("BASE", Format.NONE));
		directiveTable.put("NOBASE", new Directive("NOBASE", Format.NONE));
		directiveTable.put("LTORG", new Directive("LTORG", Format.NONE));
	}

	public HashMap<String, Directive> getDirectiveTable() {
		return directiveTable;
	}

	public void setDirectiveTable(HashMap<String, Directive> directiveTable) {
		DirectiveTable.directiveTable = directiveTable;
	}

}
