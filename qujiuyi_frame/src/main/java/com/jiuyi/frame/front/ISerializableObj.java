package com.jiuyi.frame.front;

/**
 * @author xutaoyang @Date 2015年3月23日
 * 
 * @Description java object序列化成map的接口。方便jackon转换成json格式。可以直接用Map<Object, Object>
 *              替代。但是泛型写起来有点儿麻烦。 这里定义一个统一的接口，也方便一些统一的处理
 *
 * @Copyright 2015 重庆玖壹健康管理有限公司
 */
public interface ISerializableObj {

	public MapObject serializeToMapObject();

}
