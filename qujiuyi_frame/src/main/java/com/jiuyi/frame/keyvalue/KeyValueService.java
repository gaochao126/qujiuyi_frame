package com.jiuyi.frame.keyvalue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.frame.keyvalue.impls.KeyValueForever;

/**
 * @Author: xutaoyang @Date: 上午10:02:00
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class KeyValueService {

	@Autowired
	public KeyValueForever keyValueForever;

}
