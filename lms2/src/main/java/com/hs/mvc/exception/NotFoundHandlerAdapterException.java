package com.hs.mvc.exception;

import jakarta.servlet.ServletException;

public class NotFoundHandlerAdapterException extends ServletException {
	private static final long serialVersionUID = 1L;
	
	public NotFoundHandlerAdapterException(final String message) {
        super(message);
    }
}
