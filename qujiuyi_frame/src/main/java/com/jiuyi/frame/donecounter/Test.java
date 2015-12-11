package com.jiuyi.frame.donecounter;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: xutaoyang @Date: 下午5:47:48
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Component
public class Test {

	@Autowired
	DoneCounter doneCounter;

	@PostConstruct
	public void init() {
		// doneCounter.daily.incCount("woca", 1);
		// doneCounter.weekly.getCount("woca", 1);
		// doneCounter.month.getCount("woca", 1);
		// doneCounter.forever.getCount("woca", 1);
	}

}
