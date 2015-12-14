package com.jiuyi.frame.db.mappers;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import com.jiuyi.frame.annotations.Column;
import com.jiuyi.frame.annotations.Init;
import com.jiuyi.frame.helper.Loggers;
import com.jiuyi.frame.util.ObjectUtil;

public class ClassInfo<T> {

	private static Objenesis objenesis = new ObjenesisStd();
	private ObjectInstantiator<T> instantiator;
	private Method initMethod;
	private Class<T> clazz;
	private List<FieldInfo> fieldsInfo;

	public ClassInfo(Class<T> clazz) {
		this.clazz = clazz;
		instantiator = objenesis.getInstantiatorOf(clazz);
		List<Field> allFields = ObjectUtil.getAllFields(clazz);
		Method[] methods = clazz.getMethods();
		for (Field field : allFields) {
			this.fieldsInfo.add(new FieldInfo(field));
		}
		for (Method method : methods) {
			if (method.isAnnotationPresent(Init.class)) {
				this.initMethod = method;
				break;
			}
		}
	}

	public T newInstance() {
		T bean = this.instantiator.newInstance();
		if (initMethod != null) {
			try {
				initMethod.invoke(bean, (Object[]) null);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				Loggers.err("<<ClassInfo>> init method invoke err");
			}
		}
		return bean;
	}

	public List<FieldInfo> getFieldsInfo() {
		return this.fieldsInfo;
	}

	public FieldInfo getFieldInfoByColName(String columnName) {
		for (FieldInfo fieldInfo : fieldsInfo) {
			if (fieldInfo.matchColumn(columnName)) {
				return fieldInfo;
			}
		}
		return null;
	}

	public Class<T> getClazz() {
		return clazz;
	}

	/**
	 * 字段信息
	 * 
	 * @author xutaoyang
	 *
	 */
	public static class FieldInfo {
		private Field field;
		private String columnName;

		public FieldInfo(Field field) {
			this.field = field;
			Column column = field.getAnnotation(Column.class);
			this.columnName = column != null ? column.value() : field.getName();
		}

		public boolean matchColumn(String column) {
			return this.columnName.equals(column);
		}

		public Field getField() {
			return field;
		}

		public void setField(Field field) {
			this.field = field;
		}

		public String getColumnName() {
			return columnName;
		}

		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}

	}
}
