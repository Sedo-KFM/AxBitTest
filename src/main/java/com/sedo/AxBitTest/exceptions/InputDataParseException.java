package com.sedo.AxBitTest.exceptions;

public class InputDataParseException extends RuntimeException{

	private String uri;

	public InputDataParseException(String uri, String message) {
		super(message);
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}
}
