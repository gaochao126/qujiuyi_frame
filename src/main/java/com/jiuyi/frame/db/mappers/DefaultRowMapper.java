package com.jiuyi.frame.db.mappers;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.jdbc.core.RowMapper;

import com.jiuyi.frame.db.DbValueHandlerComposite;
import com.jiuyi.frame.helper.Loggers;

public class DefaultRowMapper<T> implements RowMapper<T> {

	private ClassInfo<T> clazzInfo;

	private DbValueHandlerComposite valHandler;

	public DefaultRowMapper(Class<T> clazz, DbValueHandlerComposite valHandler) {
		this.clazzInfo = new ClassInfo<>(clazz);
		this.valHandler = valHandler;
	}

	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		Field[] fields = clazzInfo.getClazz().getDeclaredFields();
		T bean = clazzInfo.newInstance();
		try {
			for (int _iterator = 0; _iterator < rsmd.getColumnCount(); _iterator++) {
				// String columnName = rsmd.getColumnName(_iterator + 1);
				/* label：如果有别名则为alias，否则为col name */
				String columnName = rsmd.getColumnLabel(_iterator + 1);
				Object columnValue = rs.getObject(_iterator + 1);
				for (Field field : fields) {
					if (field.getName().equals(columnName)) {
						Object value = valHandler.resolveValue(field, columnName, columnValue);
						if (value != null) {
							BeanUtils.setProperty(bean, field.getName(), value);
						}
						break;
					}
				}
			}
		} catch (Exception e) {
			Loggers.err("db : RersultSet to object err", e);
		}
		return bean;
	}

}
