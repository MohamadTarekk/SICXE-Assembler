package model;

public class ProgramCounter {
	
	private static ProgramCounter instance = null;
	
	public static ProgramCounter getInstance() {
		if (instance == null)
			instance = new ProgramCounter();
		return instance;
	}

}
