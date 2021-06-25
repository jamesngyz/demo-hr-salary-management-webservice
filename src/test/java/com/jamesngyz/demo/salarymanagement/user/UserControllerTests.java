package com.jamesngyz.demo.salarymanagement.user;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
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
import com.jamesngyz.demo.salarymanagement.Constants;
import com.jamesngyz.demo.salarymanagement.error.ErrorResponse;
import com.jamesngyz.demo.salarymanagement.error.InvalidCsvException;
import com.jamesngyz.demo.salarymanagement.user.rest.UserCreateOrUpdateResponse;

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
		
		UserCreateOrUpdateResponse expectedResponse = new UserCreateOrUpdateResponse(1);
		String expected = objectMapper.writeValueAsString(expectedResponse);
		
		mockMvc.perform(
				multipart("/users/upload").file(file))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(expected));
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
		
		UserCreateOrUpdateResponse expectedResponse = new UserCreateOrUpdateResponse(0);
		String expected = objectMapper.writeValueAsString(expectedResponse);
		
		mockMvc.perform(
				multipart("/users/upload").file(file))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(expected));
	}
	
	@Test
	void uploadUsers_NoFileInRequest_HttpStatus400() throws Exception {
		ErrorResponse response = new ErrorResponse("File missing in request");
		String expected = objectMapper.writeValueAsString(response);
		
		mockMvc.perform(
				post("/users/upload")
						.contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(expected));
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
		
		ErrorResponse response = new ErrorResponse(InvalidCsvException.missingField().getMessage());
		String expected = objectMapper.writeValueAsString(response);
		
		mockMvc.perform(
				multipart("/users/upload").file(file))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(expected));
	}
}
