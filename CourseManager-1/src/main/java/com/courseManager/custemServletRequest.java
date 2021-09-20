package com.courseManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.context.annotation.Configuration;

@Configuration
public class custemServletRequest extends HttpServletRequestWrapper {

	
	
	public custemServletRequest(HttpServletRequest request) {
		super(request);
		// TODO Auto-generated constructor stub
	}
	

}
