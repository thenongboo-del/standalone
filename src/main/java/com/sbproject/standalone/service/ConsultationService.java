package com.sbproject.standalone.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sbproject.standalone.entity.Consultation;
import com.sbproject.standalone.entity.ConsultationStatus;
import com.sbproject.standalone.entity.ConsultationType;
import com.sbproject.standalone.repository.ConsultationRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ConsultationService {
	
	private final ConsultationRepository consultRepository;
	
	// 상담신청
	public void requestConsultation(Consultation consult) {
		consultRepository.save(consult);
	}
	
	// 상담타입별 리스트
	public List<Consultation> selectByType(ConsultationType type) {
		return consultRepository.findByType(type);
	}
	
	//  상담상태별 리스트
	public List<Consultation> selectBystatus(ConsultationStatus status) {
		return consultRepository.findByStatus(status);
	}
	
	// wait 상담신청 리스트
	public List<Consultation> selectBystatusAndType(ConsultationStatus status, ConsultationType type) {
		return consultRepository.findByStatusAndType(status, type);
	}
	
	
	// 고객 아이디로 신청된 상담조회
	public List<Consultation> findByMemberId(String memberId) {
		return consultRepository.findByCustomerId(memberId);
	}

	
	// 선택한 상담 1개 조회(상담 아이디로)
	public Consultation selectByConsultId(Long id) {
		return consultRepository.findById(id).get();
	}

	
	public void saveConsultation(Consultation c) {
		consultRepository.save(c);
	}
}
