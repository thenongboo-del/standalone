package com.sbproject.standalone.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private String category;
	
	public CategoryException(String category) {
		this.category = category;
	}

}
