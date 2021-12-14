package com.edmwat.switchlink.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class CustomLogoutFilter extends LogoutFilter{

	public CustomLogoutFilter(LogoutSuccessHandler logoutSuccessHandler, LogoutHandler[] handlers) {
		super(logoutSuccessHandler, handlers);
		
	}

		
}