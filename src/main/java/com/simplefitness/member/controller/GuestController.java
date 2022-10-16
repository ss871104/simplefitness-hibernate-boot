package com.simplefitness.member.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simplefitness.member.domain.MemberBean;
import com.simplefitness.member.service.impl.MemberServiceImpl;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/guest")
public class GuestController {

	@Autowired
	private MemberServiceImpl service;

	@PostMapping("")
	public ResponseEntity<Object> register(@RequestBody MemberBean member, HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException {

		return new ResponseEntity<>(service.register(member, getSiteURL(request)), HttpStatus.CREATED);
	}

	private String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}

	@PostMapping("/forgetPassword")
	public ResponseEntity<Object> forgetPassword(@RequestBody MemberBean member, HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException {

		return new ResponseEntity<>(service.getForForgetPassword(member, getSiteURL(request)), HttpStatus.CREATED);
	}

	@PostMapping("/changeForgetPassword")
	public ResponseEntity<Object> changeForgetPassword(@RequestBody MemberBean member) {

		return new ResponseEntity<>(service.memberEditPasswordByCode(member), HttpStatus.ACCEPTED);
	}

	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody MemberBean member) {
		System.out.println("loginContoller");

		return new ResponseEntity<>(HttpStatus.OK);

	}

}
