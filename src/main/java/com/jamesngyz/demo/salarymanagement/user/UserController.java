package com.jamesngyz.demo.salarymanagement.user;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import com.jamesngyz.demo.salarymanagement.error.BadRequestException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.jamesngyz.demo.salarymanagement.common.MessageResponse;
import com.jamesngyz.demo.salarymanagement.common.OffsetPageable;
import com.jamesngyz.demo.salarymanagement.user.dto.UserDtoMapper;
import com.jamesngyz.demo.salarymanagement.user.rest.UserAggregateResponse;
import com.jamesngyz.demo.salarymanagement.user.rest.UserRequest;
import com.jamesngyz.demo.salarymanagement.user.rest.UserResponse;

@RestController
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping(path = "/users/upload", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<MessageResponse> uploadUsers(@RequestParam("file") MultipartFile file)
			throws IOException {
		
		List<User> requested = userService.csvToUsers(file);
		Integer createdOrUpdated = userService.createOrUpdateUsers(requested);
		MessageResponse response = new MessageResponse("Successfully created/updated");
		
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
	
	@GetMapping(path = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<UserResponse> fetchUser(@PathVariable(name = "id") String id) {
		User result = userService.getUser(id);
		if (result == null) {
			throw BadRequestException.noSuchEmployee();
		}
		UserResponse response = UserDtoMapper.userToResponse(result);
		return ResponseEntity.ok().body(response);
	}
	
	@PostMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<MessageResponse> createUser(@Valid @RequestBody UserRequest request) {
		
		User user = UserDtoMapper.requestToUser(request);
		userService.createUser(user);
		return ResponseEntity
				.created(URI.create("/users/" + request.getId()))
				.body(new MessageResponse("Successfully created"));
	}
	
	@PutMapping(path = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<MessageResponse> updateUser(@PathVariable(name = "id") String id,
			@Valid @RequestBody UserRequest request) {
		
		User user = UserDtoMapper.requestToUser(request);
		userService.updateUser(id, user);
		return ResponseEntity.ok().body(new MessageResponse("Successfully updated"));
	}
	
	@DeleteMapping(path = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<MessageResponse> deleteUser(@PathVariable(name = "id") String id) {
		
		userService.deleteUser(id);
		return ResponseEntity.ok().body(new MessageResponse("Successfully deleted"));
	}
	
}
