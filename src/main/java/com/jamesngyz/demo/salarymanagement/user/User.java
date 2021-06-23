package com.jamesngyz.demo.salarymanagement.user;

import java.math.BigDecimal;
import java.util.Date;

import com.jamesngyz.demo.salarymanagement.Constants;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {
	
	@CsvBindByName(column = "id")
	private String id;
	
	@CsvBindByName(column = "login")
	private String login;
	
	@CsvBindByName(column = "name")
	private String name;
	
	@CsvBindByName(column = "salary")
	@EqualsAndHashCode.Exclude
	private BigDecimal salary;
	
	@CsvBindByName(column = "startDate")
	@CsvDate(Constants.DATE_FORMAT)
	private Date startDate;
	
	@EqualsAndHashCode.Include
	private BigDecimal salaryEquals() {
		return salary == null ? null : salary.stripTrailingZeros();
	}
	
}
