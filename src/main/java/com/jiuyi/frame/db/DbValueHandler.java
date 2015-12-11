package com.jiuyi.frame.db;

import java.lang.reflect.Field;

public interface DbValueHandler {

	/** 如果handler支持该类型，返回true */
	public boolean supportField(Field field, String columnName, Object columnValue);

	/** 把从数据库的值转为java对象的字段值 */
	public Object resolveValue(Field field, String columnName, Object columnValue);

}
