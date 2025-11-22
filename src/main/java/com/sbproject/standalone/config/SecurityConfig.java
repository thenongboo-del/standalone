package com.sbproject.standalone.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	// 비밀번호 암호화
	@Bean
	protected PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// 스프링 시큐리티에서 관리하는 사용자(관리자)를 등록
	// 관리자명: admin, 비밀번호: admin1234, 역할: ADMIN
	// 수정 필요: 주석처리하지 않으면 filterChain() 메서드 실행 불가
	/*
	@Bean
	protected UserDetailsService users() {
		UserDetails admin = User.builder()
				.username("admin")
				.password(passwordEncoder().encode("admin1234"))
				.roles("ADMIN")
				.build();
		return new InMemoryUserDetailsManager(admin);
	}
	*/
	
	
	@Autowired
	private CustomLoginSuccessHandler customSuccessHandler;
	
	// 1. ADMIN 역할을 가진 사용자만 admin 페이지 접속
	// manager 권한을 가진 사용자만, dealer 페이지 접속
	// 2. 로그인 기본 경로, 성공했을 때의 경로, 실패했을 때의 경로, 로그인 처리에 사용되는 파라미터명(name으로 전달되는 이름) 설정 
	// 3. 로그아웃 기본 경로, 성공했을 때의 경로
	// CustomLoginSuccessHandler -> 로그인 했을때 스프링 시큐리티가 작동 할 수 있도록 여러 설정을 해놓는 클래스
	// MemberService 에서 implements UserDetailsService 하여 실제 로그인시 작동하는 메서드를 구현한다.
	
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
			// 사용자 권한에 따른 접근 권한 설정
			// - ADMIN 권한 설정
		// 어드민, 매니저만 접속할 수 있게 하는 코드 
			.authorizeHttpRequests(authorizeRequests -> authorizeRequests
				.requestMatchers("/admin/*").hasRole("ADMIN")		// admin authority
				.requestMatchers("/dealer/*").hasRole("DEALER")	// dealer authority
				.anyRequest().permitAll())
			// 로그인 처리
			.formLogin(formLogin -> formLogin
				.loginPage("/login")
				.loginProcessingUrl("/login")
				.defaultSuccessUrl("/")
				.successHandler(customSuccessHandler)
				.failureUrl("/loginfailed")
				.usernameParameter("username")
				.passwordParameter("password"))
			// 로그아웃 처리
			.logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/"));
		
		return http.build();
	}
	
	// 스프링 시큐리티가 회원 인증시에 인증과 권한을 부여하는 프로세스를 처리하는 메서드
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) 
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
}
