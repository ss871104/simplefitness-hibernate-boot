package com.simplefitness.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.simplefitness.member.service.impl.MemberServiceImpl;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/guest")
public class RoutingController {

	@Autowired
	private MemberServiceImpl memberService;

	@GetMapping("/verify")
	public String verifyMember(@Param("code") String code) {
		if (memberService.verify(code)) {
			return "your success path";
		} else {
			return "your fail path";
		}
	}

	@GetMapping("/changeForgetPassword")
	public String changeForgetPassword(@Param("code") String code) {
		if (memberService.navigateToChangePassword(code)) {
			return "your success path?code=" + code;
		} else {
			return "your fail path";
		}
	}

}
