package com.simplefitness.common;

import javax.persistence.Transient;

import lombok.Data;

@Data
public class CommonBean {

	@Transient
	private boolean successful;
	@Transient
	private String message;

}
