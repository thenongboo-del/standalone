package com.sbproject.standalone.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConditionException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String condition;
	
	public ConditionException(String condition) {
		this.condition = condition;
	}
}
