package com.sbproject.standalone.exception;

import lombok.Getter;
import lombok.Setter;

// 도서 ID(bookId)가 없을 때 발생하는 예외 클래스 -> 전용 클래스
@Getter
@Setter
public class BookIdException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String bookId;
	
	public BookIdException(String bookId) {
		this.bookId = bookId;
	}
}
