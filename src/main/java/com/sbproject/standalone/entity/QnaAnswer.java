package com.sbproject.standalone.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "qna_answer")
public class QnaAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                // 답변 고유 ID

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QnaQuestion question;   // 어떤 질문에 대한 답변인지

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;         // 답변 내용

    @Column(nullable = false)
    private String writer;          // 답변 작성자 (관리자나 회원)

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
