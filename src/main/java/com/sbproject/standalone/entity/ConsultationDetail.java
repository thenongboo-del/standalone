package com.sbproject.standalone.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "consultation_details",
indexes = {
  @Index(name = "idx_detail_consultation", columnList = "consultation_id"),
  @Index(name = "idx_detail_date", columnList = "consulted_at")
})
public class ConsultationDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                // 상담내용 아이디 (PK)

    // 연결되는 상담 (ManyToOne)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_id", nullable = false)
    private Consultation consultation;

    @CreationTimestamp
    @Column(name = "consulted_at", nullable = false, updatable = false)
    private LocalDateTime consultedAt; // 상담일

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;         // 상담내용 (텍스트 또는 HTML)

    @Column(name = "round_number")
    private Integer roundNumber;    // 상담회차 (상위 상담의 회차와 동일하게 관리 가능)

    // 상담 타입(옵션, 상세 수단)
    @Column(name = "contact_method")
    private ConsultationStatus consultationType;


}
