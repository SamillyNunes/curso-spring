package com.nunes.sam.resources.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.nunes.sam.services.exceptions.AuthorizationException;
import com.nunes.sam.services.exceptions.DataIntegrityException;
import com.nunes.sam.services.exceptions.FileException;
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
	
	@ExceptionHandler(AuthorizationException.class) //pra dizer que eh um tratador de excecoes o tipo objectNotFound
	public ResponseEntity<StandardError> authorization(AuthorizationException e, HttpServletRequest request){
		StandardError err = new StandardError(HttpStatus.FORBIDDEN.value(),e.getMessage(),System.currentTimeMillis());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
	@ExceptionHandler(FileException.class) //pra dizer que eh um tratador de excecoes o tipo objectNotFound
	public ResponseEntity<StandardError> file(FileException e, HttpServletRequest request){
		StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(),e.getMessage(),System.currentTimeMillis());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
	@ExceptionHandler(AmazonServiceException.class) //pra dizer que eh um tratador de excecoes o tipo objectNotFound
	public ResponseEntity<StandardError> amazonService(AmazonServiceException e, HttpServletRequest request){
		HttpStatus code = HttpStatus.valueOf(e.getErrorCode()); //isso pega o erro que veio da excecao 
		
		StandardError err = new StandardError(code.value(),e.getMessage(),System.currentTimeMillis());
		
		return ResponseEntity.status(code).body(err);
	}
	
	@ExceptionHandler(AmazonClientException.class) //pra dizer que eh um tratador de excecoes o tipo objectNotFound
	public ResponseEntity<StandardError> amazonClient(AmazonClientException e, HttpServletRequest request){
		StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(),e.getMessage(),System.currentTimeMillis());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
	@ExceptionHandler(AmazonS3Exception.class) //pra dizer que eh um tratador de excecoes o tipo objectNotFound
	public ResponseEntity<StandardError> amazonS3(AmazonS3Exception e, HttpServletRequest request){
		StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(),e.getMessage(),System.currentTimeMillis());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
}
