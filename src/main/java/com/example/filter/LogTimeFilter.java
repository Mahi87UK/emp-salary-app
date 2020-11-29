package com.example.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LogTimeFilter implements Filter {

 
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		long time = System.currentTimeMillis();
		 try {
	            chain.doFilter(request, response);
	        } finally {
	            time = System.currentTimeMillis() - time;
	            log.info("{}: {} ms ", ((HttpServletRequest) request).getRequestURI(),  time);
	        }
       
		
	}
}