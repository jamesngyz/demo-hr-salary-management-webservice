package com.jamesngyz.demo.salarymanagement.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.jamesngyz.demo.salarymanagement.Constants;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
	
	@InjectMocks
	private UserService subject;
	
	@Mock
	private UserRepository repository;
	
	@Test
	void createOrUpdateUsers_Created_ReturnCount() throws ParseException {
		List<User> requested = new ArrayList<>();
		SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH);
		Date startDate = formatter.parse("16-Nov-01");
		User user = User.builder()
				.id("e0001")
				.login("hpotter")
				.name("Harry Potter")
				.salary(BigDecimal.valueOf(1234.00))
				.startDate(startDate)
				.build();
		requested.add(user);
		when(repository.createOrUpdate(requested)).thenReturn(1);
		
		Integer result = subject.createOrUpdateUsers(requested);
		
		assertThat(result).isEqualTo(1);
	}
	
	@Test
	void csvToUsers_ValidFile_ReturnUsers() throws IOException, ParseException {
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"test.txt",
				"text/plain",
				"id,login,name,salary,startDate\ne0001,hpotter,Harry Potter,1234.00,16-Nov-01".getBytes());
		
		SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH);
		Date startDate = formatter.parse("16-Nov-01");
		User user = User.builder()
				.id("e0001")
				.login("hpotter")
				.name("Harry Potter")
				.salary(BigDecimal.valueOf(1234.00))
				.startDate(startDate)
				.build();
		
		List<User> result = subject.csvToUsers(file);
		
		assertThat(result).isNotNull();
		assertThat(result).hasSize(1);
		assertThat(result).contains(user);
	}
}
