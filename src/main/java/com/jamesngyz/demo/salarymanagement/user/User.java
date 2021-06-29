package com.jamesngyz.demo.salarymanagement.user;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.*;

@Entity(name = "employee")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {
	
	@Id
	private String id;
	
	private String login;
	
	private String name;
	
	@EqualsAndHashCode.Exclude
	private BigDecimal salary;
	
	private LocalDate startDate;
	
	@EqualsAndHashCode.Include
	private BigDecimal salaryEquals() {
		return salary == null ? null : salary.stripTrailingZeros();
	}
	
}
