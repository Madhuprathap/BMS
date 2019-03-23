package com.bms.exceptions;

public class BMSException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public BMSException(String msg) {
		super(msg);
	}
}
