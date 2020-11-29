package com.example.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvalidRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidRequestException(String message) {
		super(message);
		log.error("InvalidRequestException {}",message);
	}


}
