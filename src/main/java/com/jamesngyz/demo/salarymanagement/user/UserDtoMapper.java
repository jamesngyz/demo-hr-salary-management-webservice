package com.jamesngyz.demo.salarymanagement.user;

import java.util.List;

import com.jamesngyz.demo.salarymanagement.user.rest.UserAggregateResponse;
import com.jamesngyz.demo.salarymanagement.user.rest.UserRequest;
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
