package com.sbproject.standalone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbproject.standalone.entity.Faq;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Long>{
	
	List<Faq>findByFaqQuestionContaining(String keyword);
	
	List<Faq>findByFaqAnswerContaining(String keyword);
	
	List<Faq> findByFaqQuestionContainingOrFaqAnswerContaining(String questionKeyword, String answerKeyword);
	
	
}
