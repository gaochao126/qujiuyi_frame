/**
 * 
 */
package com.jiuyi.frame.idgen;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.jdbc.core.RowMapper;

/**
 * 生成器
 * 
 * @author xutaoyang
 *
 */
public class IdGenerator {

	private AtomicLong currId;

	private String name;

	private int start;

	private int step;

	private AtomicBoolean changed;

	protected static final RowMapper<IdGenerator> builder = new RowMapper<IdGenerator>() {

		@Override
		public IdGenerator mapRow(ResultSet rs, int rowNum) throws SQLException {
			long currId = rs.getLong("currId");
			String name = rs.getString("name");
			int start = rs.getInt("start");
			int step = rs.getInt("step");
			return new IdGenerator(currId, name, start, step);
		}
	};

	public IdGenerator(String name, int start, int step) {
		this.name = name;
		this.start = start;
		this.step = step;
		this.currId = new AtomicLong(start);
		this.changed = new AtomicBoolean(false);
	}

	/**
	 * 
	 */
	public IdGenerator(long curr, String name, int start, int step) {
		this.currId = new AtomicLong(curr);
		this.name = name;
		this.start = start;
		this.step = step;
		this.changed = new AtomicBoolean(false);
	}

	public boolean changed() {
		return this.changed.get();
	}

	public void changed(boolean changed) {
		this.changed.set(changed);
	}

	public long genIdLong() {
		this.changed.set(true);
		return this.currId.addAndGet(step);
	}

	public int genId() {
		this.changed.set(true);
		return (int) this.currId.addAndGet(step);
	}

	public void add(int step) {
		this.changed.set(true);
		this.currId.addAndGet(step);
	}

	public AtomicLong getCurrId() {
		return currId;
	}

	public void setCurrId(AtomicLong currId) {
		this.currId = currId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public AtomicBoolean getChanged() {
		return changed;
	}

	public void setChanged(AtomicBoolean changed) {
		this.changed = changed;
	}

}
