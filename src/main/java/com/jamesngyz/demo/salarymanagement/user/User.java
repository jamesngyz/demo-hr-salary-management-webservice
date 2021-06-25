package com.jamesngyz.demo.salarymanagement.user;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jamesngyz.demo.salarymanagement.CsvDateConverter;
import com.jamesngyz.demo.salarymanagement.error.InvalidCsvException;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {
	
	@CsvBindByName(column = "id", required = true)
	private String id;
	
	@CsvBindByName(column = "login", required = true)
	private String login;
	
	@CsvBindByName(column = "name", required = true)
	private String name;
	
	@CsvBindByName(column = "salary", required = true)
	@EqualsAndHashCode.Exclude
	private BigDecimal salary;
	
	@CsvCustomBindByName(column = "startDate", converter = CsvDateConverter.class, required = true)
	private LocalDate startDate;
	
	@EqualsAndHashCode.Include
	private BigDecimal salaryEquals() {
		return salary == null ? null : salary.stripTrailingZeros();
	}
	
	public void setSalary(BigDecimal salary) {
		if (salary.compareTo(BigDecimal.ZERO) < 0) {
			throw InvalidCsvException.invalidFieldValue();
		}
		this.salary = salary;
	}
	
}
