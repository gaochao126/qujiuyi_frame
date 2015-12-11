package com.jiuyi.frame.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.jiuyi.frame.helper.Loggers;

/**
 * @Author: xutaoyang @Date: 下午5:46:44
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class EventListenersWrapper {

	public final EventType eventType;
	private List<MethodListener> listeners = new ArrayList<>();

	public EventListenersWrapper(EventType eventType) {
		this.eventType = eventType;
	}

	public void addLinstener(Object eventListener, Method method) {
		MethodListener methodListener = new MethodListener(eventListener, method);
		this.listeners.add(methodListener);
	}

	public void callListener(AbsEvent event) {
		for (MethodListener methodListener : listeners) {
			methodListener.callMethod(event);
		}
	}

	private class MethodListener {
		Object eventListener;
		Method method;

		public MethodListener(Object eventListener, Method method) {
			this.eventListener = eventListener;
			this.method = method;
		}

		public void callMethod(AbsEvent event) {
			try {
				method.invoke(eventListener, event);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				Loggers.errf(e, "invoke listener method err,obj:%s,method:%s,event:%s", eventListener.getClass().getName(), method.getName(), event.getEventType());
			}
		}
	}
}
