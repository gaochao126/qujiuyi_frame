package com.jiuyi.frame.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.jiuyi.frame.db.RowData;
import com.jiuyi.frame.db.mappers.MapperService;
import com.jiuyi.frame.util.CollectionUtil;
import com.jiuyi.frame.util.ObjectUtil;
import com.jiuyi.frame.util.StringUtil;

/**
 * 
 * @author xutaoyang @Date 2015年3月26日
 * 
 * @Description db访问父类，注入了jdbcTemplate
 *
 * @Copyright 2015 重庆玖壹健康管理有限公司
 */
public class DbBase {

	@Autowired
	private MapperService mapperService;

	protected JdbcTemplate jdbc;

	protected SimpleJdbcInsert insert;

	protected NamedParameterJdbcTemplate namedJdbc;

	@Autowired
	public void setJdbc(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	protected String getTableName() {
		return null;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		if (!StringUtil.isNullOrEmpty(getTableName())) {
			this.insert = new SimpleJdbcInsert(dataSource).withTableName(getTableName()).usingGeneratedKeyColumns("id");
		}
	}

	@Autowired
	public void setJdbc(NamedParameterJdbcTemplate namedJdbc) {
		this.namedJdbc = namedJdbc;
	}

	protected Integer queryForInteger(String sql) {
		Integer res = queryForObject(sql, Integer.class);
		return res == null ? 0 : res;
	}

	protected Integer queryForInteger(String sql, Object... args) {
		Integer res = queryForObject(sql, args, Integer.class);
		return res == null ? 0 : res;
	}

	protected String queryForString(String sql) {
		return queryForObject(sql, String.class);
	}

	protected String queryForString(String sql, Object... args) {
		return queryForObject(sql, args, String.class);
	}

	protected <T> T queryForObject(String sql, RowMapper<T> builder) {
		List<T> list = jdbc.query(sql, builder);
		return CollectionUtil.isNullOrEmpty(list) ? null : list.get(0);
	}

	protected <T> T queryForObject(String sql, Class<T> clazz) {
		List<T> list = jdbc.queryForList(sql, clazz);
		return CollectionUtil.isNullOrEmpty(list) ? null : list.get(0);
	}

	protected <T> T queryForObject(String sql, Object[] args, Class<T> clazz) {
		List<T> list = jdbc.queryForList(sql, args, clazz);
		return CollectionUtil.isNullOrEmpty(list) ? null : list.get(0);
	}

	protected <T> T queryForObject(String sql, Object[] args, RowMapper<T> builder) {
		List<T> list = jdbc.query(sql, args, builder);
		return CollectionUtil.isNullOrEmpty(list) ? null : list.get(0);
	}

	protected SqlRowSet queryForSqlRowSet(String sql) {
		return jdbc.queryForRowSet(sql);
	}

	protected SqlRowSet queryForSqlRowSet(String sql, Object... args) {
		return jdbc.queryForRowSet(sql, args);
	}

	protected Map<String, Object> queryForMap(String sql) {
		return jdbc.queryForMap(sql);
	}

	protected Map<String, Object> queryForMap(String sql, Object... args) {
		return jdbc.queryForMap(sql, args);
	}

	protected <T> List<T> queryForList(String sql, Class<T> clazz) {
		return jdbc.query(sql, mapperService.getRowMapper(clazz));
	}

	protected <T> List<T> queryForList(String sql, Object[] args, Class<T> clazz) {
		return jdbc.query(sql, args, mapperService.getRowMapper(clazz));
	}

	protected <T> T queryForObjectDefaultBuilder(String sql, Class<T> clazz) {
		List<T> list = queryForList(sql, clazz);
		return CollectionUtil.isNullOrEmpty(list) ? null : list.get(0);
	}

	protected <T> T queryForObjectDefaultBuilder(String sql, Object[] args, Class<T> clazz) {
		List<T> list = queryForList(sql, args, clazz);
		return CollectionUtil.isNullOrEmpty(list) ? null : list.get(0);
	}

	protected RowData queryForRowData(String sql) {
		return new RowData(queryForMap(sql));
	}

	protected RowData queryForRowData(String sql, Object... args) {
		return new RowData(queryForMap(sql, args));
	}

	protected void updateObject(String sql, Object obj) {
		namedJdbc.update(sql, ObjectUtil.introspect(obj));
	}

	protected void updateObjectFormatSql(String sql, Object obj) {
		String cmd = String.format(sql, ObjectUtil.getFieldAsUpdateColumns(obj.getClass()));
		this.updateObject(cmd, obj);
	}

	protected void updateObjectFormatSql(String sql, Object obj, String... excepts) {
		String cmd = String.format(sql, ObjectUtil.getFieldAsUpdateColumns(obj.getClass(), excepts));
		this.updateObject(cmd, obj);
	}

	protected void insertObjectFormatSql(String sql, Object obj) {
		String cmd = String.format(sql, ObjectUtil.getFieldAsSelectColumns(obj.getClass()), ObjectUtil.getFieldAsInsertColumns(obj.getClass()));
		this.updateObject(cmd, obj);
	}

	protected void insertObjectFormatSql(String sql, Object obj, String... excepts) {
		String cmd = String.format(sql, ObjectUtil.getFieldAsSelectColumns(obj.getClass(), excepts), ObjectUtil.getFieldAsInsertColumns(obj.getClass(), excepts));
		this.updateObject(cmd, obj);
	}

	protected int insertReturnId(Object obj) {
		Number res = this.insert.executeAndReturnKey(ObjectUtil.introspect(obj));
		return ((Number) res).intValue();
	}

	protected long insertReturnLong(Object obj) {
		Number res = this.insert.executeAndReturnKey(ObjectUtil.introspect(obj));
		return ((Number) res).longValue();
	}

	protected List<RowData> queryForRowDataList(String sql) {
		List<Map<String, Object>> list = jdbc.queryForList(sql);
		List<RowData> res = new ArrayList<>(list.size());
		for (Map<String, Object> map : list) {
			res.add(new RowData(map));
		}
		return res;
	}

	protected List<RowData> queryForRowDataList(String sql, Object... args) {
		List<Map<String, Object>> list = jdbc.queryForList(sql, args);
		List<RowData> res = new ArrayList<>(list.size());
		for (Map<String, Object> map : list) {
			res.add(new RowData(map));
		}
		return res;
	}

	protected String formatSelectSql(String sql, Class<?> clazz, String... excepts) {
		String columns = ObjectUtil.getFieldAsSelectColumns(clazz, excepts);
		return String.format(sql, columns);
	}

	/** 页码转为limit的第一个参数 */
	protected int startIndex(int page, int pageSize) {
		return (page - 1) * pageSize;
	}

	/** 数据条数转换为页数 */
	protected int getPage(int size, int pageSize) {
		return Math.max(1, (size - 1) / pageSize + 1);
	}
}
