package com.jamesngyz.demo.salarymanagement;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageResponse {
	
	@JsonProperty("message")
	private String message;
	
}
