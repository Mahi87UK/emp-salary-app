package com.example.util;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class DoubleConverter extends AbstractBeanField<Object, Object> {
	
	private String format = "^\\d+\\.\\d+";
	
    @Override
    protected Object convert(String s) throws CsvDataTypeMismatchException {
    	
    		if(s.matches(format)) {
    			Double value = Double.parseDouble(s);
    			if (value >= 0) {
    				return value;
    			}else {
    				throw new CsvDataTypeMismatchException(field.getName()+" field should be minimum 0");
    			}
    		}
    	  
    		throw new CsvDataTypeMismatchException("Invalid "+field.getName());
    	
    }
}
