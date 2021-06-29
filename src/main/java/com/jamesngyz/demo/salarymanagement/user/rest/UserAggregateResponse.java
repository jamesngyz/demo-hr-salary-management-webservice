package com.jamesngyz.demo.salarymanagement.user.rest;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UserAggregateResponse {
	
	@JsonProperty("results")
	private List<UserResponse> users = new ArrayList<>();
	
}
