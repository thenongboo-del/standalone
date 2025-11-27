package com.sbproject.standalone.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sbproject.standalone.entity.QnaQuestion;

@Repository
public interface QnaQuestionRepository extends JpaRepository<QnaQuestion, Long> {

	Page<QnaQuestion>findAll(Pageable pageable);
	
	Page<QnaQuestion> findByTitleContaining(String keyword, Pageable pageable);
	
	Page<QnaQuestion> findByContentContaining(String keyword, Pageable pageable);
	
	final String SEARCH_QUESTION_ALL = 
		    "SELECT q FROM QnaQuestion q " +
		    "WHERE (:keyword IS NULL OR :keyword = '' " +
		    "OR q.title LIKE CONCAT('%', :keyword, '%') " +
		    "OR q.content LIKE CONCAT('%', :keyword, '%'))";
	@Query(value = SEARCH_QUESTION_ALL)
	Page<QnaQuestion> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
	
	Optional<QnaQuestion> findById(Long id);
	
}
