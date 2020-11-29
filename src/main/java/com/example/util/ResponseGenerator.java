package com.example.util;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.pojo.ResponseMessage;

public class ResponseGenerator {

	public static ResponseMessage constructResponseMessage(List<? extends Object> results,String msg) {
		ResponseMessage responseMessage =  new ResponseMessage(results,msg);
		return responseMessage;
	}
	
	
	public static ResponseEntity<ResponseMessage> buildResponse(String msg , HttpStatus httpStatus){
		return new ResponseEntity<ResponseMessage>(constructResponseMessage(null,msg), httpStatus);
	}
	
	public static ResponseEntity<ResponseMessage> buildResponse(List<? extends Object> results,String msg , HttpStatus httpStatus){
		return new ResponseEntity<ResponseMessage>(constructResponseMessage(results,msg), httpStatus);
	}
	
	public static ResponseEntity<ResponseMessage> buildResponseWithHttpStatusOnly(HttpStatus httpStatus){
		return new ResponseEntity<ResponseMessage>(httpStatus);
	}
	
}
