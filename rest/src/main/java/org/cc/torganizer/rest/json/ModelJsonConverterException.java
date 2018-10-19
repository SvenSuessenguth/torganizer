package org.cc.torganizer.rest.json;

public class ModelJsonConverterException extends RuntimeException {
	private static final long serialVersionUID = 8107389536301181498L;

	public ModelJsonConverterException(Exception exc) {
		super(exc);
	}
}
