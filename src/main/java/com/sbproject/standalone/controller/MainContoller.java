package com.sbproject.standalone.controller;




import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MainContoller {
	
	// 역할에 따라른 페이지 이동

	// 일반 회원 (Role.USER) - 회원가입 / 로그인 / 구매상담 / 시승신청
	@GetMapping("/")
	public String requestMain() {
		return "/main";
	}


	

}
