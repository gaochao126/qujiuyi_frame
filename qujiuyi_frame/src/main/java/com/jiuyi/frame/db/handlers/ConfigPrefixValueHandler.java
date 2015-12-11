package com.jiuyi.frame.db.handlers;

import java.lang.reflect.Field;

import com.jiuyi.frame.annotations.ConfigPrefix;
import com.jiuyi.frame.conf.DBConfigStatic;
import com.jiuyi.frame.db.DbValueHandler;

public class ConfigPrefixValueHandler implements DbValueHandler {

	@Override
	public boolean supportField(Field field, String columnName, Object columnValue) {
		return columnValue != null && field.getType().equals(String.class) && field.isAnnotationPresent(ConfigPrefix.class);
	}

	@Override
	public Object resolveValue(Field field, String columnName, Object columnValue) {
		String prefix = DBConfigStatic.getConfig(field.getAnnotation(ConfigPrefix.class).value());
		return prefix + (String) columnValue;
	}

}
