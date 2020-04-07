package com.nunes.sam.services.exceptions;

public class DataIntegrityException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public DataIntegrityException(String msg) {
		super(msg);
	}
	
	public DataIntegrityException(String msg, Throwable cause) { //nesse caso tem a causa da excecao tb
		super(msg, cause);
	}

}
