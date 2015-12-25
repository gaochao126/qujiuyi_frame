/**
 * 
 */
package com.jiuyi.frame.idgen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.frame.helper.Loggers;
import com.jiuyi.frame.jobs.JobContext;
import com.jiuyi.frame.jobs.JobService;
import com.jiuyi.frame.jobs.JobType;
import com.jiuyi.frame.util.CollectionUtil;
import com.sun.jersey.client.impl.CopyOnWriteHashMap;

/**
 * id自增长生成器，数据库里有一张表存储id->value，每次启动服务器的时候从表中读取数据，生成id的时候+step并回写数据库
 * 
 * @author xutaoyang
 *
 */
@Service
public class IdGeneratorService {

	private @Autowired IdGeneratorDao dao;

	private @Autowired JobService jobService;

	private CopyOnWriteHashMap<String, IdGenerator> name_generator = new CopyOnWriteHashMap<>();

	@PostConstruct
	public void init() {
		jobService.submitJob(new JobContext(JobType.SCHEDULED, new WriteBack(), 5, 5, TimeUnit.SECONDS));
	}

	@PreDestroy
	public void desctroy() {
		writeBack();
	}

	/**
	 * 根据name获取generator
	 * 
	 * @param name
	 * @return
	 */
	public IdGenerator getGenerator(String name) {
		if (name_generator.containsKey(name)) {
			return name_generator.get(name);
		}
		IdGenerator generator = dao.loadGenderatorByName(name);
		if (generator == null) {
			Loggers.err("id generator err", new Exception("id generator:" + name + " not found"));
			return null;
		}
		// 为防止意外关闭导致的回写失败，这里从数据库读出来后跳过50个数字，减小重复的概率
		generator.add(50);
		name_generator.put(name, generator);
		return generator;
	}

	public IdGenerator newGenerator(String name, int start, int step) throws Exception {
		IdGenerator old = getGenerator(name);
		if (old != null) {
			throw new Exception(name + " id generator exists!!!");
		}
		IdGenerator generator = new IdGenerator(name, start, step);
		dao.insertGenerator(generator);
		name_generator.put(name, generator);
		return generator;
	}

	/**
	 * 生成long型id
	 * 
	 * @param name
	 * @return
	 */
	public long genIdLong(String name) {
		return getGenerator(name).genIdLong();
	}

	/**
	 * 生成int型id
	 * 
	 * @param name
	 * @return
	 */
	public int genId(String name) {
		return getGenerator(name).genId();
	}

	private void writeBack() {
		List<IdGenerator> changedList = new ArrayList<>();
		for (Iterator<IdGenerator> iter = name_generator.values().iterator(); iter.hasNext();) {
			IdGenerator generator = iter.next();
			if (generator.changed()) {
				changedList.add(generator);
			}
			generator.changed(false);
		}
		if (!CollectionUtil.isNullOrEmpty(changedList)) {
			dao.batchUpdate(changedList);
		}
	}

	/**
	 * 每五秒回写一次
	 * 
	 * @author xutaoyang
	 *
	 */
	private class WriteBack implements Runnable {

		@Override
		public void run() {
			try {
				writeBack();
			} catch (Exception e) {
				Loggers.err("id generatro write back err", e);
			}
		}

	}
}
