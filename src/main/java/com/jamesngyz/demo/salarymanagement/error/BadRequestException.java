package com.jamesngyz.demo.salarymanagement.error;

public class BadRequestException extends RuntimeException {
	
	public BadRequestException(String message) {
		super(message);
	}
	
	public static BadRequestException idAlreadyExists() {
		return new BadRequestException("Employee ID already exists");
	}
	
	public static BadRequestException loginNotUnique() {
		return new BadRequestException("Employee login not unique");
	}
}
