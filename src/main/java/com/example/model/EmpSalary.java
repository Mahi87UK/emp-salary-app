package com.example.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.example.util.CustomDateDeserializer;
import com.example.util.CustomSalaryDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Entity
@Table(name="emp_salary")
public class EmpSalary {
	
	@Id
	@NotBlank(message = "Id field is required")
	private String id;
	

	@Column(unique=true)
	@NotBlank(message = "Login field is required")
	private String login;
	
	@Column
	@NotBlank(message = "Name field is required")
	private String name;
	
	@Column
	@DecimalMin("0")
	@NotNull(message = "Salary field is required")
	@JsonDeserialize (using = CustomSalaryDeserializer.class)
	private double salary;
	
	@Column
	@NotNull(message = "StartDate field is required")
	@JsonDeserialize(using= CustomDateDeserializer.class)
	private LocalDate startDate;
	
	public EmpSalary() {
		super();
	}

	public EmpSalary(String id, String login, String name, double salary, LocalDate startDate) {
		super();
		this.id = id;
		this.login = login;
		this.name = name;
		this.salary = salary;
		this.startDate = startDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	@Override
	public String toString() {
		return "EmployeeSalary [id= "+id+" login=" +login+ "name="+name+" salary="+salary+" startDate="+startDate+"]";
	}
	
	
	

}
