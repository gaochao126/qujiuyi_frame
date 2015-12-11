package com.jiuyi.frame.conf;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xutaoyang @Date: 下午8:08:53
 *
 * @Description 写个静态类，调用的时候就不用写注入了。。。
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class DBConfigStatic {

	private static Map<String, String> key_value = new HashMap<String, String>();

	public static String getConfig(String key) {
		return key_value.get(key);
	}

	public static Integer getConfigInt(String key) {
		return Integer.parseInt(key_value.get(key));
	}

	public static void update(Map<String, String> temp) {
		key_value = temp;
	}
}
