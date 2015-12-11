package com.jiuyi.frame.httpclient;

import java.util.HashMap;
import java.util.Map;

import com.jiuyi.frame.httpclient.AbsHttpClient.IRequestEntity;

public class DefaultRequestEntity implements IRequestEntity {

	private Map<String, Object> params = new HashMap<>();

	public DefaultRequestEntity() {
	}

	public DefaultRequestEntity(String cmd) {
		this.params.put("cmd", cmd);
	}

	public void putParam(String key, Object value) {
		this.params.put(key, value);
	}

	@Override
	public Map<String, ? extends Object> genEntity() {
		return this.params;
	}

}
