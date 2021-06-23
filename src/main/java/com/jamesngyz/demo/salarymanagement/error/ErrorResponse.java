package com.jamesngyz.demo.salarymanagement.error;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {
	
	@JsonProperty("message")
	private String message;
	
}
