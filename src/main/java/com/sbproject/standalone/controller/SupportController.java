package com.sbproject.standalone.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sbproject.standalone.entity.Faq;
import com.sbproject.standalone.entity.Member;
import com.sbproject.standalone.entity.QnaAnswer;
import com.sbproject.standalone.entity.QnaQuestion;
import com.sbproject.standalone.service.MemberService;
import com.sbproject.standalone.service.SupportService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@Controller
@RequiredArgsConstructor
@RequestMapping("/support")
public class SupportController {
	
	private final SupportService supportService;
	private final MemberService memberService;
	

	@GetMapping("/main")
	public String requestSupportMain() {
		return "support/supportMain";
	}
	
	
	
//-----------------------------------QNA----------------------------------------------
	
	// QNA 페이징 설정
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
	
	
	// 질문 등록 화면
	@GetMapping("/qna/add")
	public String addQna(Model model) {
		model.addAttribute("qnaQuestion", new QnaQuestion());
		return "support/qnaAdd";
	}
	
	
	// 질문 등록 처리
	@PostMapping("/qna/add")
	public String addMemberPro(@Valid @ModelAttribute QnaQuestion question, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails) {
		// 유효성 검사
		if(bindingResult.hasErrors()) {
			return "support/qnaAdd";
		}
		
		Member member = memberService.findByMemberId(userDetails.getUsername());
		question.setWriter(member.getMemberId());
		question.timeSet();
		
		supportService.createQuestion(question);
		
		return "redirect:/support/qna";
	}
	
	
		// 질문 수정 폼
		@GetMapping("/qna/update/{id}")
		public String updateQuestionView(@PathVariable("id") Long questionId, Model model) {
			QnaQuestion question = this.supportService.getQuestionByquestionId(questionId);
			model.addAttribute("question", question);
			return "support/qnaUpdate";
		}
	
		
	
		// 질문 수정 처리
		@PostMapping("/qna/update")
		public String updateQuestionLogic(@ModelAttribute QnaQuestion question, BindingResult bindingResult) {
			
			// 유효성 검사
			if(bindingResult.hasErrors()) {
				return "support/qnaUpdate";
			}
			
			// 수정 처리
			try {
				QnaQuestion oldQuestion = supportService.selectQuestionById(question.getId());
				oldQuestion.setTitle(question.getTitle());
				oldQuestion.setContent(question.getContent());
				oldQuestion.preUpdate();
				supportService.updateQuestion(oldQuestion);
				
				return "redirect:/support/qna/" + oldQuestion.getId();
				
			} catch(Exception e) {
				e.printStackTrace();
				return "support/qnaUpdate";
			}
			
		}
	
	
	
		
	///qna 삭제
	@PostMapping("/qna/delete/{id}")
	public String deleteQuestionLogic(@PathVariable("id") Long questionId, Model model) {
		// 나중에 이걸로 작성자 비교후 지워야함
		supportService.getQuestionByquestionId(questionId);
		
		// 삭제하는 메서드
		supportService.removeQuestionById(questionId);
		
		return "redirect:/support/qna";
	}
	
	
//---------------------------------------------------------------------------------

	// 댓글 쓰기
	@PostMapping("/qna/answer/insert")
	public String insertAnswer(QnaAnswer answer, @AuthenticationPrincipal  UserDetails userDetails) {
		
		
		Member member = memberService.findByMemberId(userDetails.getUsername());
		
		answer.setWriter(member.getName());
		answer.prePersist();
		
		supportService.saveAnswer(answer);
		
		return "redirect:/support/qna/" + answer.getQuestion().getId();
	}
	
	
	
	// 댓글 수정 
	// 댓글 수정 (GET 방식)
	// 댓글 수정 (GET 방식)
    @GetMapping("/qna/answer/update")
    public String updateAnswer(@RequestParam("answerId") Long answerId, 
                               @RequestParam("content") String content) {
        
        System.out.println("DEBUG: 수정 요청 들어옴 - ID: " + answerId); // 로그 확인용
        
        // 1. 댓글 조회
        QnaAnswer answer = supportService.findByAnswerId(answerId).get();
        
        // 2. 내용 변경
        answer.setContent(content);
        answer.preUpdate(); 
        
        // 3. 저장
        supportService.saveAnswer(answer);

        // 4. 리다이렉트
        return "redirect:/support/qna/" + answer.getQuestion().getId();
    }

	
	// 삭제
    @GetMapping("/qna/answer/delete")
    public String deleteAnswer(@RequestParam("answerId") Long answerId) {
    	
    	QnaAnswer answer =  supportService.findByAnswerId(answerId).get();
    	Long questionId = answer.getQuestion().getId();
        // service 호출해서 DB 삭제
    	supportService.deleteAnswer(answerId);

        // 리다이렉트할 페이지
        return "redirect:/support/qna/"  + questionId; // 필요에 따라 변경
    }

	
	
	
	@GetMapping("/qna/search")
	public String requestQuestionListBySearch(
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(name = "searchFor", defaultValue = "") String searchFor,
			@RequestParam(name = "keyword") String keyword, 
			Model model
			) {
		return requestQnaList(pageNum, searchFor, keyword, model);
	}
	
//------------------------------------------------------------------------------------------------------------------	
	
	
	
	
	@GetMapping("/faq")
	public String requestSupportFaq(
			@RequestParam(name = "searchFor", defaultValue = "") String searchFor,
			@RequestParam(name = "keyword", defaultValue="") String keyword, 
			Model model
			) {
		List<Faq> faqList = supportService.getFaqList(searchFor, keyword);
		model.addAttribute("faqList", faqList);
		
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
