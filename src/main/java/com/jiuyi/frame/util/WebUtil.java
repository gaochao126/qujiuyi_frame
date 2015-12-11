package com.jiuyi.frame.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.NativeWebRequest;

import com.google.gson.JsonElement;
import com.jiuyi.frame.constants.Constants;
import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.helper.Loggers;

/**
 * @author xutaoyang @Date 2015年3月23日
 * 
 * @Description 关于web访问的一些辅助方法
 *
 * @Copyright 2015 重庆玖壹健康管理有限公司
 */
public class WebUtil {

	private static Map<ResultConst, String> resultConst_json = new HashMap<>();

	static {
		for (ResultConst rc : ResultConst.values()) {
			resultConst_json.put(rc, JsonUtil.toJson(new ServerResult(rc)));
		}
	}

	public static String getIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
	}

	public static String getSessionId(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		return session.getId();
	}

	public static void writeResp(HttpServletResponse response, ResultConst resultConst) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		try (PrintWriter out = response.getWriter()) {
			out.append(resultConst_json.get(resultConst));
		} catch (IOException e) {
			Loggers.err("write resp err", e);
		}
	}

	public static void writeResp(HttpServletResponse response, ServerResult res) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		try (PrintWriter out = response.getWriter()) {
			out.append(JsonUtil.toJson(res));
		} catch (IOException e) {
			Loggers.err("write resp err", e);
		}
	}

	public static void redirectSilent(HttpServletResponse resp, String url) {
		try {
			resp.sendRedirect(url);
		} catch (IOException e) {
			Loggers.errf(e, "redirect err the url is:%s", url);
		}
	}

	public static void redirectErrPage(HttpServletResponse resp) {
		try {
			resp.sendRedirect("/resources/html/error.jsp");
		} catch (IOException e) {
			Loggers.err("redirect err", e);
		}
	}

	/**
	 * 查找native request中的参数，包括json，表单，cookie
	 * 
	 * @param request
	 * @param paramName
	 * @param paramGenericType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object getParamInRequest(NativeWebRequest request, String paramName, Type type) {
		Object res = request.getParameter(paramName);
		if (res != null) {
			return res;
		}
		Object nativeRequest = request.getNativeRequest();
		if (nativeRequest instanceof HttpServletRequest) {
			HttpServletRequest req = (HttpServletRequest) nativeRequest;
			Cookie[] cookies = req.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals(paramName)) {
						return cookie.getValue();
					}
				}
			}
		}
		Map<String, JsonElement> params = (Map<String, JsonElement>) request.getAttribute(Constants.JSON_PARAM_ATTR, NativeWebRequest.SCOPE_REQUEST);
		if (params == null) {
			return null;
		}
		JsonElement jsonElement = params.get(paramName);
		if (jsonElement == null) {
			return null;
		}
		return JsonUtil.fromJson(jsonElement, type);
	}

	/**
	 * 查找native request中的参数，包括json，表单，cookie
	 * 
	 * @param request
	 * @param paramName
	 * @param clazz
	 * @return
	 */
	public static Object getParamInRequest(NativeWebRequest request, String paramName, Class<?> clazz) {
		return getParamInRequest(request, paramName, (Type) clazz);
	}

	/**
	 * 查找servlet request中的参数，包括json，表单，cookie
	 * 
	 * @param request
	 * @param paramName
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object getParamInRequest(HttpServletRequest request, String paramName, Class<?> clazz) {
		/*
		 * 先从attributes里面查找参数，如果找不到再到json中查找，这样@JsonParam就可以包含@RequestParam的作用
		 */
		Object res = request.getParameter(paramName);
		if (res != null) {
			return res;
		}
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(paramName)) {
					return cookie.getValue();
				}
			}
		}
		Map<String, JsonElement> params = (Map<String, JsonElement>) request.getAttribute(Constants.JSON_PARAM_ATTR);
		if (params == null) {
			return null;
		}
		JsonElement jsonElement = params.get(paramName);
		if (jsonElement == null) {
			return null;
		}
		return JsonUtil.fromJson(jsonElement, clazz);
	}

	public static boolean containsParam(HttpServletRequest request, String paramName) {
		return getParamInRequest(request, paramName, Object.class) != null;
	}

}
