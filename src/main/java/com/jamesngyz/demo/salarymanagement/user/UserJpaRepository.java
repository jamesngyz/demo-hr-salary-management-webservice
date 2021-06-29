package com.jamesngyz.demo.salarymanagement.user;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, String> {
	
	@Query("select e from employee e where e.salary >= :minInclusive and e.salary < :maxExclusive")
	List<User> findBySalaryMinInclusiveAndMaxExclusive(@Param("minInclusive") BigDecimal minInclusive,
			@Param("maxExclusive") BigDecimal maxExclusive,
			Pageable pageable);
	
}
