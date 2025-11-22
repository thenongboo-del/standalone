package com.sbproject.standalone.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/*
< 회원 클래스의 멤버 변수>
- Member
- 회원번호(PK, 자동), 회원ID, 비밀번호, 이름, 전화번호, 이메일, 성별(남성, 여성), 나이(만 나이), 
- 주소(클래스), 사용자 역할(USER, ADMIN 둘 중 하나)
- 추가정보 (AI 추천을 위한)
	[선호차종, 연료-디젤,가솔린,전기차, 사용목적, 가족구성,] 

< 주소 클래스의 멤버 변수 >
- Address
- 국가, 우편번호, 기본주소, 상세주소

< 사용자 역할 - enum >
- Role
- USER(일반 사용자), ADMIN(관리자), DEALER
- SecurityConfig 파일에 설정
*/

@Entity
@Data
public class Member {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// 회원ID는 입력형식을 지정해야함 -> Pattern
	// - 하나 이상의 숫자를 포함, 하나 이상의 대문자 또는 소문자 포함, 공백이나 탭을 포함하지 않아야 함
	// - 총 8~16글자 사이여야 함
	// - 주의: 패턴은 테스트가 끝나면, 실무용으로 바꾸어 사용할 것
	@Column(unique = true, columnDefinition = "varchar(30)")
	@NotBlank(message = "회원ID는 필수 입력사항입니다.")
	//@Pattern(regexp = "(?=.*[0-9])(?=.*[A-Za-z])(?=\\S+$).{8,16}", message = "회원ID 형식에 맞게 입력해주세요. ex) 숫자,영문자를 포함하여 8~16글자 사이") // 실무용
//	@Pattern(regexp = "(?=.*[A-Za-z]).{4,16}", message = "회원ID 형식에 맞게 입력해주세요. ex) 영문자를 포함하여 4~16글자 사이") // 테스트용
	private String memberId;
	
	// 비밀번호는 입력형식을 지정해야함 -> Pattern
	// - 하나 이상의 숫자를 포함, 하나 이상의 대문자 또는 소문자 포함, 하나 이상의 특수문자를 포함, 공백이나 탭을 포함하지 않아야 함
	// - 총 8~16글자 사이여야 함
	// - 주의: 패턴은 테스트가 끝나면, 실무용으로 바꾸어 사용할 것
	// 주의: 암호화를 한 크기만큼 설정해야 함 (Column 설정시)
	@Column(columnDefinition = "varchar(100)")
//	@NotBlank(message = "비밀번호는 필수 입력사항입니다.")
	//@Pattern(regexp = "(?=.*[0-9])(?=.*[A-Za-z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호 형식에 맞게 입력해주세요. ex) 숫자,영문자,특수문자를 포함하여 8~16글자 사이") // 실무용
//	@Pattern(regexp = "(?=.*[0-9]).{4,16}", message = "비밀번호 형식에 맞게 입력해주세요. ex) 숫자를 포함하여 4~16글자 사이") // 테스트용
	private String password;
	
	// 테이블에서 생성 제외
	@Transient
//	@NotBlank(message = "비밀번호 확인은 필수 입력사항입니다.")
	private String password2;
	
	@Column(columnDefinition = "varchar(30)")
	@NotBlank(message = "회원명은 필수 입력사항입니다.")
	private String name;
	
	// 전화번호는 입력형식을 지정해야함 -> Pattern
	// 앞 세자리는 010, 011, 016~019, . 또는 -, 중간은 숫자 3자리 또는 숫자 네자리, . 또는 -, 맨 뒤는 숫자 4자리
	// 허용 예) 010-1111-1111
	// 불가 예) 015-1111-1111, 010-111-111, 010-11111-11111
	@Column(columnDefinition = "char(13)")
	@NotBlank(message = "전화번호는 필수 입력사항입니다.")
	@Pattern(regexp = "01(?:0|1|[6~9])[.-]?(\\d{3}|\\d{4})[.-](\\d{4})$", message = "전화번호 형식에 맞게 입력해주세요. ex) 010-1111-1111")
	private String phone;
	
	// 이메일은 입력형식을 지정해야함 -> Pattern
	// @앞에는 2~20글자 사이, @는 필수, .앞에는 2~30글자 사이, .은 필수, .뒤에는 2~6글자 사이
	// 허용 예) aa@aa.com
	// 불가 예) a@a.com, @aa.com, aaa@aaa, aaa@aaa.aaaaaa
	@Column(columnDefinition = "varchar(50)")
	@NotBlank(message = "이메일은 필수 입력사항입니다.")
	@Pattern(regexp = "^[A-Za-z0-9._%+-]{2,20}+@[A-Za-z0-9]{2,30}+.[A-Za-z]{2,6}$", message = "이메일의 형식에 맞게 입력해주세요. ex) aa@bb.com, aa:2글자 이상, bb:2글자 이상, com:2~6글자 사이")
	private String email;
	
	// 추가 - 생년월일, 가입일, 고객분류
//	@NotBlank(message = "생년월일은 필수 입력사항입니다.")
	@Column(columnDefinition = "date")
	private LocalDate birthDay;
	
	// 가입일
	@Column(columnDefinition = "datetime default now()")
	private LocalDateTime regDate;
	
	// 고객 등급 = 신규 / 일반 / VIP / 이탈 가능
//	@Column(columnDefinition = "varchar(20) default '신규'")
//	private String cSegment;
	
	@Column(columnDefinition = "char(2)")
	private String gender;
	
	// 사용자 나이는 생년월일에서 오늘 날짜를 계산해서 가져와야 할 듯
//	@NotNull @Min(value=1)
	@Transient
	private Integer age;
	
	// 사용자 역할
	@Enumerated(EnumType.STRING)
	private Role role;
	
	
	/*
	회원과 주소 -> 1 대 1의 관계
	- 각 회원은 1개의 주소와 매핑됨
	- CascadeType.ALL -> 회원을 삭제하면 회원에 대한 주소도 함께 삭제됨	
	*/
//	@OneToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "address_id")
//	@Valid // Address의 각 멤버에 대한 유효성 검사
//	private Address address;
	
	
	// 회원 생성 메서드
	// - 비밀번호 암호화, 사용자역할 설정(일반 사용자)	
	public static Member createMember(Member member, PasswordEncoder passwordEncoder) {
		member.password = passwordEncoder.encode(member.password);
		member.role = Role.USER;
		return member;
	}
	
	
	// 생일로 날짜 구하는 메서드
	public void setAges() {
		int ages = Period.between(this.getBirthDay(), LocalDate.now()).getYears();		// 생일과 현재 날짜의 기간을 구해서 년도로 변환해서 나이를 구한다.
		this.setAge(ages);
	}
	
	
	
}
