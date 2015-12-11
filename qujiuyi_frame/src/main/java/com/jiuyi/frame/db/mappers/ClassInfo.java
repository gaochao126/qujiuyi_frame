package com.jiuyi.frame.db.mappers;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import com.jiuyi.frame.annotations.Init;
import com.jiuyi.frame.helper.Loggers;
import com.jiuyi.frame.util.ObjectUtil;

public class ClassInfo<T> {

	private static Objenesis objenesis = new ObjenesisStd();
	private ObjectInstantiator<T> instantiator;
	private Method initMethod;
	private Class<T> clazz;
	private List<Field> allFields;

	public ClassInfo(Class<T> clazz) {
		this.clazz = clazz;
		instantiator = objenesis.getInstantiatorOf(clazz);
		this.allFields = ObjectUtil.getAllFields(clazz);
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(Init.class)) {
				this.initMethod = method;
				break;
			}
		}
	}

	public T newInstance() {
		T bean = this.instantiator.newInstance();
		if (initMethod != null) {
			try {
				initMethod.invoke(bean, (Object[]) null);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				Loggers.err("<<ClassInfo>> init method invoke err");
			}
		}
		return bean;
	}

	public List<Field> getAllFields() {
		return this.allFields;
	}

	public Class<T> getClazz() {
		return clazz;
	}

}
