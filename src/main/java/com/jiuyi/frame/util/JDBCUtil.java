package com.jiuyi.frame.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xutaoyang @Date: 上午10:37:30
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class JDBCUtil {

	public static List<Object[]> toBatchArgs(Object[] arr, Object param) {
		List<Object[]> res = new ArrayList<Object[]>(arr.length);
		for (Object obj : arr) {
			res.add(new Object[] { obj, param });
		}
		return res;
	}

	public static List<Object[]> toBatchArgs(Object[] arr) {
		List<Object[]> res = new ArrayList<Object[]>(arr.length);
		for (Object obj : arr) {
			res.add(new Object[] { obj });
		}
		return res;
	}
}
