package com.jiuyi.frame.argsresolve;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;

import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.annotations.TokenUser;

/**
 * @Author: xutaoyang @Date: 下午5:35:56
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class ParamAnnotationService {

	private Map<Class<? extends Annotation>, AnnotationInfo> anno_info = new HashMap<>();

	@PostConstruct
	public void init() {
		anno_info.put(TokenUser.class, null);
		anno_info.put(Param.class, null);
	}

	public boolean containMyAnno(MethodParameter parameter) {
		for (Class<? extends Annotation> annoClass : anno_info.keySet()) {
			if (parameter.hasParameterAnnotation(annoClass)) {
				return true;
			}
		}
		return false;
	}

	private class AnnotationInfo {

	}
}
