package com.sbproject.standalone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
	
	// 로그인 화면
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	// 로그인 처리
	// - 스프링 시큐리티가 처리
	// - SecurityConfig 설정과 UserDetailsService 인터페이스를 구현한 클래스에서 처리하도록 설정함.
	
	// 로그인 실패 처리
	@GetMapping("/loginfailed")
	public String loginError(Model model) {
		model.addAttribute("failToLogin","아이디가 존재하지 않거나, 비밀번호가 맞지 않습니다.");
		model.addAttribute("error", "true");
		return "login";
	}
	
	// 로그아웃 처리 
	// - 스프링 시큐리티가 세션을 자동으로 삭제
	@GetMapping("/logout")
	public String logout() {
		return "login";
	}
	
}
