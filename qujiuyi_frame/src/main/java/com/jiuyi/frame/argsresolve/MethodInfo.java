package com.jiuyi.frame.argsresolve;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import com.jiuyi.frame.annotations.MayEmpty;
import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.annotations.TokenUser;
import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.helper.ConstKeys;
import com.jiuyi.frame.helper.Loggers;
import com.jiuyi.frame.util.StringUtil;
import com.jiuyi.frame.util.WebUtil;
import com.jiuyi.frame.zervice.user.IUserManager;
import com.jiuyi.frame.zervice.user.model.User;

/**
 * @Author: xutaoyang @Date: 下午1:35:56
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class MethodInfo {

	public Set<String> paramNames = new HashSet<>();// 必须要有的参数
	public TokenUser tokenUser;

	public MethodInfo(RequestMappingInfo mappingInfo, HandlerMethod handlerMethod) {
		MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
		for (MethodParameter methodParameter : methodParameters) {
			Param paramAnnotation = methodParameter.getParameterAnnotation(Param.class);
			if (paramAnnotation != null && !methodParameter.hasParameterAnnotation(MayEmpty.class)) {
				String paramName = paramAnnotation.value();
				paramName = !StringUtil.isNullOrEmpty(paramName) ? paramName : methodParameter.getParameterName();
				this.paramNames.add(paramName);
			}
			if (methodParameter.hasParameterAnnotation(TokenUser.class)) {
				this.tokenUser = methodParameter.getParameterAnnotation(TokenUser.class);
			}
		}
	}

	/**
	 * 审查访问参数，包括：验证是否登录，用户状态，以及参数合法性
	 * 
	 * @throws Exception
	 */
	public ServerResult checkAccess(IUserManager userManager, HttpServletRequest request) throws Exception {
		if (tokenUser != null) {
			String token = (String) WebUtil.getParamInRequest(request, ConstKeys.TOKEN, String.class);
			if (StringUtil.isNullOrEmpty(token)) {
				return new ServerResult(ResultConst.NEED_PARAM_TOKEN);
			}
			if (userManager == null) {
				throw new Exception("没有找到UserManager的实现，但是有tokenUser需要解析。请实现IUserManager并Qualifier为UserManager");
			}
			User user = userManager.getUserByToken(token);
			if (user == null) {
				return new ServerResult(ResultConst.NOT_LOGIN);
			}
		}
		for (String param : paramNames) {
			if (!WebUtil.containsParam(request, param)) {
				ServerResult res = new ServerResult(ResultConst.PARAM_LACK);
				if (Loggers.isDebugEnabled()) {
					res.put("param", param);
				}
				return res;
			}
		}
		return new ServerResult();
	}
}
