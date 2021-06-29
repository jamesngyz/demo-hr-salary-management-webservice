package com.jamesngyz.demo.salarymanagement.user;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, UserJdbcRepository {
	
	@Query("select e from employee e where e.salary >= :minInclusive and e.salary < :maxExclusive")
	List<User> findBySalaryMinInclusiveAndMaxExclusive(@Param("minInclusive") BigDecimal minInclusive,
			@Param("maxExclusive") BigDecimal maxExclusive,
			Pageable pageable);
	
	@Transactional
	@Query("update employee e set e.login = :login, e.name = :name, e.salary = :salary, e.startDate = :startDate " +
			"where e.id = :id")
	@Modifying
	Integer updateUserById(@Param("id") String id,
			@Param("login") String login,
			@Param("name") String name,
			@Param("salary") BigDecimal salary,
			@Param("startDate") LocalDate startDate);
	
}
