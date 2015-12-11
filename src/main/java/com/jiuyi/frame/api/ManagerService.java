package com.jiuyi.frame.api;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ManagerService {

	@Autowired
	private ApplicationContext applicationContext;

	private Map<String, MethodHandler> cmd_handler = new HashMap<>();

	@PostConstruct
	public void init() {
		Map<String, Object> managers = applicationContext.getBeansWithAnnotation(Service.class);
		for (Object manager : managers.values()) {
			Method[] methods = manager.getClass().getMethods();
			for (Method method : methods) {
				if (method.getModifiers() == Modifier.PUBLIC || method.getModifiers() == Modifier.PRIVATE) {
					MethodHandler handler = new MethodHandler(manager, method);
					this.cmd_handler.put(method.getName(), handler);
				}
			}
		}
	}

	protected MethodHandler getHandler(String cmd) {
		return this.cmd_handler.get(cmd);
	}
}
