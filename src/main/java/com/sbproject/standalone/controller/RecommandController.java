package com.sbproject.standalone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/recommand")
public class RecommandController {

	
	@GetMapping("/form")
	private String requestRecommandForm(Model model) {
		return "recommand/recommandForm";
	}
	
	
	
	
	
	
	
}
