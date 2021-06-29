package com.jamesngyz.demo.salarymanagement.user.rest;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jamesngyz.demo.salarymanagement.Constants;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("login")
	private String login;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("salary")
	private BigDecimal salary;
	
	@JsonProperty("startDate")
	@JsonFormat(pattern = Constants.DATE_FORMAT_YYYY_MM_DD)
	private LocalDate startDate;
	
}
