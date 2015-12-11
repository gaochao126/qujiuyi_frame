package com.jiuyi.frame.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jiuyi.frame.stats.StatsService;

/**
 * @Author: xutaoyang @Date: 下午3:26:21
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class TraceInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	StatsService statsService;

	private static final String REQUEST_STAT_KEY = "jiuyi_request_stat";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		long now = System.currentTimeMillis();
		request.setAttribute(REQUEST_STAT_KEY, now);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
		Long startTime = (Long) request.getAttribute(REQUEST_STAT_KEY);
		if (startTime == null) {
			return;
		}
		long now = System.currentTimeMillis();
		long useTime = now - startTime;
		statsService.postHandle(request.getRequestURI(), useTime);
	}
}