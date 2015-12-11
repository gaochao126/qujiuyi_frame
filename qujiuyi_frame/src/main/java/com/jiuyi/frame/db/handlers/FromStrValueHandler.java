package com.jiuyi.frame.db.handlers;

import java.lang.reflect.Field;

import com.jiuyi.frame.annotations.FromStr;
import com.jiuyi.frame.db.DbValueHandler;
import com.jiuyi.frame.util.JsonUtil;

public class FromStrValueHandler implements DbValueHandler {

	@Override
	public boolean supportField(Field field, String columnName, Object columnValue) {
		return columnValue != null && field.isAnnotationPresent(FromStr.class);
	}

	@Override
	public Object resolveValue(Field field, String columnName, Object columnValue) {
		return JsonUtil.fromJson((String) columnValue, field.getType());
	}

}
