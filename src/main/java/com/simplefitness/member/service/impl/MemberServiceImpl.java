package com.simplefitness.member.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.simplefitness.member.domain.MemberBean;
import com.simplefitness.member.repository.MemberRepository;
import com.simplefitness.member.service.MemberService;

import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberBean result;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private PasswordEncoder bcryptPasswordEncoder;
	@Autowired
	private JavaMailSender mailSender;

	@Override
	public MemberBean register(MemberBean member, String siteURL) {
		try {
			if ("".equals(member.getUsername())) {
				result.setMessage("帳號未輸入");
				result.setSuccessful(false);
				return result;
			}
			if ("".equals(member.getEmail())) {
				result.setMessage("信箱未輸入");
				result.setSuccessful(false);
				return result;
			}
			if (member.getEmail().contains("@") == false) {
				result.setMessage("未符合信箱格式");
				result.setSuccessful(false);
				return result;
			}
			if ("".equals(member.getName())) {
				result.setMessage("名稱未輸入");
				result.setSuccessful(false);
				return result;
			}
			if ("".equals(member.getPassword())) {
				result.setMessage("密碼未輸入");
				result.setSuccessful(false);
				return result;
			}
			if (member.getGender() == null) {
				result.setMessage("性別未選");
				result.setSuccessful(false);
				return result;
			}
			if (memberRepository.selectByUsername(member.getUsername()) != null) {
				result.setMessage("此帳號已被註冊");
				result.setSuccessful(false);
				return result;
			}
			if (memberRepository.selectByEmail(member.getEmail()) != null) {
				result.setMessage("此信箱已被註冊");
				result.setSuccessful(false);
				return result;
			}
			// password encode
			member.setPassword(bcryptPasswordEncoder.encode(member.getPassword()));
			// set verification code and verified false
			String randomCode = RandomString.make(64);
			member.setVerificationCode(randomCode);
			member.setVerified(false);

			if (memberRepository.insert(member) == false) {
				result.setMessage("註冊錯誤，請聯絡管理員!");
				result.setSuccessful(false);
				return result;
			}

			sendVerificationEmail(member, siteURL);

			result.setSuccessful(true);
			return result;
		} catch (AddressException e) {
			result.setMessage("無此信箱");
			result.setSuccessful(false);
			// manually rollback
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public MemberBean login(String username, String password) {
		try {
			if ("".equals(username)) {
				result.setMessage("帳號未輸入");
				result.setSuccessful(false);
				return result;
			}
			if ("".equals(password)) {
				result.setMessage("密碼未輸入");
				result.setSuccessful(false);
				return result;
			}
			result.setSuccessful(true);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public MemberBean getByUsername(String username) {
		try {
			result = memberRepository.selectByUsername(username);
			if (result == null) {
				return result;
			}

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public MemberBean getForForgetPassword(MemberBean member, String siteURL) {
		try {
			if ("".equals(member.getUsername())) {
				result.setMessage("帳號未輸入");
				result.setSuccessful(false);
				return result;
			}
			if ("".equals(member.getEmail())) {
				result.setMessage("信箱未輸入");
				result.setSuccessful(false);
				return result;
			}
			if (member.getEmail().contains("@") == false) {
				result.setMessage("未符合信箱格式");
				result.setSuccessful(false);
				return result;
			}
			member = memberRepository.selectByUsernameAndEmail(member.getUsername(), member.getEmail());
			if (member == null) {
				result.setMessage("帳號或驗證碼輸入錯誤");
				result.setSuccessful(false);
				return result;
			} else if (member.isVerified() == false) {
				result.setMessage("信箱尚未驗證，無法使用忘記密碼功能！");
				result.setSuccessful(false);
				return result;
			}
			String randomCode = RandomString.make(64);
			member.setVerificationCode(randomCode);
			memberRepository.updateVerificationCode(member);

			sendForgetPasswordEmail(member, siteURL);

			result.setSuccessful(true);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public MemberBean getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MemberBean memberEditInfo(MemberBean member) {
		try {
			if (memberRepository.update(member) == false) {
				result.setMessage("資料更改出現錯誤，請聯絡管理員!");
				result.setSuccessful(false);
				return result;
			}

			result.setSuccessful(true);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public MemberBean memberEditPassword(Integer id, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MemberBean memberEditPasswordByCode(MemberBean member) {
		try {
			if ("".equals(member.getNewPassword())) {
				result.setMessage("新密碼未輸入");
				result.setSuccessful(false);
				return result;
			}
			if (bcryptPasswordEncoder.matches(member.getNewPassword(), memberRepository.selectEncodePasswordByVerificationCode(member.getVerificationCode()).getPassword())) {
				result.setMessage("新密碼與舊密碼相同！");
				result.setSuccessful(false);
				return result;
			}
			// password encode
			member.setPassword(bcryptPasswordEncoder.encode(member.getNewPassword()));

			if (memberRepository.updatePasswordByVerificationCode(member) == false) {
				result.setMessage("密碼更改出現錯誤，請聯絡管理員!");
				result.setSuccessful(false);
				return result;
			}

			result.setSuccessful(true);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void sendVerificationEmail(MemberBean member, String siteURL)
			throws MessagingException, UnsupportedEncodingException {
		String toAddress = member.getEmail();
		String fromAddress = "your email";
		String senderName = "Simple Fitness";
		String subject = "Please verify your registration";
		String content = "Dear [[name]],<br>" + "Please click the link below to verify your registration:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>" + "Simple Fitness.";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(fromAddress, senderName);
		helper.setTo(toAddress);
		helper.setSubject(subject);

		content = content.replace("[[name]]", member.getName());
		String verifyURL = siteURL + "/guest/verify?code=" + member.getVerificationCode();

		content = content.replace("[[URL]]", verifyURL);

		helper.setText(content, true);

		ExecutorService executor = Executors.newWorkStealingPool();

		executor.execute(() -> mailSender.send(message));
	}

	@Override
	public boolean verify(String verificationCode) {
		MemberBean member = memberRepository.findByVerificationCode(verificationCode);

		if (member == null || member.isVerified()) {
			return false;
		} else {
			member.setVerificationCode(null);
			member.setVerified(true);

			memberRepository.updateVerifiedByEmail(member);

			return true;
		}
	}

	private void sendForgetPasswordEmail(MemberBean member, String siteURL)
			throws MessagingException, UnsupportedEncodingException {
		String toAddress = member.getEmail();
		String fromAddress = "your email";
		String senderName = "Simple Fitness";
		String subject = "Forget Password";
		String content = "Dear [[name]],<br>" + "Please click the link below to navigate to changing password site:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">CHANGE PASSWORD</a></h3>" + "Thank you,<br>"
				+ "Simple Fitness.";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(fromAddress, senderName);
		helper.setTo(toAddress);
		helper.setSubject(subject);

		content = content.replace("[[name]]", member.getName());
		String verifyURL = siteURL + "/guest/changeForgetPassword?code=" + member.getVerificationCode();

		content = content.replace("[[URL]]", verifyURL);

		helper.setText(content, true);

		ExecutorService executor = Executors.newWorkStealingPool();

		executor.execute(() -> mailSender.send(message));
	}

	@Override
	public boolean navigateToChangePassword(String verificationCode) {
		MemberBean member = memberRepository.findByVerificationCode(verificationCode);

		if (member == null) {
			return false;
		} else {

			return true;
		}
	}

}
