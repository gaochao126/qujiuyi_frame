package com.jiuyi.frame.httpclient;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.jiuyi.frame.helper.Loggers;

/**
 * @Author: xutaoyang @Date: 上午11:14:51
 *
 * @Description 同步httpclient
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class SimpleHttpClientService extends AbsHttpClient {

	private CloseableHttpClient httpclient = HttpClients.createDefault();
	private RequestConfig requestConfig;

	public SimpleHttpClientService(String url) {
		super(url);
		this.url = url;
		this.requestConfig = RequestConfig.custom().setSocketTimeout(20000).setConnectTimeout(20000).build();// 设置请求和传输超时时间
	}

	public void post(String params) {
		HttpPost httpPost = new HttpPost(url);
		StringEntity strEntity = new StringEntity(params, DEFAULT_CHARSET);
		strEntity.setContentType("application/json");
		httpPost.setEntity(strEntity);
		CloseableHttpResponse response = null;
		String result = null;
		try {
			response = httpclient.execute(httpPost);
			result = EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					Loggers.err("<<SimpleHttpClientService>> close response err", e);
				}
			}
		}
		Loggers.debug(httpPost + ", result:" + result);
	}

	public void post(IRequestEntity entity) {
		HttpPost httpPost = genHttpPost(entity);
		CloseableHttpResponse response = null;
		String result = null;
		try {
			response = httpclient.execute(httpPost);
			result = EntityUtils.toString(response.getEntity());
		} catch (ParseException | IOException e) {
			Loggers.err("<<SimpleHttpClientService>> post err", e);
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					Loggers.err("<<SimpleHttpClientService>> close response err", e);
				}
			}
		}
		Loggers.debug(httpPost + ", result:" + result);
	}

	public String postWithRes(IRequestEntity entity) {
		HttpPost httpPost = genHttpPost(entity);
		httpPost.setConfig(requestConfig);
		CloseableHttpResponse response = null;
		String result = null;
		try {
			response = httpclient.execute(httpPost);
			result = EntityUtils.toString(response.getEntity());
		} catch (ParseException | IOException e) {
			Loggers.err("<<SimpleHttpClientService>> post err", e);
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					Loggers.err("<<SimpleHttpClientService>> close response err", e);
				}
			}
		}
		return result;
	}

	@Override
	public void shutdown() {
		try {
			this.httpclient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
