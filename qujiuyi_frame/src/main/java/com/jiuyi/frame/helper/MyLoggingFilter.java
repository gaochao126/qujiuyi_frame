package com.jiuyi.frame.helper;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jiuyi.frame.filter.LoggingFilter;

public class MyLoggingFilter extends LoggingFilter {

	private Set<String> except = new HashSet<>();

	public MyLoggingFilter() {
		this.except.add("/hospital");
		this.except.add("/department");
		this.except.add("/department_detail");
		this.except.add("/doctor");
		this.except.add("/articles");
		this.except.add("/article_new");
		this.except.add("/article_load");
		this.except.add("/disease_new");
		this.except.add("/diseases");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String uri = request.getRequestURI();
		if (uri.contains("resources") || except.contains(uri)) {
			filterChain.doFilter(request, response);
			return;
		}
		super.doFilterInternal(request, response, filterChain);
	}

}
