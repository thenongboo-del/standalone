package com.sbproject.standalone;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.sbproject.standalone.entity.Address;
import com.sbproject.standalone.entity.Member;
import com.sbproject.standalone.entity.Role;
import com.sbproject.standalone.service.MemberService;

@SpringBootApplication
public class Crm01Application {

	public static void main(String[] args) {
		SpringApplication.run(Crm01Application.class, args);
	}
	
	

	// 더미데이터 데이터 밀어 넣기 (비밀번호 암호화 + join table (Address) 때문에 DB에서 할 수 없음)
	// 회원 10명 (1명은 관리자(admin) )
	// Member entity를 통해서 등록해야한다.
	// Member의 Validation이 걸려있으면 오류가난다, 아이디, 비밀번호, 비밀번호 확인
//	@Bean
	public CommandLineRunner run(MemberService memberService) throws Exception {
		return (String[] args) -> {
			
			// 0. 딜러 등록 (Manager)
			Member m0 = new Member();
			m0.setMemberId("manager");
			m0.setPassword(new BCryptPasswordEncoder().encode("1234"));	// 비밀번호 암호화처리
			m0.setName("김딜러");
			m0.setPhone("010-4989-4989");
			m0.setEmail("manager@korea.com");
			m0.setGender("남성");
//			m0.setAge(35);
			//추가정보
			m0.setBirthDay("1990-10-15");
			m0.setRegDate(LocalDateTime.now());
//			m0.setCSegment("신규");
			
			m0.setRole(Role.DEALER);					// 딜러
			Address a0 = new Address();
			a0.setCountry("한국");
			a0.setZipcode("06035");
			a0.setBasicAddress("서울 강남구 가로수길 5");
			a0.setDetailAddress("강남 아파트 1234호");
			m0.setAddress(a0);
			
			// 1. 관리자 등록 (Admin)
			Member m1 = new Member();
			m1.setMemberId("admin");
			m1.setPassword(new BCryptPasswordEncoder().encode("1234"));	// 비밀번호 암호화처리
			m1.setName("강리자");
			m1.setPhone("010-1234-1234");
			m1.setEmail("admin@korea.com");
			m1.setGender("남성");
//			m1.setAge(41);
			//추가정보
			m1.setBirthDay("1984-03-11");
			m1.setRegDate(LocalDateTime.now());
//			m1.setCSegment("신규");
			
			m1.setRole(Role.ADMIN);					// 관리자
			Address a1 = new Address();
			a1.setCountry("한국");
			a1.setZipcode("06035");
			a1.setBasicAddress("서울 강남구 가로수길 5");
			a1.setDetailAddress("강남 아파트 1234호");
			m1.setAddress(a1);
			
			// 2~10 일반 사용자 등록
			// 2.
			Member m2 = new Member();
			m2.setMemberId("aaaa");
			m2.setPassword(new BCryptPasswordEncoder().encode("1234"));	// 비밀번호 암호화처리
			m2.setName("김준완");
			m2.setPhone("010-1111-1111");
			m2.setEmail("aaaa@naver.com");
			m2.setGender("남성");
//			m2.setAge(25);
			//추가정보
			m2.setBirthDay("1998-05-01");
			m2.setRegDate(LocalDateTime.now());
//			m2.setCSegment("신규");
			
			m2.setRole(Role.USER);					// 일반유저
			Address a2 = new Address();
			a2.setCountry("한국");
			a2.setZipcode("04767");
			a2.setBasicAddress("서울 성동구 광나루로 104");
			a2.setDetailAddress("성동 아파트 1111호");
			m2.setAddress(a2);
			
			// 3.
			Member m3 = new Member();
			m3.setMemberId("bbbb");
			m3.setPassword(new BCryptPasswordEncoder().encode("1234"));	// 비밀번호 암호화처리
			m3.setName("이익준");
			m3.setPhone("010-2222-2222");
			m3.setEmail("bbbb@naver.com");
			m3.setGender("남성");
//			m3.setAge(23);
			m3.setRole(Role.USER);					// 일반유저
			m3.setBirthDay("1901-05-01");
			m3.setRegDate(LocalDateTime.now());
//			m3.setCSegment("신규");
			
			Address a3 = new Address();
			a3.setCountry("한국");
			a3.setZipcode("07378");
			a3.setBasicAddress("서울 영등포구 가마산로 313");
			a3.setDetailAddress("가마빌라 222호");
			m3.setAddress(a3);
			
			// 4.
			Member m4 = new Member();
			m4.setMemberId("cccc");
			m4.setPassword(new BCryptPasswordEncoder().encode("1234"));	// 비밀번호 암호화처리
			m4.setName("채송화");
			m4.setPhone("010-3333-3333");
			m4.setEmail("cccc@naver.com");
			m4.setGender("여성");
//			m4.setAge(26);
			m4.setBirthDay("1998-01-11");
			m4.setRegDate(LocalDateTime.now());
//			m4.setCSegment("신규");
			m4.setRole(Role.USER);					// 일반유저
			Address a4 = new Address();
			a4.setCountry("한국");
			a4.setZipcode("01237");
			a4.setBasicAddress("서울 강북구 월계로 53");
			a4.setDetailAddress("월계 아파트 333호");
			m4.setAddress(a4);
			
			// 5.
			Member m5 = new Member();
			m5.setMemberId("dddd");
			m5.setPassword(new BCryptPasswordEncoder().encode("1234"));	// 비밀번호 암호화처리
			m5.setName("양석형");
			m5.setPhone("010-4444-4444");
			m5.setEmail("dddd@naver.com");
			m5.setGender("남성");
//			m5.setAge(26);
			m5.setBirthDay("1998-12-28");
			m5.setRegDate(LocalDateTime.now());
//			m5.setCSegment("신규");
			m5.setRole(Role.USER);					// 일반유저
			Address a5 = new Address();
			a5.setCountry("한국");
			a5.setZipcode("22229");
			a5.setBasicAddress("인천 미추홀구 경원대로 715");
			a5.setDetailAddress("미추빌라 아파트 444호");
			m5.setAddress(a5);
			
			// 6.
			Member m6 = new Member();
			m6.setMemberId("eeee");
			m6.setPassword(new BCryptPasswordEncoder().encode("1234"));	// 비밀번호 암호화처리
			m6.setName("안정원");
			m6.setPhone("010-5555-5555");
			m6.setEmail("eeee@naver.com");
			m6.setGender("남성");
//			m6.setAge(29);
			m6.setBirthDay("1995-10-18");
			m6.setRegDate(LocalDateTime.now());
//			m6.setCSegment("신규");
			m6.setRole(Role.USER);					// 일반유저
			Address a6 = new Address();
			a6.setCountry("한국");
			a6.setZipcode("22314");
			a6.setBasicAddress("인천 중구 개항로 7-1");
			a6.setDetailAddress("개항 아파트 555호");
			m6.setAddress(a6);
			
			// 7.
			Member m7 = new Member();
			m7.setMemberId("ffff");
			m7.setPassword(new BCryptPasswordEncoder().encode("1234"));	// 비밀번호 암호화처리
			m7.setName("장겨울");
			m7.setPhone("010-6666-6666");
			m7.setEmail("ffff@naver.com");
			m7.setGender("여성");
//			m7.setAge(32);
			m7.setBirthDay("1992-08-08");
			m7.setRegDate(LocalDateTime.now());
//			m7.setCSegment("신규");
			m7.setRole(Role.USER);					// 일반유저
			Address a7 = new Address();
			a7.setCountry("한국");
			a7.setZipcode("16661");
			a7.setBasicAddress("경기 수원시 권선구 경수대로 83");
			a7.setDetailAddress("경선 빌라 666호");
			m7.setAddress(a7);
			
			// 8.
			Member m8 = new Member();
			m8.setMemberId("gggg");
			m8.setPassword(new BCryptPasswordEncoder().encode("1234"));	// 비밀번호 암호화처리
			m8.setName("추민하");
			m8.setPhone("010-7777-7777");
			m8.setEmail("ffff@naver.com");
			m8.setGender("여성");
//			m8.setAge(35);
			m8.setBirthDay("1990-06-01");
			m8.setRegDate(LocalDateTime.now());
//			m8.setCSegment("신규");
			m8.setRole(Role.USER);					// 일반유저
			Address a8 = new Address();
			a8.setCountry("한국");
			a8.setZipcode("16460");
			a8.setBasicAddress("경기 수원시 팔달구 갓매산로 2");
			a8.setDetailAddress("팔달 아파트 777호");
			m8.setAddress(a8);
			
			// 9.
			Member m9 = new Member();
			m9.setMemberId("hhhh");
			m9.setPassword(new BCryptPasswordEncoder().encode("1234"));	// 비밀번호 암호화처리
			m9.setName("도재학");
			m9.setPhone("010-8888-8888");
			m9.setEmail("hhhh@naver.com");
			m9.setGender("남성");
//			m9.setAge(42);
			m9.setBirthDay("1983-05-05");
			m9.setRegDate(LocalDateTime.now());
//			m9.setCSegment("신규");
			m9.setRole(Role.USER);					// 일반유저
			Address a9 = new Address();
			a9.setCountry("한국");
			a9.setZipcode("13383");
			a9.setBasicAddress("경기 성남시 중원구 성남대로 1115");
			a9.setDetailAddress("성남아파트 888호");
			m9.setAddress(a9);
			
			// 10.
			Member m10 = new Member();
			m10.setMemberId("iiii");
			m10.setPassword(new BCryptPasswordEncoder().encode("1234"));	// 비밀번호 암호화처리
			m10.setName("오이영");
			m10.setPhone("010-9999-9999");
			m10.setEmail("iiii@naver.com");
			m10.setGender("여성");
//			m10.setAge(51);
			m10.setBirthDay("1974-10-01");
			m10.setRegDate(LocalDateTime.now());
//			m10.setCSegment("신규");
			m10.setRole(Role.USER);					// 일반유저
			Address a10 = new Address();
			a10.setCountry("한국");
			a10.setZipcode("13479");
			a10.setBasicAddress("경기 성남시 분당구 서판교로 32");
			a10.setDetailAddress("분당빌라 999호");
			m10.setAddress(a10);
			
			
			// memberService를 통해서 DB로 등록
			memberService.saveMember(m1);
			memberService.saveMember(m2);
			memberService.saveMember(m3);
			memberService.saveMember(m4);
			memberService.saveMember(m5);
			memberService.saveMember(m6);
			memberService.saveMember(m7);
			memberService.saveMember(m8);
			memberService.saveMember(m9);
			memberService.saveMember(m10);
			memberService.saveMember(m0);
			
		};
	}
	
	
}
