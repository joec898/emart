package com.jctech.emart.inventoryservice.exception;

public class ObjectNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public ObjectNotFoundException() {}
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
