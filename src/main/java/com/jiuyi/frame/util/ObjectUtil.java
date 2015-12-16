package com.jiuyi.frame.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.jiuyi.frame.annotations.FromStr;
import com.jiuyi.frame.annotations.NotInsert;
import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.helper.Loggers;

public class ObjectUtil {

	private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private static Validator validator = factory.getValidator();

	/**
	 * validate object with out put is hibernate out
	 * 
	 * @param obj
	 * @return
	 */
	public static Set<ConstraintViolation<Object>> validate(Object obj) {
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj);
		return constraintViolations;
	}

	/**
	 * validate object with my validate result
	 * 
	 * @param obj
	 * @return
	 */
	public static ValidateResult validateRes(Object obj) {
		return new ValidateResult(validate(obj));
	}

	/**
	 * validate object with my validate result
	 * 
	 * @param obj
	 * @return
	 */
	public static ServerResult validateResult(Object obj) {
		return new ValidateResult(validate(obj)).toServerResult();
	}

	/**
	 * 对list中的对象循环验证，如果有一个验证不通过，则返回失败
	 * 
	 * @param objs
	 * @return
	 */
	public static ServerResult validateList(List<? extends Object> objs) {
		for (Object obj : objs) {
			ValidateResult validateRes = validateRes(obj);
			if (!validateRes.isSuccess()) {
				return validateRes.toServerResult();
			}
		}
		return new ServerResult();
	}

	/** 把一个对象转换为map，key为字段，value为字段的值 */
	public static Map<String, Object> introspect(Object obj) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (obj == null) {
			return result;
		}
		try {
			Class<?> clazz = obj.getClass();
			BeanInfo info = Introspector.getBeanInfo(clazz);
			for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
				String propertyName = pd.getName();
				if (propertyName.equals("class")) {
					continue;
				}
				Method reader = pd.getReadMethod();
				if (reader != null) {
					Object value = reader.invoke(obj);
					if (clazz.getDeclaredField(propertyName).isAnnotationPresent(FromStr.class)) {
						value = JsonUtil.toJson(value);
					}
					result.put(propertyName, value);
				}
			}
		} catch (Exception e) {
			Loggers.err("introspect obj to map err", e);
		}
		return result;
	}

	/** 把一个对象转换为map，key为字段，value为字段的值，被标记为 not insert的字段不放到map中 */
	public static Map<String, Object> introspectExclude(Object obj) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (obj == null) {
			return result;
		}
		try {
			Class<?> clazz = obj.getClass();
			BeanInfo info = Introspector.getBeanInfo(clazz);
			for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
				String propertyName = pd.getName();
				if (propertyName.equals("class") || clazz.getDeclaredField(propertyName).isAnnotationPresent(NotInsert.class)) {
					continue;
				}
				Field field = clazz.getDeclaredField(propertyName);
				if (field.isAnnotationPresent(NotInsert.class)) {
					continue;
				}
				Method reader = pd.getReadMethod();
				if (reader != null) {
					Object value = reader.invoke(obj);
					if (field.isAnnotationPresent(FromStr.class)) {
						value = JsonUtil.toJson(value);
					}
					result.put(propertyName, value);
				}
			}
		} catch (Exception e) {
			Loggers.err("introspect obj to map err", e);
		}
		return result;
	}

	/** 获取所有定义的字段，不包括继承的 */
	public static List<String> getFields(Class<?> obj) {
		if (obj == null) {
			return new ArrayList<>();
		}
		List<String> result = new ArrayList<>();
		try {
			BeanInfo info = Introspector.getBeanInfo(obj);
			for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
				String name = pd.getName();
				if (!name.equals("class")) {// 继承自Object的class属性也会被读出来，手动把它排除掉
					result.add(name);
				}
			}
		} catch (Exception e) {
			Loggers.err("get obj fields err", e);
		}
		return result;
	}

	/** 获取所有字段，包括继承的 */
	public static List<Field> getAllFields(Class<?> clazz) {
		List<Field> res = new ArrayList<>();
		res.addAll(Arrays.asList(clazz.getDeclaredFields()));
		Class<?> superClass = clazz.getSuperclass();
		while (superClass != null) {
			res.addAll(Arrays.asList(superClass.getDeclaredFields()));
			superClass = superClass.getSuperclass();
		}
		return res;
	}

	/** `col1`,`col2`... */
	public static String getFieldAsSelectColumns(Class<?> obj) {
		return getFieldAsSelectColumns(obj, (String[]) null);
	}

	/** :col1,:col2... */
	public static String getFieldAsInsertColumns(Class<?> obj) {
		return getFieldAsInsertColumns(obj, (String[]) null);
	}

	/** `col1`=:col1,`col2`=:col2... */
	public static String getFieldAsUpdateColumns(Class<?> obj) {
		return getFieldAsUpdateColumns(obj, (String[]) null);
	}

	/**
	 * 通过object的属性字段组装select sql的字段字符串。 比如一个对象user有字段id，name，那么对返回`id`,`name`
	 * 如果except为name，那么只会返回`id`
	 * 
	 * 对于字段较多的对象比较有用，不用去写大串的sql，还有对于需要选取*-columns的时候，用except也方便很多
	 * 
	 * @param obj
	 *            要读取字段的对象
	 * @param except
	 *            排除哪些字段
	 * @return
	 */
	public static String getFieldAsSelectColumns(Class<?> obj, String... except) {
		List<String> fields = getFields(obj);
		if (except != null) {
			fields.removeAll(Arrays.asList(except));
		}
		StringBuilder sb = new StringBuilder(" ");
		for (String field : fields) {
			sb.append("`").append(field).append("`").append(",");
		}
		return sb.length() > 1 ? sb.substring(0, sb.length() - 1) + " " : "*";
	}

	/** :col1,:col2... */
	public static String getFieldAsInsertColumns(Class<?> obj, String... except) {
		List<String> fields = getFields(obj);
		if (except != null) {
			fields.removeAll(Arrays.asList(except));
		}
		StringBuilder sb = new StringBuilder(" ");
		for (String field : fields) {
			sb.append(":").append(field).append(",");
		}
		return sb.length() > 1 ? sb.substring(0, sb.length() - 1) + " " : "*";
	}

	/** `col1`=:col1,`col2`=:col2... */
	public static String getFieldAsUpdateColumns(Class<?> obj, String... except) {
		List<String> fields = getFields(obj);
		if (except != null) {
			fields.removeAll(Arrays.asList(except));
		}
		StringBuilder sb = new StringBuilder(" ");
		for (String field : fields) {
			sb.append("`").append(field).append("`=:").append(field).append(",");
		}
		return sb.length() > 1 ? sb.substring(0, sb.length() - 1) + " " : "*";
	}

}
