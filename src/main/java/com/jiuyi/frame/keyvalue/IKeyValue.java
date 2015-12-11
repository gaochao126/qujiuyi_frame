package com.jiuyi.frame.keyvalue;

import java.util.Date;

/**
 * @Author: xutaoyang @Date: 上午10:02:12
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public interface IKeyValue {

	public Integer getValueInt(int userId, String key);

	public Date getValueDate(int userId, String key);

	public String getValue(int userId, String key);

	public void setValue(int userId, String key, Integer value);

	public void setValue(int userId, String key, Date value);

	public void setValue(int userId, String key, String value);

	public void delete(int userId, String key);
}
