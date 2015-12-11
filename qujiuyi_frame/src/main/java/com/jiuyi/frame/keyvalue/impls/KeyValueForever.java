package com.jiuyi.frame.keyvalue.impls;

import org.springframework.stereotype.Service;

/**
 * @Author: xutaoyang @Date: 上午10:18:43
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class KeyValueForever extends KeyValueImpl {

	public KeyValueForever() {
		super("t_doctor_kv_forever");
	}

}
