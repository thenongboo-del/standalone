package com.sbproject.standalone.controller;




import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MainContoller {
	
	// 역할에 따라른 페이지 이동

	// 일반 회원 (Role.USER) - 회원가입 / 로그인 / 구매상담 / 시승신청
	 @GetMapping("/")
	    public String requestMain(HttpServletRequest request, Model model) {
	        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

	        // flashMap에 값이 있으면 명시적으로 모델에 넣어준다
	        if (flashMap != null && flashMap.containsKey("flashMessage")) {
	            Object fm = flashMap.get("flashMessage");
	            model.addAttribute("flashMessage", fm);
	            System.out.println(">>> flashMessage added to model: " + fm);
	        }

	        return "main";
	    }


	

}
