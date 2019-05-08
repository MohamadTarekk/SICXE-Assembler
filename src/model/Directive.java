package model;

import model.enums.Format;

public class Directive {

	private String directive;
	private Format format;

	public Directive(String directive, Format format) {
		this.directive = directive;
		this.format = format;
	}

	public String getDirective() {
		return directive;
	}

	public void setDirective(String directive) {
		this.directive = directive;
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}

	@Override
	public String toString() {
		return "Directive [directive=" + directive + ", format=" + format + "]";
	}
}
