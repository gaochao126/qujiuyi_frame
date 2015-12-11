package com.jiuyi.frame.argsresolve;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.jiuyi.frame.exceptions.JiuyiRuntimeException;
import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.helper.ConstKeys;
import com.jiuyi.frame.util.StringUtil;
import com.jiuyi.frame.util.WebUtil;
import com.jiuyi.frame.zervice.user.IUserManager;
import com.jiuyi.frame.zervice.user.model.User;

/**
 * @Author: xutaoyang @Date: 下午1:39:36
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class ArgResolver implements HandlerMethodArgumentResolver {

	@Autowired(required = false)
	@Qualifier("UserManager")
	protected IUserManager userManager;

	@Autowired
	protected MethodParamService methodParamService;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return methodParamService.supportsParameter(parameter);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		MethodParamInfo methodParamInfo = methodParamService.getMethodParamInfo(parameter);
		if (methodParamInfo.isTokenUser) {// 需要解析为用户对象
			return resolveUser(webRequest, methodParamInfo);
		} else if (methodParamInfo.isParam) {// 从json中解析数据
			return resolveObj(webRequest, methodParamInfo, binderFactory);
		}
		// ===因为有supportsParameter方法保证进入，所以这里实际上不会执行到===
		return null;
	}

	/** 解析为用户对象 */
	private Object resolveUser(NativeWebRequest webRequest, MethodParamInfo methodParamInfo) {
		String token = (String) WebUtil.getParamInRequest(webRequest, ConstKeys.TOKEN, String.class);
		if (StringUtil.isNullOrEmpty(token)) {
			throw new JiuyiRuntimeException(ResultConst.NEED_PARAM_TOKEN);
		}
		User user = userManager.getUserByToken(token);
		if (user == null) {
			throw new JiuyiRuntimeException(ResultConst.NOT_LOGIN);
		}
		user.setAccess_token(token);
		user.setLastAccess();
		return user;
	}

	/** 从json中解析数据 */
	private Object resolveObj(NativeWebRequest webRequest, MethodParamInfo methodParamInfo, WebDataBinderFactory binderFactory) throws Exception {
		Object value = WebUtil.getParamInRequest(webRequest, methodParamInfo.paramName, methodParamInfo.paramGenericType);
		/** 类型转换 */
		if (binderFactory != null && value != null) {
			WebDataBinder binder = binderFactory.createBinder(webRequest, null, methodParamInfo.paramName);
			value = binder.convertIfNecessary(value, methodParamInfo.paramDataType, methodParamInfo.methodParameter);
		}
		return value;
	}
}
