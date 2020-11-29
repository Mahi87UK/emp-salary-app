package com.example.exception.advice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import com.example.exception.FileProcessingException;
import com.example.exception.InvalidRequestException;
import com.example.pojo.ResponseMessage;
import com.opencsv.exceptions.CsvException;

@ExtendWith(SpringExtension.class)
public class ExceptionAdviceTest {

	@InjectMocks
	private ExceptionAdvice underTest;
	
	
	
	@Test
	public void testInvalidRequestException() {
		ResponseEntity<ResponseMessage> responseEntity = underTest
				.handleInvalidRequestException(new InvalidRequestException("Invalid request"));
		assertResult(responseEntity,"Invalid request");
		

	}
	
	@Test
	public void testFileProcessingException() {
		ResponseEntity<ResponseMessage> responseEntity = underTest
				.handleFileProcessingException(new FileProcessingException("Invalid file"));
		assertResult(responseEntity,"Invalid file");

	}
	
	@Test
	public void testInvalidHttpRequestException() {
		ResponseEntity<ResponseMessage> responseEntity = underTest
				.handleHttpException(new HttpRequestMethodNotSupportedException("Invalid http method"));
		assertResult(responseEntity,"Invalid request");

	}
	
	@Test
	public void testInvalidCsvException() {
		ResponseEntity<ResponseMessage> responseEntity = underTest
				.handleCsvException(new CsvException("Invalid csv"));
		assertResult(responseEntity,"Invalid csv>>>>> Failed Row null");

	}
	
	@Test
	public void testDataIntegrityException() {
		ResponseEntity<ResponseMessage> responseEntity = underTest
				.handleUniqueConstraintException(new DataIntegrityViolationException("failed"));
		assertResult(responseEntity,"Record is not unique");

	}
	
	
	
	private void assertResult(ResponseEntity<ResponseMessage> responseEntity,String expectedMsg) {
		assertNotNull(responseEntity);
		ResponseMessage responseMsg = responseEntity.getBody();
		assertNotNull(responseMsg);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals(expectedMsg, responseMsg.getMessage());
	}
}
