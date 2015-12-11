package com.jiuyi.frame.argsresolve;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @Author: xutaoyang @Date: 上午11:26:13
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class MethodParamService implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private RequestMappingHandlerMapping handlerMapping;

	@Autowired
	ParamAnnotationService paramAnnotationService;

	private volatile boolean inited = false;
	private Map<Method, MethodInfo> handler_method = new HashMap<Method, MethodInfo>();
	private Map<MethodParameter, MethodParamInfo> param_paramInfo = new HashMap<MethodParameter, MethodParamInfo>();

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		tryGenRequestMappingInfo();
	}

	public void tryGenRequestMappingInfo() {
		/** ContextRefreshedEvent 事件有可能抛出多次（很少情况下），所以这里判断一下，以免多次初始化 **/
		if (inited) {
			return;
		}
		inited = true;

		/** 拿到所有controller里request的方法，解析出它们的信息，以及它们的参数信息。自定义参数解析器 里面就不用重复解析 **/
		Map<RequestMappingInfo, HandlerMethod> reqMapping_method = handlerMapping.getHandlerMethods();

		for (Entry<RequestMappingInfo, HandlerMethod> entry : reqMapping_method.entrySet()) {
			/** 方法信息 **/
			RequestMappingInfo mappingInfo = entry.getKey();
			HandlerMethod handlerMethod = entry.getValue();
			MethodInfo reqMethodInfo = new MethodInfo(mappingInfo, handlerMethod);
			this.handler_method.put(handlerMethod.getMethod(), reqMethodInfo);

			/** 参数信息 **/
			MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
			for (MethodParameter methodParameter : methodParameters) {
				if (!paramAnnotationService.containMyAnno(methodParameter)) {
					continue;
				}
				MethodParamInfo methodParamInfo = new MethodParamInfo(methodParameter);
				this.param_paramInfo.put(methodParameter, methodParamInfo);
			}
		}
	}

	public MethodInfo getMethodInfo(HandlerMethod handlerMethod) {
		return this.handler_method.get(handlerMethod.getMethod());
	}

	/** controller request方法里面的参数信息 */
	public MethodParamInfo getMethodParamInfo(MethodParameter methodParameter) {
		return this.param_paramInfo.get(methodParameter);
	}

	public boolean supportsParameter(MethodParameter parameter) {
		return this.paramAnnotationService.containMyAnno(parameter);
	}
}
