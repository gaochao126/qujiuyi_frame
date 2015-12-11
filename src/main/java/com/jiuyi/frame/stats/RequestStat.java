package com.jiuyi.frame.stats;

import java.util.concurrent.atomic.AtomicLong;

import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;

/**
 * @Author: xutaoyang @Date: 下午4:16:37
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class RequestStat implements ISerializableObj {

	public String url;
	public AtomicLong count = new AtomicLong(0);// 总的次数
	public AtomicLong totalTime = new AtomicLong(0);// 总的时间。单位是毫秒，下同
	public AtomicLong maxTime = new AtomicLong(0);// 最长的一次访问时间

	public RequestStat(String url) {
		this.url = url;
	}

	public void updateInfo(long useTime) {
		this.count.incrementAndGet();
		this.totalTime.addAndGet(useTime);
		this.maxTime.compareAndSet(Math.min(useTime, this.maxTime.longValue()), useTime);
	}

	public float getAvgTime() {
		return (float) this.totalTime.longValue() / this.count.longValue();
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("url", this.url);
		res.put("count", this.count);
		res.put("totalTime", this.totalTime);
		res.put("maxTime", this.maxTime);
		res.put("avgTime", getAvgTime());
		return res;
	}
}
