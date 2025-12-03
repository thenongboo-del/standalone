package com.sbproject.standalone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbproject.standalone.entity.Notice;



@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long>{

}
