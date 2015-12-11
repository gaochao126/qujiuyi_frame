package com.jiuyi.frame.interceptor;

import java.io.IOException;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.google.gson.JsonElement;
import com.jiuyi.frame.argsresolve.MethodInfo;
import com.jiuyi.frame.argsresolve.MethodParamService;
import com.jiuyi.frame.constants.Constants;
import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.helper.Loggers;
import com.jiuyi.frame.permission.AbsPermissionCheck;
import com.jiuyi.frame.util.JsonUtil;
import com.jiuyi.frame.util.StringUtil;
import com.jiuyi.frame.util.WebUtil;
import com.jiuyi.frame.zervice.user.IUserManager;

/**
 * 
 * @author xutaoyang
 *
 * @Date 2015年3月23日
 * 
 * @Description 访问权限控制类
 *
 * @Copyright 2015 重庆玖壹健康管理有限公司
 */
public class AccessInterceptor extends HandlerInterceptorAdapter {

	@Autowired(required = false)
	@Qualifier("UserManager")
	protected IUserManager userManager;

	@Autowired(required = false)
	@Qualifier("PermissionCheck")
	protected AbsPermissionCheck permisionCheck;

	@Autowired
	protected MethodParamService methodParamService;

	@PostConstruct
	public void init() throws Exception {
		/* userManager 用于解析TokenUser，若无user，可以空实现 */
		if (userManager == null) {
			Loggers.warn("user manager qualified by UserManager is not found");
		}
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		/* 访问资源 */
		if (handler instanceof ResourceHttpRequestHandler) {
			return true;
		}

		/* 解析json参数 */
		ServerResult res = checkAccess(request, response, handler);
		if (!res.isSuccess()) {
			WebUtil.writeResp(response, res);
			return false;
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		super.afterCompletion(request, response, handler, ex);
		if (ex != null) {
			WebUtil.writeResp(response, ResultConst.SERVER_ERR);
		}
	}

	protected ServerResult checkAccess(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		/* 解析json参数 */
		ServerResult parseRes = parseRequestBody(request);
		if (!parseRes.isSuccess()) {
			return parseRes;
		}
		/* 权限控制 */
		ServerResult permissionRes = permissionCheck(request, response, handler);
		if (!permissionRes.isSuccess()) {
			return permissionRes;
		}

		/* 参数验证 */
		ServerResult paramCheck = checkParam(request, response, handler);
		if (!paramCheck.isSuccess()) {
			return paramCheck;
		}
		return new ServerResult();
	}

	/**
	 * 解析json参数为Map<String,JsonElement>
	 * 
	 * @param request
	 * @return
	 */
	protected ServerResult parseRequestBody(HttpServletRequest request) {
		/* 只解析json和jsonp */
		if (!contentTypeIsJson(request)) {
			return new ServerResult();
		}
		String json = null;
		try {
			json = IOUtils.toString(request.getInputStream(), "UTF-8");
		} catch (IOException e1) {
			return new ServerResult(ResultConst.PARAM_ERROR);
		}
		if (StringUtil.isNullOrEmpty(json)) {
			return new ServerResult(ResultConst.SUCCESS);
		}
		HashMap<String, JsonElement> params = null;
		try {
			params = JsonUtil.parseToElement(json);
		} catch (Exception e) {
			Loggers.err("parse req body param err", e);
		}
		if (params != null) {
			request.setAttribute(Constants.JSON_PARAM_ATTR, params);
		}
		return new ServerResult();
	}

	/**
	 * 权限验证
	 * 
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	protected ServerResult permissionCheck(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (permisionCheck == null) {
			return new ServerResult();
		}
		ServerResult checkResult = permisionCheck.checkPermission(request, response, handler);
		/* 这个result是子项目的permission check返回的，可能为空 */
		if (checkResult == null) {
			Exception e = new Exception("权限验证结果为null，请返回正确的checkResult");
			Loggers.err("permission check result is null", e);
			throw e;
		}
		return checkResult;
	}

	/**
	 * 参数验证
	 * 
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	protected ServerResult checkParam(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		MethodInfo reqMethodInfo = null;
		if (handler instanceof HandlerMethod) {
			HandlerMethod method = (HandlerMethod) handler;
			reqMethodInfo = methodParamService.getMethodInfo(method);
		}
		ServerResult res = new ServerResult();
		/* 参数验证 */
		if (reqMethodInfo != null) {
			res = reqMethodInfo.checkAccess(userManager, request);
		}
		return res;
	}

	/**
	 * 如果contentType是json，return true，反之false
	 * 
	 * @param req
	 * @return
	 */
	private boolean contentTypeIsJson(HttpServletRequest req) {
		String contentType = req.getContentType();
		// json or jsonp
		return !StringUtil.isNullOrEmpty(contentType) && (contentType.startsWith("application/json") || contentType.startsWith("application/javascript"));
	}
}
