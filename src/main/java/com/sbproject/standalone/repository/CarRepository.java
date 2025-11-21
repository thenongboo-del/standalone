package com.sbproject.standalone.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sbproject.standalone.entity.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {				
	
	// 도서ID를 통한 상세보기 (1건)
	Car findByCarId(String CarId);
	
	// 전체 도서 목록 조회 -> paging
	Page<Car> findAll(Pageable pageable);
	
	// 필드별로 도서 목록 조회하는 메서드 (최근 출판일 순으로 정렬)
	// 회사별 차량 목록
	Page<Car> findByCompany(String keyword, Pageable pageable);
		
	// -차량별 차량 목록
	Page<Car> findByCategory(String keyword, Pageable pageable);
	
	// -연료 타입별 차량 목록
	Page<Car> findByFuelType(String keyword, Pageable pageable);
	
	// -해외 공장별 차량목록
	Page<Car> findByplant(String keyword, Pageable pageable);
		

	// 검색을 통한 도서 목록 조회
	// 차량명, 제조사, 차총별 3가지 필드로 조회.
	Page<Car> findByNameContainingOrCompanyContainingOrCategoryContainingOrderByNameAsc(String keyword1, String keyword2, String keyword3, Pageable pageable);
	
	// 메인의 상단 슬라이더
	// books.html
	// 출판일순으로 정렬한 10개의 정보를 가져오라는 뜻 (슬라이더)
//	final String SELECT_top3_By_Category= 
//			"select * from (select *, row_number() over (partition by category order by release_date desc ) as rn from book) as ranking where ranking.rn <= :count";
//	@Query(value=SELECT_top3_By_Category , nativeQuery=true)
//	List<Car> querySelectEachCategoryTopN(@Param("count") int count);
	
	
	// 메인의 하단 슬라이더 - 추천상품 (건강 요리 사랑 제목 또는 내용에서 조회해서 5건 씩)
	// 제목, 내용에서 조회
	// books.html	
//	public List<Car> findTop5ByTitleContainingOrDescriptionContainingOrderByReleaseDateDesc(String keyword1, String keyword2);
	
	
	// 상세 하단 슬라이드
	// 중복데이터 제거 필요
//	public List<Car> findTop5ByAuthorOrderByReleaseDateDesc(String author);
//	
//	public List<Car> findTop5ByCategoryOrderByReleaseDateDesc(String category);
//	
//	public List<Car> findTop5ByTitleContainingOrderByReleaseDateDesc(String category);
	
	
	// 내가 짠코드 - 검색
	/*
	// 제목
	public final String SEARCH_TITLE=  "select * from book where title like concat('%', :search_keyword, '%') order by release_date";
	public String SEARCH_TITLE_COUNT = "select count(*) from book where title like concat('%', :search_keyword, '%')";
	@Query(value = SEARCH_TITLE, countQuery = SEARCH_TITLE_COUNT, nativeQuery = true)
	Page<Book> selectByTitleWithKeyword (@Param("search_keyword")String searchKeyword, Pageable pageable);
	
	// 내용
	public final String SEARCH_DESCRIPTION=  "select * from book where description like concat('%', :search_keyword, '%') order by release_date";
	public String SEARCH_DESCRIPTION_COUNT = "select count(*) from book where description like concat('%', :search_keyword, '%')";
	@Query(value = SEARCH_DESCRIPTION, countQuery = SEARCH_TITLE_COUNT, nativeQuery = true)
	Page<Book> selectByDescriptionWithKeyword (@Param("search_keyword")String searchKeyword, Pageable pageable);
	
	// 저자
	public final String SEARCH_AUTHOR=  "select * from book where author like concat('%', :search_keyword, '%') order by release_date";
	public String SEARCH_AUTHOR_COUNT = "select count(*) from book where author like concat('%', :search_keyword, '%')";
	@Query(value = SEARCH_AUTHOR, countQuery = SEARCH_AUTHOR_COUNT, nativeQuery = true)
	Page<Book> selectByAuthorWithKeyword (@Param("search_keyword")String searchKeyword, Pageable pageable);
	
	// 출판사
	public final String SEARCH_PUBLISHER=  "select * from book where publisher like concat('%', :search_keyword, '%') order by release_date";
	public String SEARCH_PUBLISHER_COUNT = "select count(*) from book where publisher like concat('%', :search_keyword, '%')";
	@Query(value = SEARCH_PUBLISHER, countQuery = SEARCH_PUBLISHER_COUNT, nativeQuery = true)
	Page<Book> selectByPublisherWithKeyword (@Param("search_keyword")String searchKeyword, Pageable pageable);
	*/
	

}
