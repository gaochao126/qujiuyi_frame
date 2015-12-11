package com.jiuyi.frame.base.accesscheck;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @Author: xutaoyang @Date: 下午3:50:49
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class TestAnnotation {

	public static void main(String[] args) {
		Clazz clazz = new Clazz();
		Class<? extends Clazz> cls = clazz.getClass();
		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {
			Annotation[][] annotations = method.getParameterAnnotations();
			for (Annotation[] annotationArr : annotations) {
				for (Annotation anno : annotationArr) {
					System.out.println(anno.annotationType().equals(NotNull.class));
				}
			}
		}
	}

	static class Clazz {

		public void function(@NotNull @NotEmpty String param1, String param2) {

		}

	}

}
