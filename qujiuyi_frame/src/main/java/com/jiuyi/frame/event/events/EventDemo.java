package com.jiuyi.frame.event.events;

import com.jiuyi.frame.event.AbsEvent;
import com.jiuyi.frame.event.EventType;
import com.jiuyi.frame.zervice.user.model.User;

/**
 * @Author: xutaoyang @Date: 下午5:37:22
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class EventDemo extends AbsEvent {

	public final String data;

	public EventDemo(User user, String data) {
		super(user);
		this.data = data;
	}

	@Override
	public EventType getEventType() {
		return EventType.DEMO;
	}

}
