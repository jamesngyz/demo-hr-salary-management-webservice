package com.jamesngyz.demo.salarymanagement.error;

public class InvalidCsvException extends RuntimeException {
	
	public InvalidCsvException(String message) {
		super(message);
	}
	
	public static InvalidCsvException missingField() {
		return new InvalidCsvException("CSV contains missing field");
	}
	
	public static InvalidCsvException invalidFieldValue() {
		return new InvalidCsvException("CSV contains field with invalid value");
	}
	
}
