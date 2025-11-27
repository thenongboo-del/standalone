package com.sbproject.standalone.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sbproject.standalone.entity.Car;
import com.sbproject.standalone.entity.QnaQuestion;
import com.sbproject.standalone.service.SupportService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@Controller
@RequiredArgsConstructor
@RequestMapping("/support")
public class SupportController {
	
	private final SupportService supportService;
	

	@GetMapping("/main")
	public String requestSupportMain() {
		return "support/supportMain";
	}
	
	
	// 페이징 설정
	private int pageSize = 10;	// 페이지당 글 갯수
	private int pageBlock = 5;	// 페이징되어 나타나는 숫자 링크들의 갯수
	
	@GetMapping("/qna/paging")
	public String requestQnaList(
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(name = "searchFor", defaultValue = "") String searchFor,
			@RequestParam(name = "keyword", defaultValue = "") String keyword,
			Model model
			) {
		// 질문 ID로 내림차순 정렬
		Page<QnaQuestion> paging = supportService.getQnaList(pageNum, searchFor, keyword, pageSize, "id", "desc");
		
		// 페이징 처리된 도서 목록 획득
		
		List<QnaQuestion> questionList = paging.getContent();
		
		log.info("paging: {}" + paging.toString());
		
		// 페이지 블럭의 시작번호 & 끝번호 설정
		int startPage = pageNum - (pageNum - 1) % pageBlock;
		int endPage = startPage + pageBlock - 1;
		
		// 페이징 정보를 모델에 저장
		model.addAttribute("paging", paging);						// 페이징정보
		model.addAttribute("questionList", questionList);					// 질문 목록
		model.addAttribute("pageNum", pageNum);						// 현제 페이지 번호
		model.addAttribute("pageBlock", pageBlock);					// 노출 될 페이지 블럭 수 - 5
		model.addAttribute("totalItems", paging.getTotalElements());// 전체 게시물 수
		model.addAttribute("totalPages", paging.getTotalPages());	// 전체 페이지 수 ex) 165개 / 12 = 13.75 -> 14pages
		model.addAttribute("startPage", startPage);					// 블럭의 시작 페이지
		model.addAttribute("endPage", endPage);						// 블럭의 끝 페이지
		model.addAttribute("searchFor", searchFor);							// all, author, publisher, category, carCondition 중의 1개의 값		
		model.addAttribute("keyword", keyword);						// state에서 사용 할 값
		
		
		// 메인의 하단 슬라이더 - 추천상품 (건강 요리 사랑 제목 또는 내용에서 조회해서 5건 씩)
//		List<Car> sliderList2 = this.carService.getCarListSlider("main2");
//		model.addAttribute("sliderList2", sliderList2);
//		sliderList2.forEach(c -> log.info(c.toString()));
		
		return "support/qna";
	}
	
	// Qna게시판 관리자만 댓글을 달 수 있다.
	@GetMapping("/qna")
	public String requestSupportQna(Model model) {
		return requestQnaList(1, "", null, model);
	}	
	
	
	// Qna 게시판 상세페이지
	@GetMapping("/qna/{id}")
	public String requestCarByCarId(@PathVariable("id") Long questionId, Model model) {
		QnaQuestion question = this.supportService.getQuestionByquestionId(questionId);
		model.addAttribute("question", question);
		
		
		//하단 슬라이더 - 추천상품 ( 여행 건강, 요리, 사랑 행복 키워드 별로 조회 해서 5건 씩 )
//		List<Car> sliderList3 = this.carService.getCarListSlider2("detail1", car);
//		model.addAttribute("sliderList3", sliderList3);
		
		return "support/qnaDetail";
	}
	
	
	
	
	@PostMapping("/qna/search")
	public String requestQuestionListBySearch(
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(name = "searchFor", defaultValue = "") String searchFor,
			@RequestParam(name = "keyword") String keyword, 
			Model model
			) {
		return requestQnaList(pageNum, searchFor, keyword, model);
	}
	
	
	
	
	
	
	@GetMapping("/faq")
	public String requestSupportFaq() {
		return "support/faq";
	}	
	
	@GetMapping("/find")
	public String requestSupportFind() {
		return "support/find";
	}
	
	@GetMapping("/chatbot")
	public String requestSupportChatBot() {
		return "support/chatbot";
	}	
	
	
	
	
}
