package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info =@Info(title = "Employee Salary App",version = "1.0",description = "Employee Salary Management Service"))
public class EmployeeSalaryMgmtServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeSalaryMgmtServiceApplication.class, args);
	}

}
