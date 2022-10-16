package com.simplefitness.member.domain;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberGender {
	
	MALE("男"),
	FEMALE("女"),
	OTHER("其他");
	
	@JsonValue
	private final String display;

}
