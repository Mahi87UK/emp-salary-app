package com.example.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.example.constants.Constants;

public class DateConverter extends AbstractBeanField<Object, Object> {
	
		
    @Override
    protected Object convert(String s) throws CsvDataTypeMismatchException {
    	try {
    	   DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.SUPPORTED_DATE_FORMATS);
    	   LocalDate date = LocalDate.parse(s, formatter);
	       return date;
    	}
    	catch(DateTimeParseException e) {
    		//TODO - make it configurable msg
    		throw new CsvDataTypeMismatchException("Invalid date value.Supported formats are "+Constants.SUPPORTED_DATE_FORMATS+". Please correct the "+field.getName() +" field with value "+s + " to the supported format.");
    	}

    }
}