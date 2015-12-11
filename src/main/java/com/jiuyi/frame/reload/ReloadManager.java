package com.jiuyi.frame.reload;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.helper.Loggers;

/**
 * @author xutaoyang @Date 2015年3月23日
 * 
 * @Description 配置重载服务类
 * 
 * @Copyright 2015 重庆玖壹健康管理有限公司
 */
@Service
public class ReloadManager {

	private ConcurrentHashMap<String, IReloader> key_reloader = new ConcurrentHashMap<String, IReloader>();

	public void registerReloader(String key, IReloader reloader) {
		IReloader existReloader = key_reloader.get(key);
		if (existReloader != null && !existReloader.getClass().equals(reloader.getClass())) {// 不同reloader用了同样的key
			Loggers.errf(new Exception("reloader register duplicate reloader name:" + key), "reloader register duplicate reloader name:%s", key);
			return;
		}
		key_reloader.put(key, reloader);
	}

	public ServerResult reload(String key) {
		if (key_reloader.containsKey(key)) {
			key_reloader.get(key).reload();
			return new ServerResult(ResultConst.SUCCESS);
		}
		return new ServerResult(ResultConst.KEY_NOT_EXIST);
	}

	public Set<String> reloaders() {
		return this.key_reloader.keySet();
	}
}
