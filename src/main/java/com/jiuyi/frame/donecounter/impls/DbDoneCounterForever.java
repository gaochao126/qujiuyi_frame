package com.jiuyi.frame.donecounter.impls;

import org.springframework.stereotype.Service;

/**
 * @Author: xutaoyang @Date: 下午6:42:33
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class DbDoneCounterForever extends DbDoneCounter {

	public DbDoneCounterForever() {
		super("t_doctor_counter_forever");
	}

}
