package com.sbproject.standalone.entity;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// 상품 클래스
/*
- 제조회사, 제조년도, 차량이름
- 차량 구분 : 경차, 소형, 준중형 세단, 준중형 해치백, 준중형 SUB, 중형 세단, 중형 SUV, 준대형 세단, 준대형 SUV, 대형 세단, 대형 SUV
- ★ Validtion으로 설정한 어노테이션도 Entity로 생성한 테이블에 제약조건으로 추가됨.

*	id
*	carId : ex) k-k3 (기아,k3) / h-avante (현대, 아반떼) 
*	company : 현대 / 기아
*	category
*	name : 차량명
*	price: 가격
*	plant: 해외 공장 (us, china, slovakia, mexico, india)
*	이미지
*	fileName1 : 얖
*	fileName2 : 뒤	
*	fileName3 : 옆
*	fileName4 : 전체
*
*/



@Data
@Entity
public class Car {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// 상품 ID는 ISBN 다음에 0~9 사이의 숫자로 시작
	@NotBlank(message = "차량 ID는 필수 입력 사항입니다.")
	@Column(columnDefinition = "varchar(30)")
	private String carId;
	
	
	@NotBlank(message = "차량명은 필수 입력 사항입니다.")
	private String name;
	
	// 최소값 0, 음수는 사용불가, 전체 8자리, 소수점 2자리
	@NotNull @Min(value=0) @Digits(integer=11, fraction=2)
	private Integer price;
	
	@NotBlank(message = "제조회사 필수 입력 사항입니다.")
	@Column(columnDefinition = "varchar(100)")
	private String company;
	
	@NotBlank(message = "해외 공장은 필수 입력 사항입니다.")
	@Column(columnDefinition = "varchar(100)")
	private String plant;
	
	
	@NotBlank(message = "차량구분은 필수 입력 사항입니다.")
	@Column(columnDefinition = "varchar(100)")
	private String category; // default값 설정 : 컴퓨터/IT
	
	@NotBlank(message = "연료타입은 필수 입력 사항입니다.")
	@Column(columnDefinition = "varchar(30)")
	private String fuelType;
	
	
	// 앞면
	@Column(columnDefinition = "varchar(30) default 'no_image.jpg'")
	private String fileName;  // 도서이미지의 파일명
	
	
	
	// 생성할 테이블에서는 필드는 제외함
	@Transient
	private MultipartFile carImage; // 업로드된 차량 이미지
	
	
	@OneToOne(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    private CarDetailInfo carDetailInfo;
	
}
