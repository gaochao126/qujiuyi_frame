package com.jiuyi.frame.base;

import java.util.concurrent.ConcurrentHashMap;

import com.jiuyi.frame.event.EventType;
import com.jiuyi.frame.event.annotations.ListenEvent;
import com.jiuyi.frame.event.events.EventLogout;
import com.jiuyi.frame.zervice.user.model.User;

public abstract class ManagerBase<U extends User, T> {

	protected ConcurrentHashMap<Integer, T> userId_info = new ConcurrentHashMap<Integer, T>();

	@ListenEvent(EventType.USER_LOGOUT)
	public void onLogout(EventLogout event) {
		User user = event.getUser();
		this.userId_info.remove(user.getId());
	}

	/** 先从内存中取，为空则从数据库取，再为空则新建一个默认的状态 */
	public T loadInfoBase(U user) {
		T info = userId_info.get(user.getId());
		if (info != null) {
			return info;
		}
		info = constructInfo(user);
		if (info != null) {
			this.userId_info.putIfAbsent(user.getId(), info);
		}
		return info;
	}

	/** 构造对象 */
	protected abstract T constructInfo(U user);
}
