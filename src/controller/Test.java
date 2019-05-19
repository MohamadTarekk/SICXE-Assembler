package controller;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Test {

	public static void main(String[] args) {
		// System.out.println(Utility.evaluateExpression("(506+8)"));
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		String foo = "40+2+(5+6)*2";
		try {
			System.out.println(engine.eval(foo));
		} catch (ScriptException e) {
			System.out.println("Expression evaluation failed!!!");
		}

	}

}
