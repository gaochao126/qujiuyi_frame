package com.jiuyi.frame.base.accesscheck;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;

import com.jiuyi.frame.annotations.AccessCheck;
import com.jiuyi.frame.annotations.TokenUser;
import com.jiuyi.frame.base.ControllerBase;
import com.jiuyi.frame.front.ResultConst;

/**
 * @author xutaoyang @Date 2015年3月23日
 * 
 * @Description 访问权限管理类，在服务启动的时候会注册所有url的访问权限。每个来自客户端的请求都会先通过拦截器才能继续。
 *
 * @Copyright 2015 重庆玖壹健康管理有限公司
 */
public class AccessCheckService {

	private Map<String, CheckType> url_checkType = new HashMap<String, CheckType>(200);
	private static final AccessCheckService instance = new AccessCheckService();

	public static AccessCheckService instance() {
		return instance;
	}

	public void addLoginCheck(ControllerBase controller) {
		// String cmd = controller.getCMD();
		Class<? extends ControllerBase> clazz = controller.getClass();
		AccessCheck loginCheck = clazz.getAnnotation(AccessCheck.class);
		CheckType clsCheckType = loginCheck == null ? CheckType.VISITOR : loginCheck.checkType();

		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			RequestMapping reqMapping = method.getAnnotation(RequestMapping.class);
			if (reqMapping == null) {
				continue;
			}
			String[] mappings = reqMapping.value();
			AccessCheck mthdLgnChk = method.getAnnotation(AccessCheck.class);
			// 如果方法上面没有声明，则根据类的声明来判定。只要方法上声明需要检测，根据就近原则，就需要检测
			CheckType mthdChk = mthdLgnChk == null ? clsCheckType : mthdLgnChk.checkType();
			if (needLogin(method)) {
				mthdChk = CheckType.Doctor;
			}
			if (mthdChk.needCheck()) {
				for (String mapping : mappings) {
					String checkUrl = mapping;
					synchronized (url_checkType) {
						url_checkType.put(checkUrl, mthdChk);
					}
				}
			}
		}
	}

	private boolean needLogin(Method method) {
		Annotation[][] annotations = method.getParameterAnnotations();
		for (Annotation[] annotationArr : annotations) {
			for (Annotation anno : annotationArr) {
				if (anno.annotationType().equals(TokenUser.class)) {
					return true;
				}
			}
		}
		return false;
	}

	public ResultConst checkAccess(HttpServletRequest request) {
		String uri = request.getRequestURI();
		if (!url_checkType.containsKey(uri)) {
			return ResultConst.SUCCESS;
		}
		CheckType checkType = url_checkType.get(uri);
		if (!checkType.needCheck()) {
			return ResultConst.SUCCESS;
		}
		return checkType.getCheckHandler().checkAccess(request);
	}

}
