package com.jiuyi.frame.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.constants.Constants;
import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.util.JsonUtil;
import com.jiuyi.frame.util.StringUtil;
import com.jiuyi.frame.zervice.user.IUserManager;
import com.jiuyi.frame.zervice.user.model.User;

@Controller
public class ApiController {

	@Autowired
	private ManagerService manager;

	@Autowired(required = false)
	private IUserManager userManager;

	@SuppressWarnings("unchecked")
	@RequestMapping("/api")
	@ResponseBody
	private Object api(@Param("cmd") String cmd, HttpServletRequest request) {
		MethodHandler handler = manager.getHandler(cmd);
		if (handler == null) {
			return new ServerResult(ResultConst.URL_ERROR);
		}
		Map<String, JsonElement> params = (Map<String, JsonElement>) request.getAttribute(Constants.JSON_PARAM_ATTR);
		String token = JsonUtil.fromJson(params.get(Constants.TOKEN), String.class);
		if (handler.needUser) {
			if (StringUtil.isNullOrEmpty(token)) {
				return new ServerResult(ResultConst.NEED_PARAM_TOKEN);
			}
			User user = userManager == null ? null : userManager.getUserByToken(token);
			if (handler.needUser && user == null) {
				return new ServerResult(ResultConst.NEED_PARAM_TOKEN);
			}
		}
		JsonElement paramsEle = params.containsKey("params") ? params.get("params") : new JsonObject();
		Object arg = JsonUtil.fromJson(paramsEle, handler.paramType);
		Object res = handler.invoke(arg);
		return res;
	}

}
