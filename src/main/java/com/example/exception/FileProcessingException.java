package com.example.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileProcessingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FileProcessingException(String message) {
		super(message);
		log.error("FileProcessingException {}",message);
	}

}
