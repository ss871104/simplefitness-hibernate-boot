package com.simplefitness.member.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simplefitness.member.domain.MemberBean;
import com.simplefitness.member.service.impl.MemberServiceImpl;
import com.simplefitness.security.jwt.JwtUtils;

@RestController
@PreAuthorize("hasAnyRole('USER', 'PREMIUM')")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/member")
public class MemberController {

	@Autowired
	private MemberServiceImpl service;
	@Autowired
	private JwtUtils jwtUtils;

	@GetMapping("/authentication")
	public ResponseEntity<Object> authentication() {
		final Map<String, Object> body = new HashMap<>();
		body.put("status", HttpServletResponse.SC_OK);

		return new ResponseEntity<Object>(body, HttpStatus.OK);
	}
	
	@GetMapping("/{token}")
	public ResponseEntity<Object> getMemberInfo(@PathVariable @RequestHeader(value="Authorization") String token) {
		token = token.substring(7, token.length());
		String username = jwtUtils.getUserNameFromJwtToken(token);

		return new ResponseEntity<>(service.getByUsername(username), HttpStatus.OK);
	}

	@PutMapping("/updateInfo")
	public ResponseEntity<Object> updatetUser(@RequestBody MemberBean member, @RequestHeader(value="Authorization") String token) {
		token = token.substring(7, token.length());
		String username = jwtUtils.getUserNameFromJwtToken(token);
		member.setUsername(username);

		return new ResponseEntity<Object>(service.memberEditInfo(member), HttpStatus.ACCEPTED);
	}

}
