package com.simplefitness.security.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		System.out.println("fail");
		
		if (exception instanceof BadCredentialsException) {
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

		    final Map<String, Object> body = new HashMap<>();
		    body.put("status", HttpServletResponse.SC_BAD_REQUEST);
		    body.put("error", "BadCredential");
		    body.put("message", "帳號或密碼輸入錯誤");
		    body.put("path", request.getServletPath());

		    final ObjectMapper mapper = new ObjectMapper();
		    mapper.writeValue(response.getOutputStream(), body);
			
		}
		
	}

}
