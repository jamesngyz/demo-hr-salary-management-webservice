package com.jamesngyz.demo.salarymanagement.user;

import java.util.List;

import com.jamesngyz.demo.salarymanagement.user.rest.UserAggregateResponse;
import com.jamesngyz.demo.salarymanagement.user.rest.UserRequest;
import com.jamesngyz.demo.salarymanagement.user.rest.UserResponse;

public final class UserDtoMapper {
	
	public static User csvRowToUser(UserCsvRow row) {
		return User.builder()
				.id(row.getId())
				.login(row.getLogin())
				.name(row.getName())
				.salary(row.getSalary())
				.startDate(row.getStartDate())
				.build();
	}
	
	public static UserResponse userToResponse(User user) {
		return UserResponse.builder()
				.id(user.getId())
				.login(user.getLogin())
				.name(user.getName())
				.salary(user.getSalary())
				.startDate(user.getStartDate())
				.build();
	}
	
	public static UserAggregateResponse usersToAggregateResponse(List<User> users) {
		UserAggregateResponse aggregateResponse = new UserAggregateResponse();
		for (User user : users) {
			UserResponse response = userToResponse(user);
			aggregateResponse.getUsers().add(response);
		}
		return aggregateResponse;
	}
	
	public static User requestToUser(UserRequest request) {
		return User.builder()
				.id(request.getId())
				.login(request.getLogin())
				.name(request.getName())
				.salary(request.getSalary())
				.startDate(request.getStartDate())
				.build();
	}
}
