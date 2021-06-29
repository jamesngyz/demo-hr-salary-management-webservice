package com.jamesngyz.demo.salarymanagement.user;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringJoiner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserRepository {
	
	private final NamedParameterJdbcTemplate jdbcTemplate;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public UserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public Integer createOrUpdate(List<User> request) {
		String query = "INSERT INTO employee (id, login, name, salary, start_date) VALUES \n";
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		StringJoiner rowsJoiner = new StringJoiner(",\n");
		for (int i = 0; i < request.size(); i++) {
			String idParam = "id" + i;
			String loginParam = "login" + i;
			String nameParam = "name" + i;
			String salaryParam = "salary" + i;
			String startDateParam = "startDate" + i;
			
			StringJoiner fieldsJoiner = new StringJoiner(",:", "(:", ")");
			fieldsJoiner.add(idParam)
					.add(loginParam)
					.add(nameParam)
					.add(salaryParam)
					.add(startDateParam);
			rowsJoiner.add(fieldsJoiner.toString());
			
			User u = request.get(i);
			parameters.addValue(idParam, u.getId())
					.addValue(loginParam, u.getLogin())
					.addValue(nameParam, u.getName())
					.addValue(salaryParam, u.getSalary().toString())
					.addValue(startDateParam, u.getStartDate().format(DateTimeFormatter.ISO_DATE));
		}
		query = query + rowsJoiner.toString() +
				" \nON DUPLICATE KEY UPDATE " +
				" id=VALUES(id), login=VALUES(login), name=VALUES(name), salary=VALUES(salary), start_date=VALUES(start_date);";
		
		return jdbcTemplate.update(query, parameters);
	}
	
	@Transactional
	public void create(User user) {
		entityManager.persist(user);
	}
	
}
