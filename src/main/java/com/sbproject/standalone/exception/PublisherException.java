package com.sbproject.standalone.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublisherException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String publisher;
	
	public PublisherException(String publisher) {
		this.publisher = publisher;
	}
	
}
