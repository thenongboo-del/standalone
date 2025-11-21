package com.sbproject.standalone.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sbproject.standalone.entity.Car;
import com.sbproject.standalone.service.CarService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
@Controller
@RequestMapping("/cars")
public class CarController {

	private final CarService carService;
	
	// 페이징 설정
	private int pageSize = 6;	// 페이지당 상품 갯수(4개씩 2줄)
	private int pageBlock = 5;	// 페이징되어 나타나는 숫자 링크들의 갯수
	
	// [1] 일반 사용자 기능	
	
	// 도서 전체 목록 1 -> 페이징 처리가 되지 않음
//	@GetMapping
//	public String requestCarList(Model model) {
//		List<Car> carList = this.carService.getCarList();
//		model.addAttribute("carList", carList);
//		return "car/cars";
//	}
	
	
	// 전체 목록 조회 -> 페이징 처리된 메서드로 이동
	@GetMapping
	public String requestCarList(Model model) {
		return requestCarPagingList(1, "all", null, model);
	}
	
	
	
	// 제조회사, 차량구분 별로 상품 조회 (출판일기준으로 내침차순 정렬)
	@GetMapping("/state")
	public String requestCarByState(
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(name = "state", defaultValue = "all") String state,
			@RequestParam(name = "keyword") String keyword, 
			Model model
			) {
		
		return requestCarPagingList(pageNum, state, keyword, model);
	}
	
	// 검색을 통한 상품 조회
	// 차량명, 제조회사, 차량구분3가지 필드에 대한 검색 -> 차량명에 대한 오름차순
	@PostMapping("/search")
	public String requestCarListBySearch(
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(name = "state", defaultValue = "search") String state,
			@RequestParam(name = "keyword") String keyword, 
			Model model
			) {
		return requestCarPagingList(pageNum, state, keyword, model);
	}
	
	
	
	// 상품 전체 목록 : paging 처리
	// 1. 전체 목록 + 페이징
	// 2. 브랜드별 차량명 차종별 도서 조회 + 페이징
	// 3. 검색을 통한 조회 + 페이징 : 제목, 내용, 저자, 출판사별로 검색
	@GetMapping("/paging")
	public String requestCarPagingList(
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(name = "state", defaultValue = "all") String state,
			@RequestParam(name = "keyword") String keyword,
			Model model
			) {
		// 출판일을 기준으로 최근 순으로 정렬
		Page<Car> paging = carService.getCarPagingList(pageNum, state, keyword, pageSize, "id", "desc");
		
		// 페이징 처리된 도서 목록 획득
		
		List<Car> carList = paging.getContent();
		
		// 페이지 블럭의 시작번호 & 끝번호 설정
		int startPage = pageNum - (pageNum - 1) % pageBlock;
		int endPage = startPage + pageBlock - 1;
		
		// 페이징 정보를 모델에 저장
		model.addAttribute("carList", carList);					// 도서 목록
		model.addAttribute("pageNum", pageNum);						// 현제 페이지 번호
		model.addAttribute("pageBlock", pageBlock);					// 노출 될 페이지 블럭 수 - 5
		model.addAttribute("totalItems", paging.getTotalElements());// 전체 게시물 수
		model.addAttribute("totalPages", paging.getTotalPages());	// 전체 페이지 수 ex) 165개 / 12 = 13.75 -> 14pages
		model.addAttribute("startPage", startPage);					// 블럭의 시작 페이지
		model.addAttribute("endPage", endPage);						// 블럭의 끝 페이지
		model.addAttribute("state", state);							// all, author, publisher, category, carCondition 중의 1개의 값		
		model.addAttribute("keyword", keyword);						// state에서 사용 할 값
		
		
		// 메인의 하단 슬라이더 - 추천상품 (건강 요리 사랑 제목 또는 내용에서 조회해서 5건 씩)
//		List<Car> sliderList2 = this.carService.getCarListSlider("main2");
//		model.addAttribute("sliderList2", sliderList2);
//		sliderList2.forEach(c -> log.info(c.toString()));
		
		return "car/cars";
	}
	
	
		
