package com.jamesngyz.demo.salarymanagement.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler({ InvalidCsvException.class })
	protected ResponseEntity<Object> handleInvalidCsv(InvalidCsvException ex) {
		
		return ResponseEntity.badRequest()
				.contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorResponse(ex.getMessage()));
	}
	
	@ExceptionHandler({ ResourceNotFoundException.class })
	protected ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {
		
		return ResponseEntity.badRequest()
				.contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorResponse(ex.getMessage()));
	}
	
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestPart(
			MissingServletRequestPartException ex,
			HttpHeaders headers,
			HttpStatus status,
			WebRequest request) {
		
		return ResponseEntity.badRequest()
				.contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorResponse("File missing in request"));
	}
	
}
