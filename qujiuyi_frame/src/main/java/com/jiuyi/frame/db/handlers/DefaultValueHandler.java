package com.jiuyi.frame.db.handlers;

import java.lang.reflect.Field;

import com.jiuyi.frame.db.DbValueHandler;
import com.jiuyi.frame.util.Defaults;

public class DefaultValueHandler implements DbValueHandler {

	@Override
	public boolean supportField(Field field, String columnName, Object columnValue) {
		return true;
	}

	@Override
	public Object resolveValue(Field field, String columnName, Object columnValue) {
		return columnValue != null ? columnValue : Defaults.defaultValue(field.getType());
	}

}
