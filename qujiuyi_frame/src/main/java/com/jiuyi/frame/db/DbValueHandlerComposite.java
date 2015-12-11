package com.jiuyi.frame.db;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.jiuyi.frame.db.handlers.AgeValueHandler;
import com.jiuyi.frame.db.handlers.ConfigPrefixValueHandler;
import com.jiuyi.frame.db.handlers.DefaultValueHandler;
import com.jiuyi.frame.db.handlers.FromStrValueHandler;
import com.jiuyi.frame.db.handlers.ReadableDateValueHandler;

/**
 * 把数据库中的值转为java对象的字段的值
 * 
 * @author xutaoyang
 *
 */
@Service
public class DbValueHandlerComposite implements DbValueHandler {

	private List<DbValueHandler> handlers = new CopyOnWriteArrayList<>();

	@PostConstruct
	public void init() {
		handlers.add(new ConfigPrefixValueHandler());
		handlers.add(new FromStrValueHandler());
		handlers.add(new AgeValueHandler());
		handlers.add(new ReadableDateValueHandler());

		/* 默认什么都不做的hanlder，应该把它放在最后 */
		handlers.add(new DefaultValueHandler());
	}

	/**
	 * 获取handlers
	 * 
	 * @return
	 */
	public List<DbValueHandler> getHandlers() {
		return Collections.unmodifiableList(handlers);
	}

	/**
	 * 添加handler
	 * 
	 * @param handler
	 */
	public void addHandler(DbValueHandler handler) {
		/* 为了把defaultHandler放在最后 */
		int addIndex = this.handlers.size() - 1;
		addIndex = addIndex < 0 ? 0 : addIndex;
		this.handlers.add(addIndex, handler);
	}

	/**
	 * 批量添加handler
	 * 
	 * @param handlers
	 * @return
	 */
	public DbValueHandlerComposite addHandlers(List<? extends DbValueHandler> handlers) {
		if (handlers != null) {
			for (DbValueHandler handler : handlers) {
				addHandler(handler);
			}
		}
		return this;
	}

	@Override
	public boolean supportField(Field field, String columnName, Object columnValue) {
		return getHandler(field, columnName, columnValue) != null;
	}

	@Override
	public Object resolveValue(Field field, String columnName, Object columnValue) {
		DbValueHandler handler = getHandler(field, columnName, columnValue);
		Assert.notNull(handler, "Unknown return value type [" + field.getType() + "]");
		return handler.resolveValue(field, columnName, columnValue);
	}

	/**
	 * 找到第一个合适的handler进行处理
	 * 
	 * @param field
	 * @param columnName
	 * @param columnValue
	 * @return
	 */
	private DbValueHandler getHandler(Field field, String columnName, Object columnValue) {
		for (DbValueHandler handler : handlers) {
			if (handler.supportField(field, columnName, columnValue)) {
				return handler;
			}
		}
		return null;
	}

}
