package com.jamesngyz.demo.salarymanagement.user;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jamesngyz.demo.salarymanagement.OffsetPageable;
import com.jamesngyz.demo.salarymanagement.user.rest.UserAggregateResponse;
import com.jamesngyz.demo.salarymanagement.user.rest.UserCreateOrUpdateResponse;

@RestController
public class UserController {
	
	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping(path = "/users/upload", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<UserCreateOrUpdateResponse> uploadUsers(@RequestParam("file") MultipartFile file)
			throws IOException {
		
		List<User> requested = userService.csvToUsers(file);
		Integer createdOrUpdated = userService.createOrUpdateUsers(requested);
		UserCreateOrUpdateResponse response = new UserCreateOrUpdateResponse(createdOrUpdated);
		
		if (createdOrUpdated == 0) {
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.created(URI.create("")).body(response);
		}
	}
	
	@GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<UserAggregateResponse> fetchUsers(
			@RequestParam(name = "minSalary", required = false) BigDecimal minSalary,
			@RequestParam(name = "maxSalary", required = false) BigDecimal maxSalary,
			@RequestParam(name = "offset", required = false) Long offset,
			@RequestParam(name = "limit", required = false) Integer limit) {
		
		minSalary = minSalary == null ? BigDecimal.ZERO : minSalary;
		maxSalary = maxSalary == null ? BigDecimal.valueOf(4000) : maxSalary;
		OffsetPageable pageable = new OffsetPageable(offset, limit);
		
		List<User> users = userService.getUsersWithSalaryBetween(minSalary, maxSalary, pageable);
		UserAggregateResponse response = UserDtoMapper.usersToAggregateResponse(users);
		return ResponseEntity.ok().body(response);
	}
}
