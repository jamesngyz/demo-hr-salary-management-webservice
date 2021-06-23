package com.jamesngyz.demo.salarymanagement.user;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
	
	private JdbcTemplate jdbcTemplate;
	
	public UserRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	Integer createOrUpdate(List<User> requested) {
		return null;
	}
	
}