	// 차 상세 보기 (1권)
	// 1번 방법 - PathVariable 사용
	@GetMapping("/car/{carId}")
	public String requestCarByCarId(@PathVariable("carId") String carId, Model model) {
		Car car = this.carService.getCarByCarId(carId);
		model.addAttribute("car", car);
		
		
		//하단 슬라이더 - 추천상품 ( 여행 건강, 요리, 사랑 행복 키워드 별로 조회 해서 5건 씩 )
//		List<Car> sliderList3 = this.carService.getCarListSlider2("detail1", car);
//		model.addAttribute("sliderList3", sliderList3);
		
		return "car/car";
	}
	
	// 2번 방법 - RequestParam 사용
	/*
	@GetMapping("/car")
	public String requestCarById(@RequestParam("carId") String carId, Model model) {
		Car car = this.carService.getCarByCarId(carId);
		return "car/car";
	}
	*/
	
	// 파일이미지 다운로드
	@GetMapping("/download")
	public void downloadCarImage(@RequestParam("file") String file, 
			HttpServletResponse response) throws IOException {
		File imageFile = new File(fileDir + file);
		response.setContentType("application/download");
		response.setContentLength((int)imageFile.length());
		response.setHeader("Content-disposition", "attachment;filename=\"" + file + "\"");
		
		OutputStream os = response.getOutputStream();
		FileInputStream fis = new FileInputStream(imageFile);
		FileCopyUtils.copy(fis, os);
		
		fis.close();
		os.close();
	}
	
	// 도서ID에 해당하는 도서 1건 조회
	
	// ################################################################################
	// [2] 관리자 기능
	
	// 도서 등록
	// 도서 등록 폼
	@GetMapping("/admin/add")
	public String requestAddCarForm(Model model) {
		model.addAttribute("car", new Car());
		return "car/addCar";
	}
	
	private String fileDir = "c:/upload02/";
	
	// 도서 등록 처리
	@PostMapping("/admin/add")
	public String requestAddCarProc(@Valid @ModelAttribute Car car, BindingResult bindingResult) {
		// 유효성 검사를 위배했을 때
		if(bindingResult.hasErrors()) {
			return "car/addCar";
		}
		
		// 업로드 파일 처리
		MultipartFile carImage = car.getCarImage();
		String saveName = carImage.getOriginalFilename();
		File saveFile = new File(fileDir, saveName);
		
		if(carImage != null && !carImage.isEmpty()) {
			try {
				carImage.transferTo(saveFile);
			} catch(IOException e) {
				e.printStackTrace();
				throw new RuntimeException("도서 이미지 업로드를 실패하였습니다.!", e);
			}
			car.setFileName(saveName);
		} else {
			car.setFileName("no_image.jpg");
		}
		this.carService.addNewCar(car);
		return "redirect:/cars";
	}
	
	// 폼에서 넘어오는 name의 값이 @ModelAttribute에 설정되어 있는 객체에 정확하게 바인딩되도록 하는 메서드
	// @InitBinder에서 생략하는 값은 @ModelAttribute에서 값을 설정하지 않음
//	@InitBinder
//	public void initBinder(WebDataBinder binder) {
//		binder.setAllowedFields("carId", "title", "price", "author", "description", "publisher", 
//				"category", "stock", "releaseDate", "carCondition", "fileName", "carImage");
//	}
	
	// 폼에서 넘어오는 name의 값이 @ModelAttribute에 설정되어 있는 객체에 정확하게 바인딩되도록 하는 메서드
	// @InitBinder에서 생략하는 값은 @ModelAttribute에서 값을 설정하지 않음
	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    binder.setAllowedFields(
	        "carId", "name", "price", "company", "plant",
	        "category", "fileName", "carImage"
	    );
	}
	
	// 도서 전체 목록 조회
	
	// 도서 1건 조회
	
	// 도서 삭제
	
	// 도서 수정
	
}
