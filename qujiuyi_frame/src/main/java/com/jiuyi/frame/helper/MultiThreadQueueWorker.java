package com.jiuyi.frame.helper;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

/**
 * 多线程队列处理器<BR>
 * 特点：按数据接收顺序处理，但对于同一K值的V数据处理，既保证顺序，也是线程安全的<BR>
 * <B>要求：一个V对象唯一对应一个键值</B><BR>
 */
public class MultiThreadQueueWorker<K, V> {

	private Logger logger = Logger.getLogger(MultiThreadQueueWorker.class);
	private final LinkedBlockingQueue<K> keyQueue = new LinkedBlockingQueue<K>();
	private final ConcurrentHashMap<K, ValueQueueWrap<V>> key_ValueQueueWrap = new ConcurrentHashMap<K, ValueQueueWrap<V>>();
	protected final String name;
	private final Thread[] workers;
	private volatile boolean running;
	private final int warnSize;
	private final IProcessor<K, V> processor;

	public MultiThreadQueueWorker(String name, int threadNum, int warnSize, IProcessor<K, V> processor) {
		this.name = name;
		workers = new Thread[threadNum];
		running = false;
		this.warnSize = warnSize;
		this.processor = processor;
		for (int i = 0; i < workers.length; i++) {
			String threadName = String.format("%s-Pool%d-Th%d", name, threadNum, i);
			workers[i] = new Thread(new WorkRunnable(), threadName);
			workers[i].setDaemon(true);
		}
	}

	/**
	 * 只能开启一次，关闭后再开会抛异常
	 */
	public synchronized void start() {
		if (!running) {
			running = true;
			for (int i = 0; i < workers.length; i++) {
				workers[i].start();
			}
		}
	}

	public synchronized void shutdown() {
		if (running) {
			running = false;
			for (int i = 0; i < workers.length; i++) {
				workers[i].interrupt();
			}
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void accept(K key, V value) {
		ValueQueueWrap<V> valueQueueWrap = key_ValueQueueWrap.get(key);
		if (valueQueueWrap == null) {
			valueQueueWrap = new ValueQueueWrap<V>();
			ValueQueueWrap<V> tmp = key_ValueQueueWrap.putIfAbsent(key, valueQueueWrap);
			if (tmp != null) {
				valueQueueWrap = tmp;
			}
		}
		synchronized (valueQueueWrap) {
			int size = valueQueueWrap.valueQueue.size();
			if (size == 0 && !valueQueueWrap.hasBeenAppendedToKeyQueue) {
				if (keyQueue.offer(key)) {
					valueQueueWrap.valueQueue.addLast(value);
					valueQueueWrap.hasBeenAppendedToKeyQueue = true;
				} else {
					logger.warn("<MultiThreadQueueWorker> Workers[" + name + "] offer fail while accept. key:" + key);
				}
			} else {
				if (size > 0 && size % warnSize == 0) {
					logger.warn("<MultiThreadQueueWorker> Workers[" + name + "] queue exceed warn size. key:" + key + " size:" + size);
				}
				valueQueueWrap.valueQueue.addLast(value);
			}
		}
	}

	public void remove(K key) {
		key_ValueQueueWrap.remove(key);
	}

	private void process() {
		while (running) {
			try {
				K key = (K) keyQueue.take();
				ValueQueueWrap<V> valueQueueWrap = key_ValueQueueWrap.get(key);
				if (valueQueueWrap != null) {
					V value = null;
					synchronized (valueQueueWrap) {
						value = valueQueueWrap.valueQueue.poll();
					}
					if (value != null) {
						processor.process(key, value);
					}
					synchronized (valueQueueWrap) {
						// 这里才重置hasAddedToKeyQueue，保证对K的处理是单线程的
						valueQueueWrap.hasBeenAppendedToKeyQueue = false;
						if (valueQueueWrap.valueQueue.size() > 0) {
							if (keyQueue.offer(key)) {
								valueQueueWrap.hasBeenAppendedToKeyQueue = true;
							} else {
								logger.warn("<MultiThreadQueueWorker> Workers[" + name + "] offer fail after process.");
							}
						}
					}
				}
			} catch (Throwable t) {
				if (running) {
					logger.error("msg send queue process error", t);
				}
			}
		}
	}

	private class WorkRunnable implements Runnable {
		@Override
		public void run() {
			process();
		}
	}

	public interface IProcessor<K, V> {
		public void process(K key, V value);
	}

	private static class ValueQueueWrap<V> {
		public final LinkedList<V> valueQueue;
		public volatile boolean hasBeenAppendedToKeyQueue;

		public ValueQueueWrap() {
			this.valueQueue = new LinkedList<V>();
			this.hasBeenAppendedToKeyQueue = false;
		}
	}

}