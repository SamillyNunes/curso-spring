package com.nunes.sam.services.exceptions;

public class AuthorizationException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public AuthorizationException(String msg) {
		super(msg);
	}
	
	public AuthorizationException(String msg, Throwable cause) { //nesse caso tem a causa da excecao tb
		super(msg, cause);
	}

}
