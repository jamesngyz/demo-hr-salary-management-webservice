package com.jamesngyz.demo.salarymanagement.user;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jamesngyz.demo.salarymanagement.error.InvalidCsvException;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvBeanIntrospectionException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

@Service
public class UserService {
	
	private UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	/**
	 *
	 * @param users
	 * @return Number of users created/updated. Returns 0 (zero) if all requested
	 *         users already exist and there is no change to field values
	 */
	Integer createOrUpdateUsers(List<User> users) {
		return userRepository.createOrUpdate(users);
	}
	
	List<User> csvToUsers(MultipartFile file) throws IOException {
		
		InputStream stream = removeCommentedLines(file);
		BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream));
		
		try {
			CsvToBean<User> csvToBean = new CsvToBeanBuilder<User>(streamReader)
					.withType(User.class)
					.withIgnoreLeadingWhiteSpace(true)
					.build();
			return csvToBean.parse();
			
		} catch (Exception e) {
			if (isMissingField(e)) {
				throw InvalidCsvException.missingField();
			}
			if (isInvalidField(e)) {
				throw (InvalidCsvException) ((InvocationTargetException) e.getCause().getCause()).getTargetException();
			}
			throw e;
		}
	}
	
	private InputStream removeCommentedLines(MultipartFile file) throws IOException {
		BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream()));
		ByteArrayOutputStream updatedStream = new ByteArrayOutputStream();
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(updatedStream));
		String line;
		while ((line = fileReader.readLine()) != null) {
			if (!line.startsWith("#") && !line.isEmpty()) {
				bufferedWriter.write(line);
				bufferedWriter.newLine();
			}
		}
		bufferedWriter.flush();
		return new ByteArrayInputStream(updatedStream.toByteArray());
	}
	
	private boolean isMissingField(Exception e) {
		return e.getCause() instanceof CsvRequiredFieldEmptyException;
	}
	
	private boolean isInvalidField(Exception e) {
		return e.getCause() instanceof CsvBeanIntrospectionException &&
				e.getCause().getCause() != null && e.getCause().getCause() instanceof InvocationTargetException &&
				((InvocationTargetException) e.getCause().getCause())
						.getTargetException() instanceof InvalidCsvException;
	}
	
}
