package com.jiuyi.frame.event;

import com.jiuyi.frame.zervice.user.model.User;

/**
 * @Author: xutaoyang @Date: 上午11:28:53
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public abstract class AbsEvent implements IEvent {

	public final User user;

	/** doctor可以为 null */
	public AbsEvent(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

}
