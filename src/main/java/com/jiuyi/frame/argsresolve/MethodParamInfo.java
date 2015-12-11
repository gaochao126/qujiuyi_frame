package com.jiuyi.frame.argsresolve;

import java.lang.reflect.Type;

import org.springframework.core.MethodParameter;

import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.annotations.TokenUser;
import com.jiuyi.frame.util.StringUtil;

/**
 * @Author: xutaoyang @Date: 上午11:06:36
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class MethodParamInfo {

	/** 注解相关 **/
	public final boolean isTokenUser;
	public final boolean isParam;
	public final boolean isPage;
	public final TokenUser tokenUser;

	/** 参数相关 **/
	public final String paramName;
	public final Type paramGenericType;
	public final Class<?> paramDataType;
	public MethodParameter methodParameter;

	public MethodParamInfo(MethodParameter methodParameter) {
		this.methodParameter = methodParameter;
		this.isParam = methodParameter.hasParameterAnnotation(Param.class);
		this.tokenUser = methodParameter.getParameterAnnotation(TokenUser.class);
		this.isTokenUser = methodParameter.hasParameterAnnotation(TokenUser.class);
		this.paramGenericType = methodParameter.getGenericParameterType();
		Param annoParam = methodParameter.getParameterAnnotation(Param.class);
		String needParamName = annoParam == null ? null : annoParam.value();
		this.paramName = StringUtil.isNullOrEmpty(needParamName) ? methodParameter.getParameterName() : needParamName;
		this.paramDataType = methodParameter.getParameterType();
		this.isPage = annoParam == null ? false : annoParam.page();
	}
}
