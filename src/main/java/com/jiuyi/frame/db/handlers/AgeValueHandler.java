package com.jiuyi.frame.db.handlers;

import java.lang.reflect.Field;
import java.util.Date;

import com.jiuyi.frame.annotations.Age;
import com.jiuyi.frame.db.DbValueHandler;
import com.jiuyi.frame.util.DateUtil;

public class AgeValueHandler implements DbValueHandler {

	@Override
	public boolean supportField(Field field, String columnName, Object columnValue) {
		return (field != null && field.isAnnotationPresent(Age.class)) && (columnValue == null || columnValue instanceof Date);
	}

	@Override
	public Object resolveValue(Field field, String columnName, Object columnValue) {
		if (columnValue == null) {
			return 0;
		}
		return DateUtil.calcAge((Date) columnValue);
	}

}
