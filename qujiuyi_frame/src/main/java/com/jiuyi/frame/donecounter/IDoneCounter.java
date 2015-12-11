package com.jiuyi.frame.donecounter;

/**
 * @Author: xutaoyang @Date: 下午4:32:58
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public interface IDoneCounter {

	/** 获取计数 */
	public Integer getCount(String target, int userId);

	/** 获取+1后的计数 */
	public Integer incAndGetCount(String target, int userId);

	/** 获取+1前的计数 */
	public Integer getAndIncCount(String target, int userId);

	/** +1 */
	public void incCount(String target, int userId);

	/** +add */
	public void addCount(String target, int userId, int add);

	/** 设置为指定的计数 */
	public void setCount(String target, int userId, int count);

	/** 删除计数 */
	public void deleteCount(String target, int userId);
}
