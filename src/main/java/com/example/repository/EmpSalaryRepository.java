package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.model.EmpSalary;

public interface EmpSalaryRepository extends JpaRepository<EmpSalary, String>, JpaSpecificationExecutor<EmpSalary> {
	
	@Query("SELECT count(e.login) > 0 from EmpSalary e where e.login = ?1 and e.id !=?2")
	boolean existsByLoginNotById(String login,String id);
}
