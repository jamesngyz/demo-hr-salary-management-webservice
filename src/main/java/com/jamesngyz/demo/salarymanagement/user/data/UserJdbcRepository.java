package com.jamesngyz.demo.salarymanagement.user.data;

import com.jamesngyz.demo.salarymanagement.user.User;

import java.util.List;

public interface UserJdbcRepository {
	
	public Integer createOrUpdate(List<User> request);
	
	public void create(User user);
	
}
