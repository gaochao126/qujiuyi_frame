/**
 * 
 */
package com.jiuyi.frame.idgen;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jiuyi.frame.base.DbBase;

/**
 * @author xutaoyang
 *
 */
@Repository
public class IdGeneratorDao extends DbBase {

	private static final String SELECT = "SELECT * FROM `t_id_gen` WHERE `name`=?";
	private static final String UPDATE = "UPDATE `t_id_gen` SET `currId`=? WHERE `name`=?";

	@Override
	protected String getTableName() {
		return "t_id_gen";
	}

	/**
	 * @param name
	 * @return
	 */
	protected IdGenerator loadGenderatorByName(String name) {
		return queryForObject(SELECT, new Object[] { name }, IdGenerator.builder);
	}

	/**
	 * @param changedList
	 */
	protected void batchUpdate(List<IdGenerator> changedList) {
		List<Object[]> args = new ArrayList<>(changedList.size());
		for (IdGenerator generator : changedList) {
			args.add(new Object[] { generator.getCurrId().get(), generator.getName() });
		}
		super.jdbc.batchUpdate(UPDATE, args);
	}

	/**
	 * @param generator
	 */
	protected void insertGenerator(IdGenerator generator) {
		super.insertReturnId(generator);
	}

}
