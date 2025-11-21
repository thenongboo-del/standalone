package com.sbproject.standalone.interceptor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.util.StopWatch;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;


// 요청이 컨트롤러에서 동작할 때 가로채서 URL과 날짜시간 정보를 획득하기 위한 클래스
//- Slf4j는 Log4j2 애너테이션의 상위 개념
//- Log4j2는 Slf4j의 구현체
//@Slf4j
@Log4j2
public class MonitoringInterceptor implements HandlerInterceptor {
	// 쓰레드 영역에 변수를 설정하여 특정 쓰레드가 실행되는 모든 코드에서 설정한 변수를 사용할 수 있도록 함.
	ThreadLocal<StopWatch> stopWatchThreadLocal = new ThreadLocal<StopWatch>();

	// 컨트롤러에 요청이 들어가기 전에 실행하는 메서드
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		StopWatch stopWatch = new StopWatch(handler.toString());
		stopWatch.start(handler.toString());
		stopWatchThreadLocal.set(stopWatch);
		log.info("접근한 URL 경로 : " + this.getURLPath(request));
		log.info("요청 처리 시작 시간 : " + this.getCurrentTime());
		return true;
	}

	// 컨트롤러에서 요청에 대한 처리가 끝난 후에 실행하는 메서드 (뷰를 실행하기 전)
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("요청 처리 종료 시간 : " + this.getCurrentTime());
	}

	// 컨트롤러에서 요청에 대한 처리가 끝나고, 뷰로 응답하고 난 후에 실행되는 메서드 (뷰까지 실행이 끝난 후)
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		StopWatch stopWatch = stopWatchThreadLocal.get();
		stopWatch.stop();
		log.info("요청 처리 소요 시간 : " + stopWatch.getTotalTimeMillis() + "ms");
		stopWatchThreadLocal.remove();
		log.info("-------------------------------");
	}
	
	// 요청 URL을 리턴하는 메서드
	private String getURLPath(HttpServletRequest request) throws Exception {
		String currentPath = request.getRequestURI();
		String queryString = request.getQueryString();
		queryString = (queryString == null) ? "" : "?" + queryString;
		return currentPath + queryString;
	}
	
	// 요청 URL을 처리하는 날짜와 시간을 리턴하는 메서드
	private String getCurrentTime() {
		DateFormat fommatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		return fommatter.format(calendar.getTime());
	}
	
}
