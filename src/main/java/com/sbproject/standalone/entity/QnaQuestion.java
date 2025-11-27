package com.sbproject.standalone.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "qna_question")
public class QnaQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                // 질문 고유 ID

    @Column(nullable = false, length = 200)
    private String title;           // 질문 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;         // 질문 내용

    @Column(nullable = false)
    private String writer;          // 작성자 이름 (또는 회원 ID)

    @Column
    private String email;           // 작성자 이메일 (선택)

    @Column(updatable = false)
    private LocalDateTime createdAt;  // 작성일

    @Column
    private LocalDateTime updatedAt;  // 수정일

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<QnaAnswer> answers;  // 답변 리스트

    @PrePersist
    public void timeSet() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}