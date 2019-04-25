package model;

public class Directive {

	private String directive;
	private int length;

	public Directive(String directive, int length) {
		this.directive = directive;
		this.length = length;
	}

	public String getDirective() {
		return directive;
	}

	public void setDirective(String directive) {
		this.directive = directive;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

}
