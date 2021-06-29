package com.jamesngyz.demo.salarymanagement.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamesngyz.demo.salarymanagement.common.Constants;
import com.jamesngyz.demo.salarymanagement.common.OffsetPageable;
import com.jamesngyz.demo.salarymanagement.error.BadRequestException;
import com.jamesngyz.demo.salarymanagement.error.InvalidCsvException;
import com.jamesngyz.demo.salarymanagement.user.rest.UserAggregateResponse;
import com.jamesngyz.demo.salarymanagement.user.rest.UserRequest;
import com.jamesngyz.demo.salarymanagement.user.rest.UserResponse;

@WebMvcTest
public class UserControllerTests {
	
	private MockMvc mockMvc;
	private ObjectMapper objectMapper;
	
	@MockBean
	private UserService service;
	
	@Autowired
	public UserControllerTests(MockMvc mockMvc, ObjectMapper objectMapper) {
		this.mockMvc = mockMvc;
		this.objectMapper = objectMapper;
	}
	
	@Test
	void uploadUsers_NewIdsAndLogins_HttpStatus201() throws Exception {
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"test.txt",
				"text/plain",
				"id,login,name,salary,startDate\ne0001,hpotter,Harry Potter,1234.00,16-Nov-01".getBytes());
		
		SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT_DD_MMM_YY, Locale.ENGLISH);
		Date startDate = formatter.parse("16-Nov-01");
		
		List<User> users = new ArrayList<>();
		User user = User.builder()
				.id("e0001")
				.login("hpotter")
				.name("Harry Potter")
				.salary(BigDecimal.valueOf(1234.00))
				.startDate(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
				.build();
		users.add(user);
		when(service.csvToUsers(file)).thenReturn(users);
		when(service.createOrUpdateUsers(users)).thenReturn(1);
		
		mockMvc.perform(
				multipart("/users/upload").file(file))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json("{\"message\": \"Successfully created/updated\"}"));
	}
	
	@Test
	void uploadUsers_SuccessWithNoDataUpdated_HttpStatus200() throws Exception {
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"test.txt",
				"text/plain",
				"id,login,name,salary,startDate\ne0001,hpotter,Harry Potter,1234.00,16-Nov-01".getBytes());
		
		SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT_DD_MMM_YY, Locale.ENGLISH);
		Date startDate = formatter.parse("16-Nov-01");
		
		List<User> users = new ArrayList<>();
		User user = User.builder()
				.id("e0001")
				.login("hpotter")
				.name("Harry Potter")
				.salary(BigDecimal.valueOf(1234.00))
				.startDate(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
				.build();
		users.add(user);
		when(service.csvToUsers(file)).thenReturn(users);
		when(service.createOrUpdateUsers(users)).thenReturn(0);
		
		mockMvc.perform(
				multipart("/users/upload").file(file))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json("{\"message\": \"Successfully created/updated\"}"));
	}
	
	@Test
	void uploadUsers_NoFileInRequest_HttpStatus400() throws Exception {
		mockMvc.perform(
				post("/users/upload")
						.contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json("{\"message\": \"File missing in request\"}"));
	}
	
	@Test
	void uploadUsers_CsvHasError_HttpStatus400() throws Exception {
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"test.txt",
				"text/plain",
				("id,login,name,salary,startDate\n" +
						"e0001,hpotter,,1234.00,16-Nov-01\n" +
						"e0002,rwesley,Ron Weasley,19234.50,2001-11-16").getBytes());
		
		when(service.csvToUsers(file)).thenThrow(InvalidCsvException.missingField());
		
		mockMvc.perform(
				multipart("/users/upload").file(file))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json("{\"message\": \"CSV contains missing field\"}"));
	}
	
	@Test
	void fetchUsers_NoParameters_HttpStatus200AndReturnAllUsers() throws Exception {
		User user1 = User.builder()
				.id("e0001")
				.login("hpotter")
				.name("Harry Potter")
				.salary(BigDecimal.valueOf(1234.00))
				.startDate(LocalDate.parse("16-Nov-01", DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_DD_MMM_YY)))
				.build();
		User user2 = User.builder()
				.id("e0002")
				.login("rwesley")
				.name("Ron Weasley")
				.salary(BigDecimal.valueOf(1234.50))
				.startDate(LocalDate.parse("2001-11-16", DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_YYYY_MM_DD)))
				.build();
		List<User> users = new ArrayList<>();
		users.add(user1);
		users.add(user2);
		
		when(service.getUsersWithSalaryBetween(BigDecimal.ZERO, new BigDecimal(4000), new OffsetPageable()))
				.thenReturn(users);
		UserAggregateResponse expectedResponse = UserDtoMapper.usersToAggregateResponse(users);
		String expected = objectMapper.writeValueAsString(expectedResponse);
		
		mockMvc.perform(get("/users"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(expected));
	}
	
	@Test
	void getUser_validId_HttpStatus200() throws Exception {
		String id = "e0001";
		User user = User.builder()
				.id(id)
				.login("hpotter")
				.name("Harry Potter")
				.salary(BigDecimal.valueOf(1234.00))
				.startDate(LocalDate.parse("16-Nov-01", DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_DD_MMM_YY)))
				.build();
		
		when(service.getUser(id)).thenReturn(user);
		
		UserResponse expectedResponse = UserDtoMapper.userToResponse(user);
		String expected = objectMapper.writeValueAsString(expectedResponse);
		
		mockMvc.perform(get("/users/" + id))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(expected));
	}
	
	@Test
	void getUser_UserNotFound_HttpStatus400() throws Exception {
		String id = "e0001";
		when(service.getUser(id)).thenReturn(null);
		
		mockMvc.perform(get("/users/" + id))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void createUser_ValidRequest_HttpStatus201() throws Exception {
		String requestJson = "{\"id\": \"emp0001\", " +
				"\"login\": \"hpotter\", " +
				"\"name\": \"Harry Potter\", " +
				"\"salary\": 1234.00, " +
				"\"startDate\": \"2001-12-16\"}";
		
		mockMvc.perform(
				post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json("{\"message\": \"Successfully created\"}"));
	}
	
	@Test
	void createUser_IdAlreadyExists_HttpStatus400() throws Exception {
		UserRequest request = UserRequest.builder()
				.id("emp0001")
				.login("hpotter")
				.name("Harry Potter")
				.salary(BigDecimal.valueOf(1234.00))
				.startDate(LocalDate.parse("2001-11-16", DateTimeFormatter.ISO_DATE))
				.build();
		String requestJson = objectMapper.writeValueAsString(request);
		
		doThrow(BadRequestException.idAlreadyExists()).when(service).createUser(any());
		
		mockMvc.perform(
				post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json("{\"message\": \"Employee ID already exists\"}"));
	}
	
	@Test
	void createUser_LoginNotUnique_HttpStatus400() throws Exception {
		UserRequest request = UserRequest.builder()
				.id("emp0001")
				.login("hpotter")
				.name("Harry Potter")
				.salary(BigDecimal.valueOf(1234.00))
				.startDate(LocalDate.parse("2001-11-16", DateTimeFormatter.ISO_DATE))
				.build();
		String requestJson = objectMapper.writeValueAsString(request);
		
		doThrow(BadRequestException.loginNotUnique()).when(service).createUser(any());
		
		mockMvc.perform(
				post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json("{\"message\": \"Employee login not unique\"}"));
	}
	
	@Test
	void createUser_NegativeSalary_HttpStatus400() throws Exception {
		UserRequest request = UserRequest.builder()
				.id("emp0001")
				.login("hpotter")
				.name("Harry Potter")
				.salary(BigDecimal.valueOf(-1234.00))
				.startDate(LocalDate.parse("2001-11-16", DateTimeFormatter.ISO_DATE))
				.build();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json("{\"message\": \"Invalid salary\"}"));
	}
	
	@Test
	void createUser_InvalidDate_HttpStatus400() throws Exception {
		String requestJson = "{\"id\": \"emp0001\", " +
				"\"login\": \"hpotter\", " +
				"\"name\": \"Harry Potter\", " +
				"\"salary\": 1234.00, " +
				"\"startDate\": \"2001-13-16\"}";
		
		mockMvc.perform(
				post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json("{\"message\": \"Invalid date\"}"));
	}
	
	@Test
	void updateUser_ValidRequest_HttpStatus200() throws Exception {
		String id = "emp0001";
		String requestJson = "{\"login\": \"hpotter\", " +
				"\"name\": \"Harry Potter\", " +
				"\"salary\": 1234.00, " +
				"\"startDate\": \"2001-12-16\"}";
		
		mockMvc.perform(
				put("/users/" + id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json("{\"message\": \"Successfully updated\"}"));
	}
	
	@Test
	void deleteUser_ValidId_HttpStatus200() throws Exception {
		String id = "emp0001";
		
		mockMvc.perform(
				delete("/users/" + id))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json("{\"message\": \"Successfully deleted\"}"));
	}
	
	@Test
	void deleteUser_UserNotFound_HttpStatus400() throws Exception {
		String id = "emp0001";
		
		doThrow(BadRequestException.noSuchEmployee()).when(service).deleteUser(any());
		
		mockMvc.perform(
				delete("/users/" + id))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json("{\"message\": \"No such employee\"}"));
	}
	
}
