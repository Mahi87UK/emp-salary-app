package com.example.pojo;

import java.io.Serializable;
import java.time.LocalDate;

import com.example.util.DateConverter;
import com.example.util.DoubleConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.CsvDate;

public class EmpSalaryPojo implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@CsvBindByName(required = true)
	private String id;

	@CsvBindByName(required = true)
	private String login;

	@CsvBindByName(required = true)
	private String name;

	@CsvDate
	@CsvCustomBindByName(required = true, converter = DateConverter.class)
	private LocalDate startDate;

	@CsvCustomBindByName(required = true, converter = DoubleConverter.class)
	private double salary;

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

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public EmpSalaryPojo(String id, String login, String name, LocalDate startDate, double salary) {
		super();
		this.id = id;
		this.login = login;
		this.name = name;
		this.startDate = startDate;
		this.salary = salary;
	}

	public EmpSalaryPojo() {
		super();
	}
	
	
}
