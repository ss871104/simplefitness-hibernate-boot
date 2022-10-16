package com.simplefitness.member.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.simplefitness.common.CommonBean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@Data
@EqualsAndHashCode(callSuper = false)
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Component
public class MemberBean extends CommonBean {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mem_id")
	private Integer memId;
	@Column(name = "mem_name")
	private String name;
	@Column(insertable = false)
	private String nickname;
	@Column(updatable = false)
	private String username;
	@Column(name = "mem_password")
	private String password;
	@Column(insertable = false)
	private String phone;
	private String email;
	@Column(name = "verification_code", length = 64)
	private String verificationCode;
	private boolean verified;
	@Enumerated(EnumType.ORDINAL)
	@Column(updatable = false)
	private MemberGender gender;
	@Column(insertable = false)
	private LocalDate birth;
	@Column(name = "register_date", insertable = false, updatable = false)
	private LocalDate registerDate;
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "mem_role", insertable = false)
	private MemberRole role;
	@Lob
	@Column(insertable = false)
	private byte[] pic;
	@Transient
	private String newPassword;
	@Transient
	private String token;

	public MemberBean(String password) {
		super();
		this.password = password;
	}

}
