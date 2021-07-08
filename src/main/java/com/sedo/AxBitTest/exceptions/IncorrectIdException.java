package com.sedo.AxBitTest.exceptions;

public class IncorrectIdException extends RuntimeException {

	private String uri;

	public IncorrectIdException(String uri, String message) {
		super(message);
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}
}
