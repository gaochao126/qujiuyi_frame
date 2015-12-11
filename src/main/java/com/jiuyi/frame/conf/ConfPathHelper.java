package com.jiuyi.frame.conf;

/**
 * @author xutaoyang @Date 2015年3月23日
 * 
 * @Description 配置文件路径管理类
 *
 * @Copyright 2015 重庆玖壹健康管理有限公司
 */
public class ConfPathHelper {

	private static final String CONF_DIR = "D:\\server\\config\\";

	public static final String getPath(String fileName) {
		return CONF_DIR + fileName;
	}

}
