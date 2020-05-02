package com.nunes.sam.services.exceptions;

public class FileException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public FileException(String msg) {
		super(msg);
	}
	
	public FileException(String msg, Throwable cause) { //nesse caso tem a causa da excecao tb
		super(msg, cause);
	}

}
