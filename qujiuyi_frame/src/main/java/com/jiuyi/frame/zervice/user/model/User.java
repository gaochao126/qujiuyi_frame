package com.jiuyi.frame.zervice.user.model;

import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;

public class User implements ISerializableObj {

	private int id;
	private transient long lastAccesss;
	private transient String access_token;

	public int getStatus() {
		return 0;
	}

	public long getLastAccesss() {
		return lastAccesss;
	}

	public String getAccess_token() {
		return access_token;
	}

	public boolean isExpire(long now, int expireTime) {
		if (lastAccesss == 0) {
			lastAccesss = System.currentTimeMillis();
			return false;
		}
		long passedTime = now - lastAccesss;
		return passedTime > expireTime;
	}

	public void setLastAccess() {
		this.lastAccesss = System.currentTimeMillis();
	}

	public void setLastAccesss(long lastAccesss) {
		this.lastAccesss = lastAccesss;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("id", this.id);
		return res;
	}

}
