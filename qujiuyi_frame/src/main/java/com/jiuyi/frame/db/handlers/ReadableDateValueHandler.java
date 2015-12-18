package com.jiuyi.frame.db.handlers;

import java.lang.reflect.Field;
import java.util.Date;

import com.jiuyi.frame.annotations.ReadableDate;
import com.jiuyi.frame.db.DbValueHandler;
import com.jiuyi.frame.util.DateUtil;

public class ReadableDateValueHandler implements DbValueHandler {

	@Override
	public boolean supportField(Field field, String columnName, Object columnValue) {
		return (field != null && field.isAnnotationPresent(ReadableDate.class)) && (columnValue == null || columnValue instanceof Date);
	}

	@Override
	public Object resolveValue(Field field, String columnName, Object columnValue) {
		if (columnValue == null) {
			return null;
		}
		String pattern = field.getAnnotation(ReadableDate.class).value();
		return DateUtil.date2Str((Date) columnValue, pattern);
	}

}
