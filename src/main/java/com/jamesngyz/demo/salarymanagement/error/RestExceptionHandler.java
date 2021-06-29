package com.jamesngyz.demo.salarymanagement.error;

import java.time.LocalDate;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler({ InvalidCsvException.class,
			ResourceNotFoundException.class,
			BadRequestException.class })
	protected ResponseEntity<Object> handleBadRequest(Exception ex) {
		
		return ResponseEntity.badRequest()
				.contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorResponse(ex.getMessage()));
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers,
			HttpStatus status,
			WebRequest request) {
		
		ObjectError e = ex.getBindingResult().getAllErrors().get(0);
		String message = e.getDefaultMessage();
		if (e instanceof FieldError && ((FieldError) e).getField().equals("salary")) {
			message = "Invalid salary";
		}
		return ResponseEntity.badRequest()
				.contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorResponse(message));
	}
	
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
			HttpMessageNotReadableException ex,
			HttpHeaders headers,
			HttpStatus status,
			WebRequest request) {
		
		String message = ex.getMessage();
		if (ex.getCause() != null && ex.getCause() instanceof InvalidFormatException
				&& ((InvalidFormatException) ex.getCause()).getTargetType() == LocalDate.class) {
			message = "Invalid date";
		}
		return ResponseEntity.badRequest()
				.contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorResponse(message));
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
