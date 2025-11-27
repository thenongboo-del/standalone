package com.sbproject.standalone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbproject.standalone.entity.QnaAnswer;

@Repository
public interface QnaAnswerRepository extends JpaRepository<QnaAnswer, Long> {

}
