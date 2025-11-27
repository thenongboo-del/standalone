package com.sbproject.standalone.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbproject.standalone.entity.QnaQuestion;

@Repository
public interface QnaQuestionRepository extends JpaRepository<QnaQuestion, Long> {

	Page<QnaQuestion>findAll(Pageable pageable);
	
	Page<QnaQuestion> findByTitleContaining(String keyword, Pageable pageable);
	
	Page<QnaQuestion> findByContentContaining(String keyword, Pageable pageable);

	
	
}
