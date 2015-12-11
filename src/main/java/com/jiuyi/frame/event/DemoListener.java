package com.jiuyi.frame.event;

import org.springframework.stereotype.Service;

import com.jiuyi.frame.event.annotations.ListenEvent;
import com.jiuyi.frame.event.events.EventDemo;

/**
 * @Author: xutaoyang @Date: 下午5:24:37
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class DemoListener {

	@ListenEvent(EventType.DEMO)
	public void onEvent(EventDemo event) {
		System.out.println(event.data);
	}

}
