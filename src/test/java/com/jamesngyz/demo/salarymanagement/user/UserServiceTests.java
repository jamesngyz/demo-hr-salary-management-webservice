package com.jamesngyz.demo.salarymanagement.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
import com.jamesngyz.demo.salarymanagement.error.InvalidCsvException;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
	
	@InjectMocks
	private UserService subject;
	
	@Mock
	private UserRepository repository;
	
	@Test
	void createOrUpdateUsers_Created_ReturnCount() throws ParseException {
		List<User> requested = new ArrayList<>();
		SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT_DD_MMM_YY, Locale.ENGLISH);
		Date startDate = formatter.parse("16-Nov-01");
		User user = User.builder()
				.id("e0001")
				.login("hpotter")
				.name("Harry Potter")
				.salary(BigDecimal.valueOf(1234.00))
				.startDate(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
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
				("id,login,name,salary,startDate\n" +
						"e0001,hpotter,Harry Potter,1234.00,16-Nov-01\n" +
						"e0002,rwesley,Ron Weasley,19234.50,2001-11-16").getBytes());
		
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
				.salary(BigDecimal.valueOf(19234.50))
				.startDate(LocalDate.parse("2001-11-16", DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_YYYY_MM_DD)))
				.build();
		
		List<User> result = subject.csvToUsers(file);
		
		assertThat(result).isNotNull();
		assertThat(result).hasSize(2);
		assertThat(result).contains(user1);
		assertThat(result).contains(user2);
	}
	
	@Test
	void csvToUsers_CsvHasMissingId_ThrowInvalidCsvException() throws IOException, ParseException {
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"test.txt",
				"text/plain",
				("id,login,name,salary,startDate\n" +
						",hpotter,Harry Potter,1234.00,16-Nov-01\n" +
						"e0002,rwesley,Ron Weasley,19234.50,2001-11-16").getBytes());
		
		assertThatThrownBy(() -> {
			subject.csvToUsers(file);
		}).isInstanceOf(InvalidCsvException.class)
				.hasMessage(InvalidCsvException.missingField().getMessage());
	}
	
	@Test
	void csvToUsers_CsvHasMissingLogin_ThrowInvalidCsvException() throws IOException, ParseException {
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"test.txt",
				"text/plain",
				("id,login,name,salary,startDate\n" +
						"e0001,,Harry Potter,1234.00,16-Nov-01\n" +
						"e0002,rwesley,Ron Weasley,19234.50,2001-11-16").getBytes());
		
		assertThatThrownBy(() -> {
			subject.csvToUsers(file);
		}).isInstanceOf(InvalidCsvException.class)
				.hasMessage(InvalidCsvException.missingField().getMessage());
	}
	
	@Test
	void csvToUsers_CsvHasMissingName_ThrowInvalidCsvException() throws IOException, ParseException {
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"test.txt",
				"text/plain",
				("id,login,name,salary,startDate\n" +
						"e0001,hpotter,,1234.00,16-Nov-01\n" +
						"e0002,rwesley,Ron Weasley,19234.50,2001-11-16").getBytes());
		
		assertThatThrownBy(() -> {
			subject.csvToUsers(file);
		}).isInstanceOf(InvalidCsvException.class)
				.hasMessage(InvalidCsvException.missingField().getMessage());
	}
	
	@Test
	void csvToUsers_CsvHasMissingSalary_ThrowInvalidCsvException() throws IOException, ParseException {
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"test.txt",
				"text/plain",
				("id,login,name,salary,startDate\n" +
						"e0001,hpotter,Harry Potter,,16-Nov-01\n" +
						"e0002,rwesley,Ron Weasley,19234.50,2001-11-16").getBytes());
		
		assertThatThrownBy(() -> {
			subject.csvToUsers(file);
		}).isInstanceOf(InvalidCsvException.class)
				.hasMessage(InvalidCsvException.missingField().getMessage());
	}
	
	@Test
	void csvToUsers_CsvHasMissingStartDate_ThrowInvalidCsvException() throws IOException, ParseException {
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"test.txt",
				"text/plain",
				("id,login,name,salary,startDate\n" +
						"e0001,hpotter,Harry Potter,1234.00,\n" +
						"e0002,rwesley,Ron Weasley,19234.50,2001-11-16").getBytes());
		
		assertThatThrownBy(() -> {
			subject.csvToUsers(file);
		}).isInstanceOf(InvalidCsvException.class)
				.hasMessage(InvalidCsvException.missingField().getMessage());
	}
	
	@Test
	void csvToUsers_CsvHasNegativeSalary_ThrowInvalidCsvException() throws IOException, ParseException {
		MockMultipartFile file = new MockMultipartFile(
				"file", "test.txt", "text/plain",
				("id,login,name,salary,startDate\n" +
						"e0001,hpotter,Harry Potter,1234.00,16-Nov-01\n" +
						"e0002,rwesley,Ron Weasley,-19234.50,2001-11-16").getBytes());
		
		assertThatThrownBy(() -> {
			subject.csvToUsers(file);
		}).isInstanceOf(InvalidCsvException.class)
				.hasMessage(InvalidCsvException.invalidFieldValue().getMessage());
	}
	
	@Test
	void csvToUsers_RowStartsWithHex_IgnoreRow() throws IOException, ParseException {
		MockMultipartFile file = new MockMultipartFile(
				"file", "test.txt", "text/plain",
				("id,login,name,salary,startDate\n" +
						"#e0001,hpotter,Harry Potter,1234.00,16-Nov-01\n" +
						"e0002,rwesley,Ron Weasley,19234.50,2001-11-16").getBytes());
		
		User user1 = User.builder()
				.id("#e0001")
				.login("hpotter")
				.name("Harry Potter")
				.salary(BigDecimal.valueOf(1234.00))
				.startDate(LocalDate.parse("16-Nov-01", DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_DD_MMM_YY)))
				.build();
		User user2 = User.builder()
				.id("e0002")
				.login("rwesley")
				.name("Ron Weasley")
				.salary(BigDecimal.valueOf(19234.50))
				.startDate(LocalDate.parse("2001-11-16", DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_YYYY_MM_DD)))
				.build();
		
		List<User> result = subject.csvToUsers(file);
		
		assertThat(result).isNotNull();
		assertThat(result).hasSize(1);
		assertThat(result).doesNotContain(user1);
		assertThat(result).contains(user2);
	}
	
}
