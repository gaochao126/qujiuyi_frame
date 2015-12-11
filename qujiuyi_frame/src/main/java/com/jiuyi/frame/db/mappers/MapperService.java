package com.jiuyi.frame.db.mappers;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.jiuyi.frame.db.DbValueHandlerComposite;

@Service
public class MapperService {

	@Autowired
	private DbValueHandlerComposite valHandler;

	private ConcurrentHashMap<Class<?>, RowMapper<?>> clazz_mapper = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")
	public <T> RowMapper<T> getRowMapper(Class<T> clazz) {
		if (clazz_mapper.containsKey(clazz)) {
			return (RowMapper<T>) clazz_mapper.get(clazz);
		}
		DefaultRowMapper<T> mapper = new DefaultRowMapper<>(clazz, valHandler);
		clazz_mapper.putIfAbsent(clazz, new DefaultRowMapper<>(clazz, valHandler));
		return mapper;
	}

}
