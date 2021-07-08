package com.sedo.AxBitTest.exceptions;

public class InputDataValidateException extends RuntimeException {

	private String uri;

	public InputDataValidateException(String uri, String message) {
		super(message);
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}
}
