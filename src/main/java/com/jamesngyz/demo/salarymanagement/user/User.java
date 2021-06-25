package com.jamesngyz.demo.salarymanagement.user;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jamesngyz.demo.salarymanagement.CsvDateConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

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
	
	@CsvCustomBindByName(column = "startDate", converter = CsvDateConverter.class)
	private LocalDate startDate;
	
	@EqualsAndHashCode.Include
	private BigDecimal salaryEquals() {
		return salary == null ? null : salary.stripTrailingZeros();
	}
	
}
