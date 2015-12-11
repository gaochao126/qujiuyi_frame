package com.jiuyi.frame.permission;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonElement;
import com.jiuyi.frame.constants.Constants;
import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.util.JsonUtil;

/**
 * 权限控制抽象父类，在AccessInterceptor中被调用，通过checkPermission方法判断权限，
 * 返回ServerResult如果isSuccess则代表有权限； 各子项目可以继承这个类并重写checkPermission方法
 * 并把类的Qualifier为“PermissionCheck”，找不到Qualifier为“PermissionCheck”的类，则代表没有权限控制
 * 
 * @author xutaoyang
 *
 */
public abstract class AbsPermissionCheck {

	public abstract ServerResult checkPermission(HttpServletRequest request, HttpServletResponse response, Object handler);

	/**
	 * 从request中获取参数，可获取parameter以及json中的参数
	 * 
	 * @param request
	 * @param paramName
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Object getParamInRequest(HttpServletRequest request, String paramName, Class<?> clazz) {
		/*
		 * 先从attributes里面查找参数，如果找不到再到json中查找，这样@JsonParam就可以包含@RequestParam的作用
		 */
		Object res = request.getParameter(paramName);
		if (res != null) {
			return res;
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

}
