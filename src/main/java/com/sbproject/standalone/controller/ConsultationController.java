package com.sbproject.standalone.controller;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sbproject.standalone.entity.Consultation;
import com.sbproject.standalone.entity.ConsultationDetail;
import com.sbproject.standalone.entity.ConsultationEndResult;
import com.sbproject.standalone.entity.ConsultationStatus;
import com.sbproject.standalone.entity.ConsultationType;
import com.sbproject.standalone.service.ConsultationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@Controller
@RequiredArgsConstructor
@RequestMapping("/consultation")
public class ConsultationController {
	
	private final ConsultationService consultService;
	

	// 상담 메인화면
	@GetMapping("/main")
	public String requestConsultationMain(Model model) {
		
		return "consultation/consultation-customer";
	}
	
	
	// 상담신청 폼 보기
	@GetMapping("/buy/form")
	public String requestConsultationBuyView(Model model) {
		
		model.addAttribute("consultation", new Consultation());
		
		return "consultation/buyForm";
	}
	
	
	
	
//	상담신청 폼 처리
	@PostMapping("/buy/form")
	public String requestConsultationBuyLogic(@ModelAttribute Consultation consult, Model model, 
			@AuthenticationPrincipal UserDetails userDetails, RedirectAttributes ra) {
		consult.setCustomerId(userDetails.getUsername());
		consult.setType(ConsultationType.BUY);
		consult.setRequestedAt(LocalDateTime.now());
		// 상담 신청일과 상담디테일 객체 생성및 추가
//		ConsultationDetail d = new ConsultationDetail();
//		consult.requestConsultation(d);
		
		consultService.requestConsultation(consult);
		
		// 성공 메시지 플래시 속성으로 전달 (리다이렉트 후에도 유지됨)
	    ra.addFlashAttribute("successMessage", "구매상담 신청이 완료되었습니다! 담당 딜러가 곧 연락드릴 예정입니다.");
		
		return "redirect:/consultation/buy/form";
	}
	
	
//	상담 신청 수락 로직
	@PostMapping("/dealer/saveSchedule")
	public String requestConsultationBuySvaeScheduleLogic(
			@RequestParam("consultId") Long consultId,
	        @RequestParam("scheduleDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate scheduleDate,
	        @RequestParam("scheduleTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime scheduleTime,
	        @AuthenticationPrincipal UserDetails userDetails
			) {
		Consultation consult = consultService.selectByConsultId(consultId);
		// 딜러세팅, 시간세팅, 수락일 세팅, status 세팅
		
		//딜러세팅
		consult.setDealerId(userDetails.getUsername());
		
		// 다음 상담 일정
		LocalDateTime scheduleDateTime = LocalDateTime.of(scheduleDate, scheduleTime);
		// 다음 일정 세팅
		consult.setNextSchedule(scheduleDateTime);
		
		// 수락일 세팅
		consult.setAssignedAt(LocalDateTime.now());
		
		// status 세팅
		consult.setStatus(ConsultationStatus.IN_PROGRESS);
		
		// 객체를 저장
		consultService.saveConsultation(consult);
		
		
		
		
		return "redirect:/dealer/main";
	}
	
	// 상담내용 저장
	@PostMapping("/dealer/insert/content")
	public String insertConsultationDetail(@RequestParam("id") Long id, @RequestParam("content") String content) {
		
		// 1) 상위 consultation 조회
	    Consultation consult = consultService.selectByConsultId(id);

	    // 2) 새 detail 객체 생성
	    ConsultationDetail detail = new ConsultationDetail();
	    detail.setConsultedAt(LocalDateTime.now());
	    detail.setContent(content);
	    detail.setRoundNumber(consult.getRoundNumber());  //회차 맞추기

	    // 3) 양방향 관계 연결 (편의 메소드 사용)
	    consult.addDetail(detail);

	    // 4) 회차 증가 / 업데이트 날짜 세팅
	    consult.setRoundNumber(consult.getRoundNumber()+1);
	    consult.setUpdatedAt(LocalDateTime.now());
	    
	    // 5) 상태변경 : 상담중 -> 일정 조정
	    consult.setStatus(ConsultationStatus.SCHEDULE);
	    
	    // 4) 저장
	    consultService.saveConsultation(consult); // 또는 repository.save(consult)
		
		return "redirect:/dealer/main";
	}
	
	
	@PostMapping("/dealer/complete")
	public String ConsultationComplete(@RequestParam("id")Long id, @RequestParam("resultType") String resultType) {
		
		Consultation consult = consultService.selectByConsultId(id);
		
		// 문자열을 이넘타입으로 변환하기
		ConsultationEndResult result = ConsultationEndResult.valueOf(resultType);
//		if(resultType == "BUY") {
//			consult.setEndResult(ConsultationEndResult.PURCHASE);
//			
//		} else if(resultType == "NORMAL") {
//			consult.setEndResult(ConsultationEndResult.NORMAL);
//		}
		
		// 상태 전환 -> 종료
		consult.setStatus(ConsultationStatus.END);
		
		// 종료 값 입력로 변환
		consult.setEndResult(result);
		
		// 종료일 지정
		consult.setEndDate(LocalDateTime.now());
		
		// 저장
		consultService.saveConsultation(consult); 
		
		
		
		return "redirect:/dealer/main";
	}
	
	
	
	
	
	
	//----------------------------------------------- Drive ----------------------------------------------
	@GetMapping("/drive/form")
	public String requestConsultationDriveView(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		
		List<Consultation> consult = consultService.findByMemberId(userDetails.getUsername());
		
		// 이전에 시승신청을 했는지 확인
		boolean flag = true;
		if(consult != null) {
			for(int i=0; i<= consult.size()-1; i++) {
				if(consult.get(i).getType() == ConsultationType.DRIVE) {	// 시승신청 리스트중에서
					if(consult.get(i).getStatus() == ConsultationStatus.WAIT || consult.get(i).getStatus() == ConsultationStatus.SCHEDULE) {
						flag = false;	// 대기중이거나 스케쥴 조정중인 건이 있다면 플레그 false
					}
				}
			}
		}
		
		
		// 이전에 시승신청을 했으면, 안내 페이지로 이동
		if(flag) {
			model.addAttribute("consultation", new Consultation());
			return "consultation/driveForm";
		} else {
			return "consultation/driveRequested";
		}
	}
	
	
	
	@PostMapping("/drive/form")
	public String requestConsultationDriveLogic(@ModelAttribute Consultation consult, Model model, 
			@AuthenticationPrincipal UserDetails userDetails, RedirectAttributes ra) {
		consult.setCustomerId(userDetails.getUsername());
		consult.setType(ConsultationType.DRIVE);
		consult.setStatus(ConsultationStatus.WAIT);
		consult.setRequestedAt(LocalDateTime.now());
		
		// 상담 신청일과 상담디테일 객체 생성및 추가
//		ConsultationDetail d = new ConsultationDetail();
//		consult.requestConsultation(d);
		
		consultService.requestConsultation(consult);
		
		// 성공 메시지 플래시 속성으로 전달 (리다이렉트 후에도 유지됨)
	    ra.addFlashAttribute("successMessage", "시승 신청이 완료되었습니다! 담당 딜러가 곧 연락드릴 예정입니다.");
		
		return requestConsultationDriveView(model, userDetails);
	}
	
	
}
