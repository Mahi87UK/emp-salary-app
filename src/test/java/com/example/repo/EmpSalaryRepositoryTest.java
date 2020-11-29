package com.example.repo;



import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.model.EmpSalary;
import com.example.repository.EmpSalaryRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class EmpSalaryRepositoryTest {
 
    @Autowired
    private TestEntityManager entityManager;
 
    @Autowired
    private EmpSalaryRepository employeeRepository;
 
    @Test
    public void whenRecordExistsByLogin_thenReturnTrue() {
        // given
        EmpSalary empSalaryObj = new EmpSalary("id1","login1","name1",10.0,LocalDate.now());
        entityManager.persist(empSalaryObj);
        entityManager.flush();
     
        // when
        boolean found = employeeRepository.existsByLoginNotById("login1","id");
     
        // then
        assertTrue(found);
    }
    
    @Test
    public void whenRecordDoesNotExistsByLogin_thenReturnFalse() {
        // given
        EmpSalary empSalaryObj = new EmpSalary("id1","login1","name1",10.0,LocalDate.now());
        entityManager.persist(empSalaryObj);
        entityManager.flush();
     
        // when
        boolean found = employeeRepository.existsByLoginNotById("login2","id1");
     
        // then
        assertFalse(found);
    }
 
}
