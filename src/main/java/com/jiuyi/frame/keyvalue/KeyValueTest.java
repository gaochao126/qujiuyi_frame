package com.jiuyi.frame.keyvalue;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: xutaoyang @Date: 上午10:21:48
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Component
public class KeyValueTest {

	@Autowired
	KeyValueService keyValueService;

	@PostConstruct
	public void test() {
		// keyValueService.keyValueForever.setValue(1, "1", new Date());
		// Date value = keyValueService.keyValueForever.getValueDate(1, "1");
		// System.out.println(DateUtil.date2Str(value));
		//
		// keyValueService.keyValueForever.delete(1, "1");
		// Date value1 = keyValueService.keyValueForever.getValueDate(1, "1");
		// System.out.println(value1);
	}

}
