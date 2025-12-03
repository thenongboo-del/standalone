package com.sbproject.standalone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sbproject.standalone.entity.Consultation;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/consultation")
public class ConsultationController {

	// 상담 메인화면
	@GetMapping("/main")
	public String requestConsultationMain(Model model) {
		
		return "consultation/consultation-customer";
	}
	
	
	@GetMapping("/buy/form")
	public String requestConsultationBuyView(Model model) {
		
		model.addAttribute("consultation", new Consultation());
		
		return "consultation/buyForm";
	}
	
	
	@GetMapping("/drive/form")
	public String requestConsultationDriveView(Model model) {
		
		return "consultation/driveForm";
	}
	
	
}
