package com.jiuyi.frame.cache.redis;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.frame.conf.DBConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author xutaoyang @Date 2015年3月23日
 * 
 * @Description redis缓存服务类
 *
 * @Copyright 2015 重庆玖壹健康管理有限公司
 */
@Service("RedisService")
public class RedisService {

	@Autowired
	DBConfig dbConfig;

	private JedisPool pool;

	// public RedisService() {
	// String redisHost = "192.168.9.5";
	// Integer redisPort = 51110;
	// JedisPoolConfig config = new JedisPoolConfig();
	// this.pool = new JedisPool(config, redisHost, redisPort);
	// }

	@PostConstruct
	public void init() {
		String redisHost = dbConfig.getConfig("doctor.redis.host");
		Integer redisPort = dbConfig.getConfigInt("doctor.redis.port");
		JedisPoolConfig config = new JedisPoolConfig();
		this.pool = new JedisPool(config, redisHost, redisPort);
	}

	public void set(String key, String value) {
		try (Jedis jedis = pool.getResource()) {
			jedis.set(key, value);
		}
	}

	public void setInteger(String key, Integer value) {
		try (Jedis jedis = pool.getResource()) {
			jedis.set(key, String.valueOf(value));
		}
	}

	public Long incValue(String key) {
		try (Jedis jedis = pool.getResource()) {
			return jedis.incr(key);
		}
	}

	public Long decValue(String key) {
		try (Jedis jedis = pool.getResource()) {
			return jedis.decr(key);
		}
	}

	public Long incBy(String key, long inc) {
		try (Jedis jedis = pool.getResource()) {
			return jedis.incrBy(key, inc);
		}
	}

	public Long decBy(String key, long dec) {
		try (Jedis jedis = pool.getResource()) {
			return jedis.decrBy(key, dec);
		}
	}

	public String get(String key) {
		try (Jedis jedis = pool.getResource()) {
			return jedis.get(key);
		}
	}

	public Integer getInteger(String key) {
		String value = get(key);
		return value == null ? 0 : Integer.valueOf(value);
	}

	public Integer getInteger(String key, Integer defaultVal) {
		String value = get(key);
		return value == null ? defaultVal : Integer.valueOf(value);
	}

	public void append(String key, String append) {
		try (Jedis jedis = pool.getResource()) {
			jedis.append(key, append);
		}
	}

	public void delete(String key) {
		try (Jedis jedis = pool.getResource()) {
			jedis.del(key);
		}
	}

	public void exist(String key) {
		try (Jedis jedis = pool.getResource()) {
			jedis.exists(key);
		}
	}

	public Set<String> keys(String pattern) {
		try (Jedis jedis = pool.getResource()) {
			return jedis.keys(pattern);
		}
	}

	public Object eval(String script) {
		try (Jedis jedis = pool.getResource()) {
			return jedis.eval(script);
		}
	}

	public JedisPool getPool() {
		return this.pool;
	}

	public Jedis getResource() {
		return this.pool.getResource();
	}

	public void close(Jedis jedis) {
		jedis.close();
	}

	@PreDestroy
	public void destroyPool() {
		this.pool.destroy();
	}

	public static void main(String[] args) {
		RedisService redisService = new RedisService();

		/** get pattern */
		// for (int index = 0; index < 10; index++) {
		// redisService.set("daily" + index, String.valueOf(index));
		// }
		// Set<String> dailyKeys = redisService.keys("daily*");
		// for (String dailyKey : dailyKeys) {
		// System.out.println(dailyKey + "--->" + redisService.get(dailyKey));
		// }
		// redisService.set("weekly", "1");

		/** delete pattern */
		// Object res =
		//
		// redisService.eval("return redis.call('del', unpack(redis.call('keys',
		// 'daily*')))");
		// System.out.println(res);

		/** append */
		// redisService.set("foo", "bar");
		// redisService.append("foo", "--from usa");
		// System.out.println(redisService.get("foo"));

		redisService.setInteger("foo", 1);
		System.out.println(redisService.get("foo"));
		System.out.println(redisService.get("bar"));

		redisService.destroyPool();
	}
}
