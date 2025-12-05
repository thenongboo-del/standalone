package com.sbproject.standalone.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbproject.standalone.entity.Consultation;
import com.sbproject.standalone.entity.ConsultationStatus;
import com.sbproject.standalone.entity.ConsultationType;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long>{

	// 고객 각자의 진행중인 상담 건수
	List<Consultation> findByCustomerId(String memberId);
	
	// 딜러 입장에서 wiat 상태이고 buy 상담인 리스트
	List<Consultation> findByStatusAndType(ConsultationStatus status, ConsultationType type);
	
	// 타입별 상담 리스트
	List<Consultation> findByType(ConsultationType type);
	
	// 상태별 상담 리스트
	List<Consultation> findByStatus(ConsultationStatus status);
	
	// 스케줄러를 위한
	List<Consultation> findByNextScheduleBetweenAndStatusNot(LocalDateTime start, LocalDateTime end, ConsultationStatus status);
	
}
