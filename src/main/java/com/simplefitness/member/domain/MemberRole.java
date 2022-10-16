package com.simplefitness.member.domain;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {
	
	USER("ROLE_USER"),
	PREMIUM("ROLE_PREMIUM");
	
	@JsonValue
	private final String display;

}
