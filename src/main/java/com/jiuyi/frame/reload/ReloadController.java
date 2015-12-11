package com.jiuyi.frame.reload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.front.ServerResult;

/**
 * @Author: xutaoyang @Date: 下午7:50:22
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Controller
public class ReloadController {

	private static final String CMD = "reload_";
	private static final String CMD_LOAD_INFO = CMD + "info";
	private static final String CMD_RELOAD = "reload";

	@Autowired
	ReloadManager manager;

	@RequestMapping(CMD_LOAD_INFO)
	@ResponseBody
	public ServerResult loadReloadInfo(@Param("pwd") String password) {
		if (!password.equals("gulugulu!!!")) {
			return new ServerResult();
		}
		ServerResult res = new ServerResult();
		res.put("reloader", manager.reloaders());
		return res;
	}

	@RequestMapping(CMD_RELOAD)
	@ResponseBody
	public ServerResult reload(@Param("pwd") String password, @Param("reloader") String reloaderName) {
		if (!password.equals("gulugulu!!!")) {
			return new ServerResult();
		}
		return manager.reload(reloaderName);
	}

}
