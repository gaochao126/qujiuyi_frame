package com.jiuyi.frame.httpclient;

import java.nio.charset.Charset;
import java.util.Map;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import com.jiuyi.frame.util.JsonUtil;

/**
 * @Author: xutaoyang @Date: 上午10:06:33
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public abstract class AbsHttpClient {

	protected String url;
	protected Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	public AbsHttpClient(String url) {
		this.url = url;
	}

	public abstract void post(String entity);

	public abstract void post(IRequestEntity entity);

	public abstract void shutdown();

	public HttpPost genHttpPost(IRequestEntity entity) {
		HttpPost httpPost = new HttpPost(url);
		StringEntity s = new StringEntity(JsonUtil.toJson(entity.genEntity()), DEFAULT_CHARSET);
		s.setContentType("application/json");
		httpPost.setEntity(s);
		return httpPost;
	}

	public interface IRequestEntity {

		public Map<String, ? extends Object> genEntity();

	}
}
