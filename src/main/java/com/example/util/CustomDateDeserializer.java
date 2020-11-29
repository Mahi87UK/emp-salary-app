package com.example.util;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.example.constants.Constants;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class CustomDateDeserializer extends StdDeserializer<LocalDate> {

	private static final long serialVersionUID = 1L;

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.SUPPORTED_DATE_FORMATS);

	public CustomDateDeserializer() {
		this(null);
	}

	public CustomDateDeserializer(Class<?> vc) {
		super(vc);
	}

	@SuppressWarnings("deprecation")
	@Override
	public LocalDate deserialize(JsonParser jsonparser, DeserializationContext context) throws JsonMappingException {

		try {
			String date = jsonparser.getText();
			return LocalDate.parse(date, formatter);
		} catch (IOException | DateTimeParseException e) {
			throw new JsonMappingException("Invalid date");
		}
	}
}
