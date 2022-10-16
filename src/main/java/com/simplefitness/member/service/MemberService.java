package com.simplefitness.member.service;

import com.simplefitness.member.domain.MemberBean;

public interface MemberService {

	public MemberBean register(MemberBean member, String siteURL);

	public MemberBean login(String username, String password);

	public MemberBean getForForgetPassword(MemberBean member, String siteURL);

	public MemberBean getAll();

	public MemberBean memberEditInfo(MemberBean member);

	public MemberBean memberEditPassword(Integer id, String password);

	public MemberBean memberEditPasswordByCode(MemberBean member);

	public boolean verify(String verificationCode);

	public boolean navigateToChangePassword(String verificationCode);

	public MemberBean getByUsername(String username);

}
