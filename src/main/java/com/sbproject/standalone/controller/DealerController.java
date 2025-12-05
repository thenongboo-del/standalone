package com.sbproject.standalone.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sbproject.standalone.entity.Car;
import com.sbproject.standalone.entity.Consultation;
import com.sbproject.standalone.entity.ConsultationStatus;
import com.sbproject.standalone.entity.Member;
import com.sbproject.standalone.entity.Notice;
import com.sbproject.standalone.entity.Role;
import com.sbproject.standalone.service.CarService;
import com.sbproject.standalone.service.ConsultationService;
import com.sbproject.standalone.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dealer")
public class DealerController {
	
	
	private final MemberService memberService;
	
	private final CarService carService;
	
	private final ConsultationService consultService;
	
	
	private final PasswordEncoder passwordEncoder;
	

	// 딜러 메인화면
	@GetMapping("/main")
	public String requestDealerMain(Model model) {
		
		/* 상담 리스트 */
		// 대기중인상담
		
		List<Consultation> consultWaitList = consultService.selectBystatus(ConsultationStatus.WAIT);
		List<Consultation> consultScheduleList = consultService.selectBystatus(ConsultationStatus.SCHEDULE);
		List<Consultation> consultInProgressList = consultService.selectBystatus(ConsultationStatus.IN_PROGRESS);
		List<Consultation> consultEndList = consultService.selectBystatus(ConsultationStatus.END);

		
		/* 자동차 리스트 */
		List<Car> carList = carService.getcarList();
		
		/* 멤버 리스트(Role.USER만) */
		List<Member> memberList = memberService.selectAllCustomer();
		
		/* 공지사항 */
		List<Notice> noticeList = memberService.selectAllNotice();
		
		
		model.addAttribute("consultWaitList", consultWaitList);
		model.addAttribute("consultScheduleList", consultScheduleList);
		model.addAttribute("consultInProgressList", consultInProgressList);
		model.addAttribute("consultEndList", consultEndList);
		
		model.addAttribute("carList", carList);
		model.addAttribute("customerList", memberList);
		model.addAttribute("notice", noticeList);
		model.addAttribute("member", new Member());
		
		return "dealer/main";
	}
	
	
	@PostMapping("/addDealer")
	public String requestAddDealerView(@Valid @ModelAttribute Member member, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		// 유효성 검사
		if(bindingResult.hasErrors()) {
			return "dealer/main";
		}
		
		// 입력한 비밀번호 2개가 일치하는지를 검사
		if(!member.getPassword().equals(member.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordIncorrect", "입력한 비밀번호가 일치하지 않습니다.");
			return "dealer/main";
		}
		
		// 중복 ID 체크
		// DataIntegrityViolationException -> unique 속성을 위배했을 때 발생하는 예외
		try {
			Member m = Member.createMember(member, passwordEncoder);
			m.setRegDate(LocalDateTime.now());	
//			m.setCSegment("신규");
			int age = Period.between(m.getBirthDay(), LocalDate.now()).getYears();		// 생일과 현재 날짜의 기간을 구해서 년도로 변환해서 나이를 구한다.
			m.setAge(age);
			m.setRole(Role.DEALER);
			memberService.saveMember(m);
		} catch(DataIntegrityViolationException e) {
			// rejectValue(필드명, 오류코드, 메시지)
			bindingResult.rejectValue("memberId", "duplecatedMemberId", "이미 존재하는 회원 ID입니다.");
			return "dealer/main";
		} catch(Exception e) {
			bindingResult.rejectValue("memberId", "duplecatedMemberId", e.getMessage());
			return "dealer/main";
		}
		
		// 가입 메세지
		redirectAttributes.addFlashAttribute("flashMessage", 
			    "싱규사원 정보 입력이 정상적으로 완료되었습니다. 환영합니다, " + member.getName() + "님!");
		
		return "dealer/main";
	}
	
	
	
	
}
