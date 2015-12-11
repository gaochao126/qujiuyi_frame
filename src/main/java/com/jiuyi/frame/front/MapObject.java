package com.jiuyi.frame.front;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author xutaoyang @Date 2015年3月23日
 * 
 * @Description
 *
 * @Copyright 2015 重庆玖壹健康管理有限公司
 */
public class MapObject extends HashMap<String, Object>implements ISerializableObj {

	private static final long serialVersionUID = 7717460765617475167L;

	@Override
	public Object put(String key, Object value) {
		/** 前端要求，所有为空的都返回这个，我晕 */
		/*
		 * if (value == null) { value = ""; }
		 */
		return super.put(key, value);
	}

	public Object put(String key, Object value, Object defaultVal) {
		if (value == null) {
			value = defaultVal;
		}
		return super.put(key, value);
	}

	public void putObject(ISerializableObj obj) {
		if (obj != null) {
			putAll(obj.serializeToMapObject());
		}
	}

	public void putObject(String key, ISerializableObj obj) {
		put(key, obj == null ? null : obj.serializeToMapObject());
	}

	public void putObjects(String key, Collection<? extends ISerializableObj> objList) {
		if (objList == null) {
			put(key, new ArrayList<>());
		} else {
			List<MapObject> maps = new ArrayList<MapObject>(objList.size());
			for (Iterator<? extends ISerializableObj> iter = objList.iterator(); iter.hasNext();) {
				ISerializableObj serObj = iter.next();
				if (serObj != null) {
					maps.add(serObj.serializeToMapObject());
				}
			}
			put(key, maps);
		}
	}

	@Override
	public MapObject serializeToMapObject() {
		return this;
	}

	public Map<String, Object> toMap() {
		return (Map<String, Object>) this;
	}
}
