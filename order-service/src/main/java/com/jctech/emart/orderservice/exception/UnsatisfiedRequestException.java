package com.jctech.emart.orderservice.exception;

public class UnsatisfiedRequestException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public UnsatisfiedRequestException() {}
    public UnsatisfiedRequestException(String message) {
        super(message);
    }
}
