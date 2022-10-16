package com.simplefitness.member.repository;

import java.util.List;

import com.simplefitness.member.domain.MemberBean;

public interface MemberRepository {

	public boolean insert(MemberBean member);

	public boolean update(MemberBean member);

	public boolean delete(Integer memId);

	public MemberBean selectById(Integer memId);

	public List<MemberBean> selectAll();

	public MemberBean selectByUsername(String username);

	public MemberBean selectByEmail(String email);

	public MemberBean selectByUsernameAndPassword(String username, String password);

	public MemberBean selectByUsernameAndEmail(String username, String email);

	public MemberBean selectEncodePasswordByVerificationCode(String username);

	public boolean updatePasswordById(String newPassword, Integer memId);

	public MemberBean findByVerificationCode(String code);

	public boolean updateVerifiedByEmail(MemberBean member);

	public boolean updateVerificationCode(MemberBean member);

	boolean updatePasswordByVerificationCode(MemberBean member);

}
