package com.jiuyi.frame.conf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.frame.base.DbBase;
import com.jiuyi.frame.reload.IReloader;
import com.jiuyi.frame.reload.ReloadManager;

/**
 * @Author: xutaoyang @Date: 上午10:32:46
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class DBConfig extends DbBase implements IReloader {

	private static final String RELOADER_NAME = "dbConfig";
	private static final String SELECT_CONFIG = "SELECT * FROM `t_sys_config`";
	private static final String SELECT_CONFIG_BY_KEY = "SELECT `configValue` FROM `t_sys_config` WHERE `configName`=?";
	private static Map<String, String> key_value = new HashMap<String, String>();

	@Autowired
	ReloadManager reloadManager;

	@PostConstruct
	public void init() {
		reloadManager.registerReloader(RELOADER_NAME, this);
		reload();
	}

	public String getConfig(String key) {
		return key_value.get(key);
	}

	public Integer getConfigInt(String key) {
		return Integer.parseInt(key_value.get(key));
	}

	public String getConfigFromDB(String key) {
		return jdbc.queryForObject(SELECT_CONFIG_BY_KEY, new Object[] { key }, String.class);
	}

	@Override
	public void reload() {
		Map<String, String> temp = new HashMap<String, String>();
		List<Map<String, Object>> allConfig = jdbc.queryForList(SELECT_CONFIG);
		if (allConfig != null && !allConfig.isEmpty()) {
			for (Map<String, Object> row : allConfig) {
				String key = row.get("configName").toString();
				String value = row.get("configValue").toString();
				temp.put(key, value);
			}
		}
		key_value = temp;
		DBConfigStatic.update(temp);
	}
}
