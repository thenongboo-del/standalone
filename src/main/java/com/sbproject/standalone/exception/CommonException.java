package com.sbproject.standalone.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

// 모든 예외 발생시에 공통으로 사용하는 예외 클래스
// @ControllerAdvice : 컨트롤러에 전달되는 모든 예외에 공통으로 사용되는 전역(공용) 클래스
@ControllerAdvice
public class CommonException {
	// @ExceptionHandler : throw로 던지 예외를 받아서 처리하는 애노테이션
	
	// 공용 예외처리 메서드 : RuntimeException
	@ExceptionHandler(RuntimeException.class)
	private ModelAndView handleErrorCommon(Exception exception) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", exception);
		mav.setViewName("error/errorCommon");
		return mav;
	}
	
	// 전용 예외처리 메서드 : BookIdException
	@ExceptionHandler(BookIdException.class)
	public ModelAndView handleError(HttpServletRequest request, BookIdException exception) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("fieldName", "도서ID");
		mav.addObject("invalidParam", exception.getBookId());
		mav.addObject("exception", exception);
		mav.addObject("url", request.getRequestURL() + "?" + request.getQueryString());
		mav.setViewName("error/errorBook");
		return mav;
	}
	
	// 전용 예외처리 메서드 : CategoryException
	@ExceptionHandler(CategoryException.class)
	public ModelAndView handleError(HttpServletRequest request, CategoryException exception) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("fieldName", "도서분류");
		mav.addObject("invalidParam", exception.getCategory());
		mav.addObject("exception", exception);
		mav.addObject("url", request.getRequestURL() + "?" + request.getQueryString());
		mav.setViewName("error/errorBook");
		return mav;
	}
	
	// 전용 예외처리 메서드 : ConditionException
	@ExceptionHandler(ConditionException.class)
	public ModelAndView handleError(HttpServletRequest request, ConditionException exception) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("fieldName", "도서상태");
		mav.addObject("invalidParam", exception.getCondition());
		mav.addObject("exception", exception);
		mav.addObject("url", request.getRequestURL() + "?" + request.getQueryString());
		mav.setViewName("error/errorBook");
		return mav;
	}
	
	// 전용 예외처리 메서드 : AuthorException
	@ExceptionHandler(AuthorException.class)
	public ModelAndView handleError(HttpServletRequest request, AuthorException exception) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("fieldName", "저자");
		mav.addObject("invalidParam", exception.getAuthor());
		mav.addObject("exception", exception);
		mav.addObject("url", request.getRequestURL() + "?" + request.getQueryString());
		mav.setViewName("error/errorBook");
		return mav;
	}
	
	// 전용 예외처리 메서드 : PublisherException
	@ExceptionHandler(PublisherException.class)
	public ModelAndView handleError(HttpServletRequest request, PublisherException exception) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("fieldName", "출판사");
		mav.addObject("invalidParam", exception.getPublisher());
		mav.addObject("exception", exception);
		mav.addObject("url", request.getRequestURL() + "?" + request.getQueryString());
		mav.setViewName("error/errorBook");
		return mav;
	}

}
