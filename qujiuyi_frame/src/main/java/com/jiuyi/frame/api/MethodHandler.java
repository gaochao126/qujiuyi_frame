package com.jiuyi.frame.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import com.jiuyi.frame.helper.Loggers;

public class MethodHandler {

	public final Object manager;
	public final Method method;
	public final Type paramType;
	public final boolean needUser;

	public MethodHandler(Object manager, Method method) {
		this.manager = manager;
		this.method = method;
		this.needUser = true;
		this.paramType = method.getParameterTypes().length > 0 ? method.getParameterTypes()[0] : null;
	}

	public Object invoke(Object... args) {
		try {
			Object res = this.method.invoke(manager, args);
			return res;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Loggers.err("invoke manager function err", e);
			return new Object();
		}
	}

}
