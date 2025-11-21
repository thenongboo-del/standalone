package com.sbproject.standalone.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/*
< 주소 클래스의 멤버변수 >
- 아이디, 국가, 우편번호, 기본주소, 상세주소
*/

@Entity
@Data
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(columnDefinition = "varchar(50)")
	@NotBlank(message = "국가명은 필수 입력사항입니다.")
	private String country;
	
	@Column(columnDefinition = "varchar(10)")
	@NotBlank(message = "우편번호는 필수 입력사항입니다.")
	private String zipcode;
	
	@Column(columnDefinition = "varchar(100)")
	@NotBlank(message = "기본 주소는 필수 입력사항입니다.")
	private String basicAddress;
	
	@Column(columnDefinition = "varchar(100)")
	@NotBlank(message = "상세 주소는 필수 입력사항입니다.")
	private String detailAddress;
	
}
