package com.sbproject.standalone.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sbproject.standalone.entity.Member;
import com.sbproject.standalone.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

/*
★★★★★
< 스프링 시큐리티가 관리하고, 로그인시에 사용하는 서비스 클래스 >
- 로그인하는 사용자의 인증을 담당하는 클래스
- 반드시 UserDetailsService 인터페이스를 구현해야 함 -> 로그인시에 사용하게 됨
*/

@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {
	
	private final MemberRepository memberRepository;
	
	// ★★★★★
	// 로그인할 때 회원 인증에 사용되는 메서드
	@Override
	public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
		Optional<Member> _member = memberRepository.findByMemberId(memberId);
		
		if(_member.isEmpty()) {
			throw new UsernameNotFoundException("회원 ID를 찾을 수 없습니다.");
		}
		
		Member member = _member.get();
		
		// 로그인을 할 때 전달받은 회원 ID를 사용하여 비밀번호가 일치하는지를 검사하는 User 객체를 러턴함.
		return User.builder().username(member.getMemberId()).password(member.getPassword())
				.roles(member.getRole().toString()).build();
	}

	// 회원가입
	public void saveMember(Member member) {
		memberRepository.save(member);
	}
	
	// 회원ID로 회원조회
	public Member findByMemberId(String memberId) {
		return memberRepository.findByMemberId(memberId).get();
	}
	
	// 회원정보 수정
	public Member updateMember(Member member) {
		memberRepository.queryUpdateMember(member);
		return memberRepository.findByMemberId(member.getMemberId()).get();
	}
	
	// 회원탈퇴
	public void deleteMember(String memberId) {
		memberRepository.deleteByMemberId(memberId);
	}
	
	
	// 유저 이름으로 찾기
	public Optional<Member> selectMemberByName(String name) {
		return memberRepository.findByName(name);
	}
	
	// 모든 유저 조회
	public List<Member> selectAllMember() {
		return memberRepository.findAll();
	}
	
}
