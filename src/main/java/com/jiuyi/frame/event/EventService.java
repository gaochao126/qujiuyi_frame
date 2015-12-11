package com.jiuyi.frame.event;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Service;

import com.jiuyi.frame.event.annotations.ListenEvent;
import com.jiuyi.frame.helper.Loggers;
import com.jiuyi.frame.helper.MultiThreadQueueWorker;
import com.jiuyi.frame.helper.MultiThreadQueueWorker.IProcessor;
import com.jiuyi.frame.zervice.user.model.User;

/**
 * @Author: xutaoyang @Date: 下午5:00:25
 *
 * @Description 事件相关入口类
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class EventService implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

	private volatile boolean inited = false;
	private ApplicationContext ctx;
	private Map<EventType, EventListenersWrapper> type_info = new HashMap<>();
	private MultiThreadQueueWorker<Integer, AbsEvent> eventQueue = new MultiThreadQueueWorker<>("my-event-queue", 4, 100000, new EventProcessor());

	public void registerListner(Object listener) {
		resolveListener(listener);
	}

	/**
	 * 抛出事件，对于耗时较长的处理建议使用异步事件。这里对同一个用户先后抛出的事件做了顺序处理，先抛出的事件将被先处理。这是用用户的id来做key确保的
	 * 如果事件不包含doctor对象（doctor对象为null），那么key将会被指定为1。
	 */
	public void dispatchEvent(AbsEvent event) {
		EventType eventType = event.getEventType();
		if (eventType.handleType.equals(EventHandleType.ASYN)) {// 异步处理，加到queue里
			User user = event.getUser();
			this.eventQueue.accept(user == null ? 1 : user.getId(), event);
		} else {
			this.type_info.get(eventType).callListener(event);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		this.ctx = ctx;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (inited) {
			return;
		}
		inited = true;
		this.eventQueue.start();
		/** 初始化各个事件类型 */
		for (EventType eventType : EventType.values()) {
			this.type_info.put(eventType, new EventListenersWrapper(eventType));
		}

		/** 初始化各个监听器 */
		String[] beans = ctx.getBeanDefinitionNames();
		ConfigurableListableBeanFactory clbf = ((AbstractApplicationContext) ctx).getBeanFactory();
		for (String name : beans) {
			Object bean = clbf.getSingleton(name);
			if (bean != null) {
				resolveListener(bean);
			}
		}
	}

	@PreDestroy
	public void destroy() {
		this.eventQueue.shutdown();
	}

	/** 解析监听者，方法上声明了ListenEvent的是监听方法，有监听方法的类是监听者类，一个监听者类可以有多个监听方法监听不同的事件 */
	private void resolveListener(Object listener) {
		Class<?> clazz = listener.getClass();
		Method[] methods = clazz.getDeclaredMethods();
		if (methods == null) {
			return;
		}
		for (Method method : methods) {
			ListenEvent annoListenEvent = method.getAnnotation(ListenEvent.class);
			if (annoListenEvent == null) {
				continue;
			}
			method.setAccessible(true);
			EventType listenType = annoListenEvent.value();
			this.type_info.get(listenType).addLinstener(listener, method);
		}
	}

	private class EventProcessor implements IProcessor<Integer, AbsEvent> {

		@Override
		public void process(Integer key, AbsEvent event) {
			try {
				type_info.get(event.getEventType()).callListener(event);
			} catch (Exception e) {
				Loggers.err("<<EventProcessor call listener err>>", e);
			}
		}

	}
}
