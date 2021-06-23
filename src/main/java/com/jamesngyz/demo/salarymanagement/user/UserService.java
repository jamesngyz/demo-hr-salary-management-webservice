package com.jamesngyz.demo.salarymanagement.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

@Service
public class UserService {
	
	/**
	 *
	 * @param users
	 * @return number of users created/updated
	 * @return 0 (zero) if all requested users already exist, and no change to field
	 *         values
	 */
	Integer createOrUpdateUsers(List<User> users) {
		return null;
	}
	
	List<User> csvToUsers(MultipartFile file) throws IOException {
		Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
		CsvToBean<User> csvToBean = new CsvToBeanBuilder<User>(reader)
				.withType(User.class)
				.withIgnoreLeadingWhiteSpace(true)
				.build();
		List<User> users = csvToBean.parse();
		return users;
	}
	
}
