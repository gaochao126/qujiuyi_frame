package com.jiuyi.frame.db;

import java.util.HashMap;
import java.util.Map;

import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;

public class RowData extends HashMap<String, Object>implements ISerializableObj {

	private static final long serialVersionUID = -6103819004499248305L;

	public RowData(Map<String, Object> map) {
		if (map != null && !map.isEmpty()) {
			this.putAll(map);
		}
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		for (Map.Entry<String, Object> entry : this.entrySet()) {
			res.put(entry.getKey(), entry.getValue());
		}
		return res;
	}

}
