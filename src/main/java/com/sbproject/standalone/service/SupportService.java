package com.sbproject.standalone.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sbproject.standalone.entity.QnaQuestion;
import com.sbproject.standalone.repository.QnaAnswerRepository;
import com.sbproject.standalone.repository.QnaQuestionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SupportService {

	private final QnaQuestionRepository qnaQuestionRepository;

	public Page<QnaQuestion> getQnaList(int pageNum, String searchFor, String keyword, int pageSize, String sortField, String sortWay) {
		Pageable pageable = PageRequest.of(pageNum-1, pageSize, sortWay.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());
		
		switch(searchFor) {
		case "title" :  
			return qnaQuestionRepository.findByTitleContaining(keyword, pageable);
		case "content" : 
			return qnaQuestionRepository.findByContentContaining(keyword, pageable);
		case "all" :
			return qnaQuestionRepository.searchByKeyword(keyword, pageable);
		default:
			return qnaQuestionRepository.findAll(pageable);
		}
		
	}
	
//	질문 id로 질문 선택
	public QnaQuestion getQuestionByquestionId(Long id) {
		return qnaQuestionRepository.findById(id).get(); 
	}
	
//	질문 등록
	public void createQuestion(QnaQuestion question) {
		qnaQuestionRepository.save(question);
	}
	
//	질문 1건 조회
	public QnaQuestion selectQuestionById(Long id) {
		return qnaQuestionRepository.findById(id).get();
	}
	
//	질문 수정
	public void updateQuestion(QnaQuestion question) {
		qnaQuestionRepository.save(question);
	}
	
//	질문 삭제
	public void removeQuestionById(Long id) {
		qnaQuestionRepository.deleteById(id);
	}
	
	
	
}
