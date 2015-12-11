package com.jiuyi.frame.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @Author: xutaoyang @Date: 上午10:51:09
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class CollectionUtil {

	/**
	 * 获取分页数据
	 * 
	 * @param <T>
	 * 
	 * @param list
	 *            全部数据
	 * @param page
	 *            从0开始
	 * @param pageSize
	 *            每页的数量,需要大于0
	 * @return
	 */
	public static <T> List<T> getPage(List<T> list, int page, int pageSize) {
		if (page < 0) {
			return new ArrayList<>();
		}
		int startIndex = page * pageSize;
		int stopIndex = Math.min(list.size(), (page + 1) * pageSize);
		if (startIndex >= list.size()) {
			return new ArrayList<>();
		}
		if (list.size() < pageSize) {
			return list;
		}
		return new ArrayList<>(list.subList(startIndex, stopIndex));
	}

	public static boolean isNullOrEmpty(Collection<? extends Object> collection) {
		return collection == null || collection.isEmpty();
	}

	public static <T> List<T> emptyIfNull(List<T> list) {
		return list != null ? list : new ArrayList<T>();
	}

	/** 在l1中不在l2中的元素，差集 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> List<T> diff(List<T> l1, List<T> l2) {
		List<T> list = new ArrayList(Arrays.asList(new Object[l1.size()]));
		Collections.copy(list, l1);
		list.removeAll(l2);
		return list;
	}
}
