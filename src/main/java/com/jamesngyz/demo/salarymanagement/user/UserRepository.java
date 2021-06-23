package com.jamesngyz.demo.salarymanagement.user;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.StringJoiner;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.jamesngyz.demo.salarymanagement.Constants;

@Repository
public class UserRepository {
	
	private JdbcTemplate jdbcTemplate;
	
	public UserRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	Integer createOrUpdate(List<User> request) {
		String query = "INSERT INTO employee (id, login, name, salary, start_date) VALUES \n";
		StringJoiner rowsJoiner = new StringJoiner(",\n");
		for (User u : request) {
			StringJoiner fieldsJoiner = new StringJoiner("','", "('", "')");
			fieldsJoiner
					.add(u.getId())
					.add(u.getLogin())
					.add(u.getName())
					.add(u.getSalary().toString())
					.add(new SimpleDateFormat(Constants.DATE_FORMAT_SQL).format(u.getStartDate()));
			rowsJoiner.add(fieldsJoiner.toString());
		}
		query = query + rowsJoiner.toString() +
				" ON DUPLICATE KEY UPDATE " +
				" id=VALUES(id), login=VALUES(login), name=VALUES(name), salary=VALUES(salary), start_date=VALUES(start_date);";
		
		// TODO: For POC purposes only. Will change this to parameter binding.
		return jdbcTemplate.update(query);
	}
	
}
