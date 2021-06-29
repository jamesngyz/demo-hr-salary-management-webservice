package com.jamesngyz.demo.salarymanagement.user;

import java.util.List;

public interface UserJdbcRepository {
	
	public Integer createOrUpdate(List<User> request);
	
	public void create(User user);
	
}
