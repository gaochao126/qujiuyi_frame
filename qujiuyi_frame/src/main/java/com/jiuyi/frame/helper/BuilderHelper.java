package com.jiuyi.frame.helper;

import java.lang.reflect.Field;

import com.jiuyi.frame.zervice.user.model.User;

/**
 * @Author: xutaoyang @Date: 下午7:24:38
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class BuilderHelper {

	private Class<?> clazz = User.class;

	public static void main(String[] args) {
		new BuilderHelper().beginBuild().serial();
	}

	private static final String BUILDER_FORMAT = "public static final RowMapper<%s> builder = new RowMapper<%s>() { \n" // 不准换行
			+ "@Override \n"// 不准换行
			+ "public %s mapRow(ResultSet rs, int rowNum) throws SQLException { \n"// 不准换行
			+ "%s  \n"// 不准换行
			+ "\treturn new %s(%s); \n"//
			+ "} \n"// 不准换行
			+ "}; \n";// 不准换行

	public BuilderHelper beginBuild() {
		String className = clazz.getSimpleName();

		StringBuilder functionBody = new StringBuilder();
		StringBuilder constructorParam = new StringBuilder();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equals("builder")) {
				continue;
			}
			String singleField = genSingleField(field);
			functionBody.append(singleField).append("\n");
			constructorParam.append(field.getName()).append(",");
		}
		if (constructorParam.length() > 0) {
			constructorParam.deleteCharAt(constructorParam.length() - 1);
		}
		System.out.println(String.format(BUILDER_FORMAT, className, className, className, functionBody.toString(), className, constructorParam.toString()));
		return this;
	}

	private static final String FIELD_FORMAT = "\t%s %s = rs.get%s(\"%s\");";

	private String genSingleField(Field field) {
		Class<?> fieldType = field.getType();
		String fieldName = field.getName();
		return String.format(FIELD_FORMAT, fieldType.getSimpleName(), fieldName, getClassSimpleName(fieldType), fieldName);
	}

	private String getClassSimpleName(Class<?> clazz) {
		String sn = clazz.getSimpleName();
		return sn.substring(0, 1).toUpperCase() + sn.substring(1);
	}

	/**
	 * ISerializableObj
	 */
	private static final String SER_BUIDLER = "MapObject res = new MapObject();\n"//
			+ "%s"//
			+ "return res;";//

	public void serial() {
		StringBuilder functionBody = new StringBuilder();
		StringBuilder constructorParam = new StringBuilder();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equals("builder")) {
				continue;
			}
			String singleField = genField(field);
			functionBody.append(singleField).append("\n");
			constructorParam.append(field.getName()).append(",");
		}
		if (constructorParam.length() > 0) {
			constructorParam.deleteCharAt(constructorParam.length() - 1);
		}
		System.out.println(String.format(SER_BUIDLER, functionBody.toString()));
	}

	private static final String RESPUT_STRING = "res.put(\"%s\", this.%s);";

	private String genField(Field field) {
		String fieldName = field.getName();
		return String.format(RESPUT_STRING, fieldName, fieldName);
	}
}
