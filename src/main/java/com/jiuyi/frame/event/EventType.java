package com.jiuyi.frame.event;

/**
 * @Author: xutaoyang @Date: 下午5:36:28
 *
 * @Description 所有的事件需要在这里注册，构造函数的参数表示同步处理或者异步处理
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public enum EventType {
	/** 同步事件 */
	USER_REG, // 成功注册
	USER_LOGIN, // 登录
	USER_LOGOUT, // 登出

	/** 异步事件 */
	DEMO(EventHandleType.ASYN), ;// demo

	public final EventHandleType handleType;

	private EventType() {
		this.handleType = EventHandleType.SYN;
	}

	private EventType(EventHandleType handleType) {
		this.handleType = handleType;
	}

}
