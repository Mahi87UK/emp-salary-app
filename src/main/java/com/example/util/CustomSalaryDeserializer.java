package com.example.util;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class CustomSalaryDeserializer extends StdDeserializer<Double> {

	private static final long serialVersionUID = 1L;

	public CustomSalaryDeserializer() {
		this(null);
	}

	public CustomSalaryDeserializer(Class<?> vc) {
		super(vc);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Double deserialize(JsonParser jsonparser, DeserializationContext context)
			throws IOException, JsonMappingException {
		String value = jsonparser.getText();
		if (StringUtils.isNotEmpty(value) && value.matches("^\\d+\\.\\d+")) {
			Double dblValue = Double.parseDouble(value);
			return dblValue;
		} else {
			throw new JsonMappingException("Invalid salary");
		}
	}
}
