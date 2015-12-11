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

import com.jiuyi.frame.annotations.FromStr;
import com.jiuyi.frame.annotations.NotInsert;
import com.jiuyi.frame.helper.Loggers;

public class ObjectUtil {

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

	/** 获取所有字段 */
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
