package com.jiuyi.frame.event.events;

import com.jiuyi.frame.event.AbsEvent;
import com.jiuyi.frame.event.EventType;
import com.jiuyi.frame.zervice.user.model.User;

/**
 * @Author: xutaoyang @Date: 上午10:43:38
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class EventLogout extends AbsEvent {

	public EventLogout(User user) {
		super(user);
	}

	@Override
	public EventType getEventType() {
		return EventType.USER_LOGOUT;
	}

}
