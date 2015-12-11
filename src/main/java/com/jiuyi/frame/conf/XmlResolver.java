package com.jiuyi.frame.conf;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.jiuyi.frame.annotations.XmlAttr;
import com.jiuyi.frame.util.StringUtil;

/**
 * @author xutaoyang @Date 2015年3月23日
 * 
 * @Description 简易的xml与java object对应的解析类
 *
 * @Copyright 2015 重庆玖壹健康管理有限公司
 */
public class XmlResolver {

	/**
	 * 把xml的一个元素解析成对象
	 * 
	 * @param element
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static <T> T resolveToObj(Element element, Class<T> clazz) throws InstantiationException, IllegalAccessException {
		T object = clazz.newInstance();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			XmlAttr xmlAttr = field.getAnnotation(XmlAttr.class);
			if (xmlAttr == null) {
				continue;
			}
			String attrName = xmlAttr.value();
			field.setAccessible(true);
			String fieldValueStr = element.getAttribute(attrName);
			Class<?> fieldType = field.getType();
			field.set(object, str2Object(fieldValueStr, fieldType));// 理论上这里应该调用set方法的，这里暴力一下，也能达到设置值的效果
		}
		return object;
	}

	/**
	 * 把xml元素列表解析成java对象列表
	 * 
	 * @param elements
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static <T> List<T> resolveToObjList(List<Element> elements, Class<T> clazz) throws InstantiationException, IllegalAccessException {
		List<T> result = new ArrayList<T>(elements.size());
		for (Element element : elements) {
			result.add(resolveToObj(element, clazz));
		}
		return result;
	}

	private static Object str2Object(String value, Class<?> targetType) {
		if (Number.class.isAssignableFrom(targetType) && StringUtil.isNullOrEmpty(value)) {
			return 0;
		}
		if (targetType.equals(Integer.class) || targetType.equals(int.class)) {
			return Integer.parseInt(value);
		} else if (targetType.equals(Double.class) || targetType.equals(double.class)) {
			return Double.parseDouble(value);
		} else if (targetType.equals(Long.class) || targetType.equals(long.class)) {
			return Double.parseDouble(value);
		}
		return value;
	}
}
