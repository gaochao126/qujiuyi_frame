package com.jiuyi.frame.keyvalue.impls;

import java.util.Date;

import com.jiuyi.frame.base.DbBase;
import com.jiuyi.frame.keyvalue.IKeyValue;

/**
 * @Author: xutaoyang @Date: 上午10:05:19
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class KeyValueImpl extends DbBase implements IKeyValue {

	private String SELECT = "SELECT `value` FROM `#tableName#` WHERE `target`=? AND `userId`=?";
	private String UPDATE = "REPLACE `#tableName#` VALUE(?,?,?)";
	private String DELETE = "DELETE FROM `#tableName#` WHERE `target`=? AND `userId`=?";

	public KeyValueImpl(String tableName) {
		this.SELECT = SELECT.replaceAll("#tableName#", tableName);
		this.UPDATE = UPDATE.replaceAll("#tableName#", tableName);
		this.DELETE = DELETE.replaceAll("#tableName#", tableName);
	}

	@Override
	public Integer getValueInt(int userId, String key) {
		return queryForInteger(SELECT, new Object[] { key, userId });
	}

	@Override
	public Date getValueDate(int userId, String key) {
		String dateLong = getValue(userId, key);
		return dateLong == null ? null : new Date(Long.parseLong(dateLong));
	}

	@Override
	public String getValue(int userId, String key) {
		return queryForString(SELECT, new Object[] { key, userId });
	}

	@Override
	public void setValue(int userId, String key, Integer value) {
		jdbc.update(UPDATE, new Object[] { userId, key, value });
	}

	@Override
	public void setValue(int userId, String key, Date value) {
		jdbc.update(UPDATE, new Object[] { userId, key, value.getTime() });
	}

	@Override
	public void setValue(int userId, String key, String value) {
		jdbc.update(UPDATE, new Object[] { userId, key, value });
	}

	@Override
	public void delete(int userId, String key) {
		jdbc.update(DELETE, new Object[] { userId, key });
	}

}
