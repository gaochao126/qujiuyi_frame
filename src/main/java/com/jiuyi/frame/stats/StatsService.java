package com.jiuyi.frame.stats;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

/**
 * @Author: xutaoyang @Date: 下午4:16:04
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class StatsService {

	private ConcurrentHashMap<String, RequestStat> url_stat = new ConcurrentHashMap<>();

	public void postHandle(String uri, long useTime) {
		RequestStat requestStat = url_stat.get(uri);
		if (requestStat == null) {
			requestStat = new RequestStat(uri);
			this.url_stat.putIfAbsent(uri, requestStat);
		}
		requestStat.updateInfo(useTime);
	}

	public Collection<RequestStat> getData() {
		return this.url_stat.values();
	}
}
