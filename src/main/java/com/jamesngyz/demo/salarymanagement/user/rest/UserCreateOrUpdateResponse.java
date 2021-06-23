package com.jamesngyz.demo.salarymanagement.user.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserCreateOrUpdateResponse {
	
	@JsonProperty("createdOrUpdated")
	private Integer createdOrUpdated;
	
}
