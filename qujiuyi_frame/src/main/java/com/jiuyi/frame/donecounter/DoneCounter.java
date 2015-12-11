package com.jiuyi.frame.donecounter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.frame.donecounter.impls.DbDoneCounterDaily;
import com.jiuyi.frame.donecounter.impls.DbDoneCounterForever;
import com.jiuyi.frame.donecounter.impls.DbDoneCounterMonth;
import com.jiuyi.frame.donecounter.impls.DbDoneCounterWeekly;

/**
 * @Author: xutaoyang @Date: 下午4:32:12
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class DoneCounter {

	/** 每天，存在数据库 */
	@Autowired
	public DbDoneCounterDaily daily;

	/** 每周，存在数据库 */
	@Autowired
	public DbDoneCounterWeekly weekly;

	/** 每月，存在数据库 */
	@Autowired
	public DbDoneCounterMonth month;

	/** 永久，存在数据库 */
	@Autowired
	public DbDoneCounterForever forever;
}
