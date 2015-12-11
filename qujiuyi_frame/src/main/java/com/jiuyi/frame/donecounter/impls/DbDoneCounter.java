package com.jiuyi.frame.donecounter.impls;

import com.jiuyi.frame.base.DbBase;
import com.jiuyi.frame.donecounter.IDoneCounter;

/**
 * @Author: xutaoyang @Date: 下午4:48:30
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class DbDoneCounter extends DbBase implements IDoneCounter {

	protected String SELECT_COUNT = "SELECT `count` FROM `#tableName#` WHERE `target`=? AND `userId`=?";
	protected String ADD_COUNT = "INSERT `#tableName#`(`target`,`userId`,`count`) VALUE(?,?,1) ON DUPLICATE KEY UPDATE `count`=`count`+?";
	protected String SET_COUNT = "INSERT `#tableName#`(`target`,`userId`,`count`) VALUE(?,?,?) ON DUPLICATE KEY UPDATE `count`=?";
	protected String DELETE_COUNT = "DELETE FROM `#tableName#` WHERE `target`=? AND `userId`=?";

	public DbDoneCounter(String tableName) {
		this.ADD_COUNT = ADD_COUNT.replaceAll("#tableName#", tableName);
		this.SET_COUNT = SET_COUNT.replaceAll("#tableName#", tableName);
		this.SELECT_COUNT = SELECT_COUNT.replaceAll("#tableName#", tableName);
		this.DELETE_COUNT = DELETE_COUNT.replaceAll("#tableName#", tableName);
	}

	@Override
	public Integer getCount(String target, int userId) {
		return queryForInteger(SELECT_COUNT, new Object[] { target, userId });
	}

	@Override
	public Integer incAndGetCount(String target, int userId) {
		incCount(target, userId);
		return getCount(target, userId);
	}

	@Override
	public Integer getAndIncCount(String target, int userId) {
		Integer res = getCount(target, userId);
		incCount(target, userId);
		return res;
	}

	@Override
	public void incCount(String target, int userId) {
		addCount(target, userId, 1);
	}

	@Override
	public void addCount(String target, int userId, int add) {
		jdbc.update(ADD_COUNT, new Object[] { target, userId, add });
	}

	@Override
	public void setCount(String target, int userId, int count) {
		jdbc.update(SET_COUNT, new Object[] { target, userId, count, count });
	}

	@Override
	public void deleteCount(String target, int userId) {
		jdbc.update(DELETE_COUNT, new Object[] { target, userId });
	}

}
