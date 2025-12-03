package com.sbproject.standalone.config;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// 1. 로그인한 사용자의 권한을 획득
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		
		// 2. 권한에 따른 이동 경로 설정
		String redirectUrl = "/";
		
		for(GrantedAuthority authority : authorities) {
			if(authority.getAuthority().equals("ROLE_ADMIN")) {			// admin
				redirectUrl = "/admin/main";
			} else if(authority.getAuthority().equals("ROLE_DEALER")) {			// dealer
				redirectUrl = "/dealer/main";
			} else {		// normal user
				redirectUrl = "/";
			}
		}
		
		// 3. 경로 리다이렉션
		response.sendRedirect(request.getContextPath() + redirectUrl);
		
		
	}

}
