package com.jamesngyz.demo.salarymanagement.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public class UserControllerTests {
	
	private MockMvc mockMvc;
	
	@Autowired
	public UserControllerTests(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}
	
	@Test
	void uploadUsers_NewIdsAndLogins_HttpStatus201() throws Exception {
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"test.txt",
				"text/plain",
				"id,login,name,salary,startDate\ne0001,hpotter,Harry Potter,1234.00,16-Nov-01".getBytes());
		mockMvc.perform(
				multipart("/users/upload").file(file))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	void uploadUsers_NoFileInRequest_HttpStatus400() throws Exception {
		mockMvc.perform(
				post("/users/upload")
						.contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
}
