package com.jiuyi.frame.httpclient;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.annotation.PreDestroy;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import com.jiuyi.frame.helper.Loggers;

/**
 * @Author: xutaoyang @Date: 下午5:32:55
 *
 * @Description 异步httpclient
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class AsynHttpClientService extends AbsHttpClient {

	// BlockingQueue<String> strQueue = new ArrayBlockingQueue<>(100, true);
	BlockingQueue<IRequestEntity> reqQueue = new ArrayBlockingQueue<>(100, true);
	private HttpHost host = null;
	private CloseableHttpClient httpClient = null;
	private PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();

	private volatile boolean running = true;
	private Thread sendReqThread;

	private static final int MAX_CONN_NUM = 10;
	private static final int MAX_CONN_DEFAULT_ROUTE = 10;
	private static final int MAX_CONN_ROUTE = 10;

	public AsynHttpClientService(String url) {
		super(url);
		host = new HttpHost(url);// 针对的主机
		connManager.setMaxTotal(MAX_CONN_NUM);// 设置最大连接数
		connManager.setDefaultMaxPerRoute(MAX_CONN_DEFAULT_ROUTE);// 设置每个路由默认连接数
		connManager.setMaxPerRoute(new HttpRoute(host), MAX_CONN_ROUTE);
		httpClient = HttpClients.custom().setConnectionManager(connManager).build();
		sendReqThread = new Thread(new PostRunnable(), "AsynHttpClient");
		sendReqThread.setDaemon(true);
		sendReqThread.start();
	}

	@PreDestroy
	public void destroy() {
		if (sendReqThread != null) {
			sendReqThread.interrupt();
		}
	}

	@Override
	public void post(String params) {
		throw new UnsupportedOperationException("not support post string now");
	}

	@Override
	public void post(IRequestEntity entity) {
		Loggers.debug(entity.toString());
		try {
			this.reqQueue.put(entity);
		} catch (InterruptedException e) {
			Loggers.err("<<AsynHttpClientService>> put msg err", e);
		}
	}

	@Override
	public void shutdown() {
		this.running = false;
		try {
			this.httpClient.close();
		} catch (IOException e) {
			Loggers.err("asyn http client shutdown http client err", e);
		}
		this.connManager.shutdown();
	}

	public boolean isRunning() {
		return this.running;
	}

	private class PostRunnable implements Runnable {

		@Override
		public void run() {
			while (running) {
				IRequestEntity entity = null;
				try {
					entity = reqQueue.take();// 阻塞的
				} catch (InterruptedException e) {
					Loggers.err("<<AsynHttpClientService>> take msg err", e);
				}
				if (entity == null) {
					return;
				}
				HttpPost post = genHttpPost(entity);
				try {
					String postEntity = EntityUtils.toString(post.getEntity());
					Loggers.info(post + ", postEntity:" + postEntity);
				} catch (ParseException | IOException e1) {
					e1.printStackTrace();
				}
				try {
					HttpResponse resp = httpClient.execute(post);
					String res = EntityUtils.toString(resp.getEntity(), DEFAULT_CHARSET);
					Loggers.info(post + ", result:" + res);
				} catch (IOException e) {
					Loggers.err("AsynHttpClientService.PostRunnable err", e);
				}
			}
		}
	}
}
