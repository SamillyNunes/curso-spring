package com.nunes.sam.resources.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.nunes.sam.services.exceptions.DataIntegrityException;
import com.nunes.sam.services.exceptions.ObjectNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler { //manipulador de excecoes dos controladores pra enxugar mais as classes de controlador/recursos
	
	@ExceptionHandler(ObjectNotFoundException.class) //pra dizer que eh um tratador de excecoes o tipo objectNotFound
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request){
		StandardError err = new StandardError(HttpStatus.NOT_FOUND.value(),e.getMessage(),System.currentTimeMillis());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
	@ExceptionHandler(DataIntegrityException.class) //pra dizer que eh um tratador de excecoes o tipo objectNotFound
	public ResponseEntity<StandardError> dataIntegrity(DataIntegrityException e, HttpServletRequest request){
		StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(),e.getMessage(),System.currentTimeMillis()); //bad request eh uma requisicao proibida?
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class) //pra dizer que eh um tratador de excecoes o tipo objectNotFound
	public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, HttpServletRequest request){
		ValidationError err = new ValidationError(HttpStatus.BAD_REQUEST.value(),"Erro de validação",System.currentTimeMillis()); //bad request eh uma requisicao proibida?
		
		for(FieldError x : e.getBindingResult().getFieldErrors()) { //esta percorrendo o json que tem nas excecoes do tipo arumento nao valido e pegando somente o nome e a mensagem do erro
			err.addError(x.getField(), x.getDefaultMessage());
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
}
