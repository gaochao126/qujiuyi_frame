package com.jiuyi.frame.httpclient;

import org.apache.http.NameValuePair;

import com.jiuyi.frame.util.JsonUtil;

/**
 * @Author: xutaoyang @Date: 下午3:08:29
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class JsonNameValuePair implements NameValuePair {

	String name;
	Object value;

	public JsonNameValuePair(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getValue() {
		Class<?> clazz = this.value.getClass();
		boolean needNotJson = false;
		needNotJson = clazz.equals(String.class) || Number.class.isAssignableFrom(clazz) || clazz.equals(int.class) || clazz.equals(double.class) || clazz.equals(float.class);
		if (needNotJson) {
			return String.valueOf(value);
		}
		String json =  JsonUtil.toJson(value);
		System.out.println(json);
		return json;
	}
}
