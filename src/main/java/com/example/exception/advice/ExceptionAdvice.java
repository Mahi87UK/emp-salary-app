package com.example.exception.advice;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.exception.FileProcessingException;
import com.example.exception.InvalidRequestException;
import com.example.pojo.ResponseMessage;
import com.example.util.ResponseGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.opencsv.exceptions.CsvException;

@Controller
@RestControllerAdvice
public class ExceptionAdvice {

	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<ResponseMessage> handleInvalidRequestException(InvalidRequestException e) {
		return ResponseGenerator.buildResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = FileProcessingException.class)
	public ResponseEntity<ResponseMessage> handleFileProcessingException(FileProcessingException e) {
		return ResponseGenerator.buildResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ResponseMessage> handleHttpException(HttpRequestMethodNotSupportedException e) {
		return ResponseGenerator.buildResponse("Invalid request", HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = CsvException.class)
	public ResponseEntity<ResponseMessage> handleCsvException(CsvException e) {
		return ResponseGenerator.buildResponse(e.getMessage() + ">>>>> Failed Row " + Arrays.toString(e.getLine()),
				HttpStatus.BAD_REQUEST);
	}

	// TODO can be removed
	@ExceptionHandler(value = DataIntegrityViolationException.class)
	public ResponseEntity<ResponseMessage> handleUniqueConstraintException(DataIntegrityViolationException e) {
		return ResponseGenerator.buildResponse("Record is not unique", HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<ResponseMessage> handleMethodArguumentException(MethodArgumentNotValidException e) {
		return ResponseGenerator.buildResponse(processFieldErrors(e.getFieldErrors()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = BindException.class)
	public ResponseEntity<ResponseMessage> handleDataBindException(BindException e) {
		return ResponseGenerator.buildResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = JsonProcessingException.class)
	public ResponseEntity<ResponseMessage> handleJsonMappingException(JsonProcessingException e) {

		return ResponseGenerator.buildResponse(e.getOriginalMessage(), HttpStatus.BAD_REQUEST);
	}

	private String processFieldErrors(List<FieldError> fieldErrors) {
		StringJoiner joiner = new StringJoiner(",");
		for (FieldError fieldError : fieldErrors) {
			joiner.add(fieldError.getField() + " - " + fieldError.getDefaultMessage());
		}
		return joiner.toString();
	}
}
