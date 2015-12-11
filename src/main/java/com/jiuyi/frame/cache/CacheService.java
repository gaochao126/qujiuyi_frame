package com.jiuyi.frame.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.frame.cache.redis.RedisService;

/**
 * @Author: xutaoyang @Date: 下午2:05:04
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class CacheService {

	private String KEY_FORMAT = "%s_%s_%s";

	@Autowired
	private RedisService redisService;

	/**
	 * 通过角色id,功能key，业务key来让缓存键唯一
	 * 
	 * @param doctorId
	 *            指定一个角色的id
	 * @param key
	 *            功能key
	 * @param target
	 *            功能下具体业务key
	 * @return
	 */
	public String getValue(Integer userId, String key, String target) {
		return redisService.get(genKey(userId, key, target));
	}

	public void setValue(Integer userId, String key, String target, String value) {
		redisService.set(genKey(userId, key, target), value);
	}

	public Integer getValueInt(Integer userId, String key, String target) {
		return redisService.getInteger(genKey(userId, key, target));
	}

	public Integer getValueInt(Integer userId, String key, String target, Integer defaulVal) {
		return redisService.getInteger(genKey(userId, key, target), defaulVal);
	}

	public void setValueInt(Integer userId, String key, String target, Integer value) {
		redisService.setInteger(genKey(userId, key, target), value);
	}

	public void deleteValue(Integer userId, String key, String target) {
		redisService.delete(genKey(userId, key, target));
	}

	public void incValue(Integer userId, String key, String target) {
		redisService.incValue(genKey(userId, key, target));
	}

	public void decValue(Integer userId, String key, String target) {
		redisService.decValue(genKey(userId, key, target));
	}

	private String genKey(Integer userId, String key, String target) {
		return String.format(KEY_FORMAT, userId, key, target);
	}
}
