package com.sbproject.standalone.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sbproject.standalone.entity.Consultation;
import com.sbproject.standalone.entity.ConsultationStatus;
import com.sbproject.standalone.entity.ConsultationType;
import com.sbproject.standalone.entity.Member;
import com.sbproject.standalone.service.ConsultationService;
import com.sbproject.standalone.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {

	private final MemberService memberService;
	
	private final PasswordEncoder passwordEncoder;
	
	private final ConsultationService consultService;
	
	// [1] 일반 사용자
	// 회원가입 폼
	@GetMapping("/join")
	public String addMemberForm(Model model) {
		model.addAttribute("member", new Member());
		return "member/joinMember";
	}
	
	
	// 회원가입 처리
	@PostMapping("/signup")
	public String addMemberPro(@Valid @ModelAttribute Member member, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		// 유효성 검사
		if(bindingResult.hasErrors()) {
			return "member/joinMember";
		}
		
		// 입력한 비밀번호 2개가 일치하는지를 검사
		if(!member.getPassword().equals(member.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordIncorrect", "입력한 비밀번호가 일치하지 않습니다.");
			return "member/joinMember";
		}
		
		// 중복 ID 체크
		// DataIntegrityViolationException -> unique 속성을 위배했을 때 발생하는 예외
		try {
			Member m = Member.createMember(member, passwordEncoder);
			m.setRegDate(LocalDateTime.now());	
//			m.setCSegment("신규");
			int age = Period.between(m.getBirthDay(), LocalDate.now()).getYears();		// 생일과 현재 날짜의 기간을 구해서 년도로 변환해서 나이를 구한다.
			m.setAge(age);
			memberService.saveMember(m);
		} catch(DataIntegrityViolationException e) {
			// rejectValue(필드명, 오류코드, 메시지)
			bindingResult.rejectValue("memberId", "duplecatedMemberId", "이미 존재하는 회원 ID입니다.");
			return "member/joinMember";
		} catch(Exception e) {
			bindingResult.rejectValue("memberId", "duplecatedMemberId", e.getMessage());
			return "member/joinMember";
		}
		
		// 가입 메세지
		redirectAttributes.addFlashAttribute("flashMessage", 
			    "회원가입이 정상적으로 완료되었습니다. 환영합니다, " + member.getName() + "님!");
		
		log.info(member.toString());
		return "redirect:/";
	}
	
	// 회원정보 조회 (1건) -> 수정과 삭제 화면
	@GetMapping("/update/{memberId}")
	public String updateMemberForm(@PathVariable("memberId") String memberId, Model model) {
		List<Consultation> consultList = consultService.findByMemberId(memberId);
		List<Consultation> consultBuyEndLIst = consultService.selectBystatusAndType(ConsultationStatus.END, ConsultationType.BUY);
		List<Consultation> consultDriveEndLIst = consultService.selectBystatusAndType(ConsultationStatus.END, ConsultationType.DRIVE);
		
		// 진행중인 상담 건수 ( 모든 상담 건수 - (종료된 구매상담 + 종료된 시승신청)
		Long consultCount = (long) ((consultBuyEndLIst.size() + consultDriveEndLIst.size()) - consultList.size());
		
		Member member = memberService.findByMemberId(memberId);
		member.setAges();
		model.addAttribute("consultCount", consultCount);
		model.addAttribute("member", member);
		
		return "member/updateMember";
	}
	
	// 회원정보 수정 처리
	@PostMapping("/update")
	public String updateMemberPro(@Valid @ModelAttribute Member member, BindingResult bindingResult) {
		
		
		// 유효성 검사
		if(bindingResult.hasErrors()) {
			return "member/updateMember";
		}
		
		// 입력한 비밀번호 2개가 일치하는지를 검사
		if(!member.getPassword().equals(member.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordIncorrect", "입력한 비밀번호가 일치하지 않습니다.");
			return "member/updateMember";
		}
		
		// 수정 처리
		try {
			Member m = Member.createMember(member, passwordEncoder);
			memberService.updateMember(m);
			
		} catch(Exception e) {
			e.printStackTrace();
			return "member/updateMember";
		}
		
//		member.setAges();
				
		return "redirect:/member/update/" + member.getMemberId();
	}
	
	// 회원삭제(탈퇴)
	@GetMapping("/delete/{memberId}")
	public String deleteMemberPro(@PathVariable("memberId") String memberId) {
		memberService.deleteMember(memberId);
		
		return "redirect:/logout";
	}
	
	@GetMapping("/consultation/list")
	public String requestConsultationList(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		
		List<Consultation> allList = consultService.findByMemberId(userDetails.getUsername());

		//		대기중인 리스트
		List<Consultation> requestList = Optional.ofNullable(allList).orElseGet(Collections::emptyList).stream()
			    .filter(c -> c != null && c.getStatus() == ConsultationStatus.WAIT).collect(Collectors.toList());
		
		List<Consultation> scheduleList = Optional.ofNullable(allList).orElseGet(Collections::emptyList).stream()
				.filter(c -> c != null && c.getStatus() == ConsultationStatus.SCHEDULE).collect(Collectors.toList());
		
		List<Consultation> inProgressList = Optional.ofNullable(allList).orElseGet(Collections::emptyList).stream()
				.filter(c -> c != null && c.getStatus() == ConsultationStatus.IN_PROGRESS).collect(Collectors.toList());
		
		List<Consultation> endList = Optional.ofNullable(allList).orElseGet(Collections::emptyList).stream()
				.filter(c -> c != null && c.getStatus() == ConsultationStatus.END).collect(Collectors.toList());

		
		
		model.addAttribute("allList", allList);
		model.addAttribute("requestList", requestList);
		model.addAttribute("scheduleList", scheduleList);
		model.addAttribute("inProgressList", inProgressList);
		model.addAttribute("endList", endList);
		
		return "member/consultList";
	}
	
	
	
	
	// #################################################################################
	
	// [2] 관리자
	// 전체 회원정보 조회
	
	// 회원정보 수정
	
	// 회원삭제(강제 탈퇴) -> 블랙 컨슈머일 때
	
	
}
