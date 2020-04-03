package com.nunes.sam.resources.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.nunes.sam.services.exceptions.ObjectNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler { //manipulador de excecoes dos controladores pra enxugar mais as classes de controlador/recursos
	
	@ExceptionHandler(ObjectNotFoundException.class) //pra dizer que eh um tratador de excecoes o tipo objectNotFound
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request){
		StandardError err = new StandardError(HttpStatus.NOT_FOUND.value(),e.getMessage(),System.currentTimeMillis());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
}
