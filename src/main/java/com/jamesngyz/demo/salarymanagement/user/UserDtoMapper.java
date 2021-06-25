package com.jamesngyz.demo.salarymanagement.user;

import com.jamesngyz.demo.salarymanagement.user.rest.UserResponse;

public final class UserDtoMapper {
	
	public static UserResponse userToResponse(User user) {
		UserResponse response = UserResponse.builder()
				.id(user.getId())
				.login(user.getLogin())
				.name(user.getName())
				.salary(user.getSalary())
				.startDate(user.getStartDate())
				.build();
		return response;
	}
	
}
