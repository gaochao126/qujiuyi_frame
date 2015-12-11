package com.jiuyi.frame.stats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.front.ServerResult;

/**
 * @Author: xutaoyang @Date: 下午4:47:45
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Controller
public class StatController {

	private static final String CMD = "stat";

	@Autowired
	StatsService service;

	@RequestMapping(CMD)
	@ResponseBody
	public ServerResult loadStatInfo(@Param("pwd") String password) {
		if (!password.equals("gulugulu!!!")) {
			return new ServerResult();
		}
		return new ServerResult("stat", service.getData());
	}
}
