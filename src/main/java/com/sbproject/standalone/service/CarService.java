package com.sbproject.standalone.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sbproject.standalone.entity.Car;
import com.sbproject.standalone.repository.CarRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CarService {
	
	private final CarRepository carRepository;
	
	
	
	
	// [1] 일반 사용자 기능
	
	// 전체 차량 목록 조회
	public List<Car> getcarList() {
		return this.carRepository.findAll();
	}
	
	
	// 차량명, 제조사, 차종별로 차량 조회 (출판일기준으로 내침차순 정렬)
	// 전체 차량 목록 조회 -> paging
	// pageNum : 현제 페이지 번호
	// sortField : 정렬할 필드명
	// sortWay : 정렬 방법 (오름/내림 차순)
	public Page<Car> getCarPagingList(int pageNum, String state, String keyword, int pageSize, String sortField, String sortWay) {
		Pageable pageable = PageRequest.of(pageNum-1, pageSize, sortWay.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());
		
		switch(state) {
		case "company" :  
			return carRepository.findByCompany(keyword, pageable);
		case "category" : 
			return carRepository.findByCategory(keyword, pageable);
		case "fuelType" : 
			return carRepository.findByFuelType(keyword, pageable);
		case "plant" : 
			return carRepository.findByplant(keyword, pageable);
		case "search" :	// 검색일 때 처리
			return carRepository.findByNameContainingOrCompanyContainingOrCategoryContainingOrderByNameAsc(keyword, keyword, keyword, pageable);
		default:
			return carRepository.findAll(pageable);
		}
		
	}

	
	
	// 차량ID에 해당하는 차량 1건 조회
	public Car getCarByCarId(String carId) {
		return this.carRepository.findByCarId(carId);
	}
	
	
	
	// main(cars) 슬라이더에서 사용할 메서드
//	public List<Car> getcarListSlider(String slider) {
//		switch(slider) {
//		case "main1" :
//			return this.carRepository.querySelectEachCategoryTopN(3);
//		case "main2" :
//			List<Car> b1 = this.carRepository.findTop5ByTitleContainingOrDescriptionContainingOrderByReleaseDateDesc("건강","건강");
//			List<Car> b2 = this.carRepository.findTop5ByTitleContainingOrDescriptionContainingOrderByReleaseDateDesc("요리","요리");
//			List<Car> b3 = this.carRepository.findTop5ByTitleContainingOrDescriptionContainingOrderByReleaseDateDesc("사랑","사랑");
//			List<Car> sliderList2 = new ArrayList<Car>();
//			sliderList2.addAll(b1);
//			sliderList2.addAll(b2);
//			sliderList2.addAll(b3);
//			return sliderList2;
//		default :
//			return null;
//		}
//	}
	
	// 상세보기에서(car) 사용하는 슬라이더
//	public List<Car> getCarListSlider2(String slider, Car car) {
//		switch(slider) {
//		case "detail1" :
//			List<Car> b1 = this.carRepository.findTop5ByAuthorOrderByReleaseDateDesc(car.getAuthor());
//			List<Car> b2 = this.carRepository.findTop5ByCategoryOrderByReleaseDateDesc(car.getCategory());
//			
//			List<Car> b3 = new ArrayList<>();
//			String word = car.getTitle();
//			String[] words = word.split(" ");
//			for(String w : words) {
//				b3.addAll(this.carRepository.findTop5ByTitleContainingOrderByReleaseDateDesc(w));
//			}
//			List<Car> sliderList3 = new ArrayList<>();
//			sliderList3.addAll(b1);
//			sliderList3.addAll(b2);
//			sliderList3.addAll(b3);
//			return sliderList3;
//		default :
//			return null;
//		}
//	}
	
	// ######################################################################################
	// [2] 관리자 기능
	
	// 차량 등록
	public void addNewCar(Car car) {
		this.carRepository.save(car);
	}
	
	
	// 전체 차량 목록 조회
	
	// 차량 1권 조회
	
	// 차량 정보 수정
	
	// 차량 삭제

}
