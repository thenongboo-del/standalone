package com.sbproject.standalone.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sbproject.standalone.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	// 회원ID로 회원조회
	Optional<Member> findByMemberId(String memberId);
	Optional<Member> findByName(String name);
	
	// 회원정보 수정
	// nativeQuery = true  -> 테이블 기준으로 작성
	// nativeQuery = false -> 엔터티 기준으로 작성
	public final String UPDATE_MEMBER = "update member m inner join address a on m.address_id = a.id "
			+ " set name = :#{#member.name}, password = :#{#member.password}, phone = :#{#member.phone}, email = :#{#member.email}, "
			+ " gender = :#{#member.gender}, age = :#{#member.age}, country = :#{#member.address.country}, zipcode = :#{#member.address.zipcode}, "
			+ " basic_address = :#{#member.address.basicAddress}, detail_address = :#{#member.address.detailAddress} "
			+ " where member_id = :#{#member.memberId}";
	@Transactional
	@Modifying
	@Query(value = UPDATE_MEMBER, nativeQuery = true)
	void queryUpdateMember(@Param("member") Member member);
	
	// 회원탈퇴
	@Transactional
	void deleteByMemberId(String memberId);
}
