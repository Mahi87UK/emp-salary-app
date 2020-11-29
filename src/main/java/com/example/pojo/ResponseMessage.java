package com.example.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ResponseMessage {

	public ResponseMessage() {
		
	}
		
	public ResponseMessage(List<? extends Object> results,String message) {
		super();
		this.results = results;
		this.message = message;
	}

	public ResponseMessage(String message) {
		super();
		this.message = message;
	}
	
	@JsonInclude(Include.NON_EMPTY)
	private List<? extends Object> results;
	
	@JsonInclude(Include.NON_EMPTY)
	private String message;
	
	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<? extends Object> getResults() {
		return results;
	}

	public void setResults(List<? extends Object> results) {
		this.results = results;
	}

	
	
	
}
