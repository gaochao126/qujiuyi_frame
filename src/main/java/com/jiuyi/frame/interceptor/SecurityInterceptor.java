package com.jiuyi.frame.interceptor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jiuyi.frame.helper.Loggers;
import com.jiuyi.frame.util.WebUtil;

/**
 * @author xutaoyang @Date 2015年3月23日
 * 
 * @Description 基本的对访问的安全控制。可以防止访问洪流。
 *
 * @Copyright 2015 重庆玖壹健康管理有限公司
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter {

	private static final int SESSION_COUNT_LIMIT = 100; // 每个session在period时间内访问次数限制
	private static final int URL_COUNT_LIMIT = 10000; // 每个URL在PERIOD内被访问的次数限制
	private static final int IP_SESSION_LIMIT = 5000;// 每个IP下的session个数限制
	private static final int PERIOD = 500;
	private static final int CLEAR_TIME = 20 * 60 * 1000;// 二十分钟
	private ConcurrentHashMap<String, Count> sessionId_count = new ConcurrentHashMap<String, Count>();
	private ConcurrentHashMap<String, Count> url_count = new ConcurrentHashMap<String, Count>();
	private ConcurrentHashMap<String, Count> ip_count = new ConcurrentHashMap<String, Count>();

	private ScheduledExecutorService executor;

	@PostConstruct
	public void init() {
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleWithFixedDelay(new ClearRunnable(), 5000, 100, TimeUnit.MILLISECONDS);
		Loggers.info("<<SecurityInterceptor>> 安全检测开启");
	}

	@PreDestroy
	public void destroy() {
		executor.shutdownNow();
		Loggers.info("<<SecurityInterceptor>> 安全检测停止");
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String ip = WebUtil.getIP(request);
		ip_count.putIfAbsent(ip, new Count_IP_Session());
		if (ip_count.get(ip).overLimit(IP_SESSION_LIMIT)) {
			return false;
		}

		String url = request.getRequestURI();
		url_count.putIfAbsent(url, new Count());
		if (url_count.get(url).overLimit(URL_COUNT_LIMIT)) {
			return false;
		}

		String sessionId = WebUtil.getSessionId(request);
		sessionId_count.putIfAbsent(sessionId, new Count());
		if (sessionId_count.get(sessionId).overLimit(SESSION_COUNT_LIMIT)) {
			Loggers.errf("flood: sessionId is %s,url is %s, ip is %s", sessionId, url, ip);
			return false;
		}

		sessionId_count.get(sessionId).incCount(null);
		ip_count.get(ip).incCount(sessionId);
		url_count.get(url).incCount(null);
		return true;
	}

	private class ClearRunnable implements Runnable {
		@Override
		public void run() {
			try {
				clear(sessionId_count);
				clear(url_count);
				clear(ip_count);
			} catch (Throwable t) {
				Loggers.err("<<SecurityInterceptor>> ClearRunnable err", t);
			}
		}

		private void clear(ConcurrentHashMap<String, Count> id_count) {
			long now = System.currentTimeMillis();
			for (Iterator<Count> iter = id_count.values().iterator(); iter.hasNext();) {
				Count count = iter.next();
				count.tryReset(now);
				if (count.canBeClear(now)) {
					iter.remove();
				}
			}
		}
	}

	private class Count {
		AtomicInteger count;
		long updateTime;

		public Count() {
			count = new AtomicInteger(0);
			updateTime = System.currentTimeMillis();
		}

		/** 有较长一段时间没有更新了 */
		public boolean canBeClear(long now) {
			return (this.updateTime - now) >= CLEAR_TIME;
		}

		void incCount(String param) {
			this.count.incrementAndGet();
			this.updateTime = System.currentTimeMillis();
		}

		void tryReset(long now) {
			if ((now - updateTime) > PERIOD) {
				this.count.set(1);
				this.updateTime = System.currentTimeMillis();
			}
		}

		boolean overLimit(int limit) {
			return getCount() >= limit;
		}

		int getCount() {
			return this.count.get();
		}
	}

	private class Count_IP_Session extends Count {

		private Set<String> sessionIds = Collections.synchronizedSet(new HashSet<String>());

		void incCount(String sessionId) {
			super.incCount(sessionId);
			this.sessionIds.add(sessionId);
		}

		@Override
		void tryReset(long now) {
			super.tryReset(now);
			if ((now - updateTime) > PERIOD) {
				this.sessionIds.clear();
			}
		}

		@Override
		int getCount() {
			return this.sessionIds.size();
		}
	}
}
