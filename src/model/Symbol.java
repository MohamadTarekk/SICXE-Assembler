package model;

import model.utility.Utility;

public class Symbol {

	private String symbol;
	private String address;
	
	public Symbol(String symbol, String address) {
		
		this.symbol = symbol;
		this.address = address;
	}
	
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return symbol + Utility.getSpaces(12-symbol.length()) + address + "\n";
	}
	
}
