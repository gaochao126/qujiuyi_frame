/**
 * 
 */
package com.jiuyi.frame.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jiuyi.frame.util.WebUtil;

/**
 * 
 * 用于分页功能。前端若传入了page参数，则默认在select的时候加上limit语句来分页
 * 
 * @author xutaoyang
 *
 */
public class PageInterceptor extends HandlerInterceptorAdapter {

	private static final int DEFAULT_PAGE_SIZE = 10;
	private static ThreadLocal<Page> pageVars = new ThreadLocal<>();

	public static Page getThreadVarPage() {
		return pageVars.get();
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Integer page = (Integer) WebUtil.getParamInRequest(request, "page", Integer.class);
		if (page != null) {
			Integer pageSize = (Integer) WebUtil.getParamInRequest(request, "pageSize", Integer.class);
			pageSize = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
			Page pageObj = new Page(page, pageSize);
			pageVars.set(pageObj);
		}
		return super.preHandle(request, response, handler);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		super.afterCompletion(request, response, handler, ex);
		pageVars.remove();
	}

	public class Page {
		private int page;
		private int pageSize;

		public Page(int page, int pageSize) {
			this.page = page;
			this.pageSize = pageSize;
		}

		public int getPage() {
			return page;
		}

		public void setPage(int page) {
			this.page = page;
		}

		public int getPageSize() {
			return pageSize;
		}

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}

	}
}
