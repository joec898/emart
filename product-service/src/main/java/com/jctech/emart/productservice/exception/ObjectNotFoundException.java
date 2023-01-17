package com.jctech.emart.productservice.exception;

public class ObjectNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public ObjectNotFoundException() {}
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
