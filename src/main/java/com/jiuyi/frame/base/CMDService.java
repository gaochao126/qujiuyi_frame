package com.jiuyi.frame.base;

import java.util.HashSet;
import java.util.Set;

/**
 * @author xutaoyang @Date 2015年3月23日
 * 
 * @Description 负责管理所有请求url
 *
 * @Copyright 2015 重庆玖壹健康管理有限公司
 */
public class CMDService {

	private Set<String> cmds = new HashSet<String>();
	private static CMDService instance = new CMDService();

	public static CMDService instance() {
		return instance;
	}

	public synchronized void addCMD(String cmd) throws Exception {
		/*
		 * if (cmds.contains(cmd)) { throw new Exception("dumplicate cmd:" +
		 * cmd); }
		 */
		cmds.add(cmd);
	}

}
