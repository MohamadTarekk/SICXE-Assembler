package model;

import model.enums.Format;

public class Directive {

	private String directive;
	private Format length;

	public Directive(String directive, Format length) {
		this.directive = directive;
		this.length = length;
	}

	public String getDirective() {
		return directive;
	}

	public void setDirective(String directive) {
		this.directive = directive;
	}

	public Format getLength() {
		return length;
	}

	public void setLength(Format length) {
		this.length = length;
	}

	@Override
	public String toString() {
		return "Directive [directive=" + directive + ", length=" + length + "]";
	}
}
