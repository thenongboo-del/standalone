package com.sbproject.standalone.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String requestMessage;

    @CreationTimestamp
    @Column(name = "requested_at", nullable = false, updatable = false)
    private LocalDateTime requestedAt;	// 상담 신청일

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;	// 상담 배정일		

    @Column(name = "next_schedule")
    private LocalDateTime nextSchedule; // 상담 예정일

    // 신청고객: Member ManyToOne으로 매핑
    @JoinColumn(name = "customer_id", nullable = false)
    private String customerId;

    // 상담 딜러: ManyToOne (여러 상담이 한 딜러에 속함)
    @JoinColumn(name = "dealer_id")
    private String dealerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private ConsultationStatus status = ConsultationStatus.WAIT;

    @Column(name = "round_number")
    private Integer roundNumber = 1;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 32)
    private ConsultationType type;

    // endResult
    @Enumerated(EnumType.STRING)
    @Column(name = "end_type", length = 32)
    private ConsultationEndResult endResult;

    @Column(name = "vehicle_id")
    private Long vehicleId;		// 관심 차량 ID

    @Column(name = "internal_note", length = 2000)
    private String internalNote;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @UpdateTimestamp
    @Column(name = "end_date")
    private LocalDateTime endDate;		// 상담 종료일

    // 답변/상담 내용: 일대다 관계
    @OneToMany(mappedBy = "consultation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConsultationDetail> details = new ArrayList<>();

    // 상담 신청시 들어가는 메소드
    public void requestConsultation(ConsultationDetail d) {
    	this.requestedAt = LocalDateTime.now();
    	this.addDetail(d);
    }
    
    // 편의 메소드
    public void addDetail(ConsultationDetail d) {
        d.setConsultation(this);
        this.details.add(d);
    }

    public void removeDetail(ConsultationDetail d) {
        d.setConsultation(null);
        this.details.remove(d);
    }
}
