package com.sedo.AxBitTest.exceptions;

public class ViolatedDataException extends RuntimeException {

	private String uri;

	public ViolatedDataException(String uri, String message) {
		super(message);
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}
}
