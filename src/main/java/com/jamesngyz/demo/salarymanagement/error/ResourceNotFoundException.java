package com.jamesngyz.demo.salarymanagement.error;

public class ResourceNotFoundException extends RuntimeException {
	
	public ResourceNotFoundException(String path) {
		super("Resource not found: " + path);
	}
	
}
