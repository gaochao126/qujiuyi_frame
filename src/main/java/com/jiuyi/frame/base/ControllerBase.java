package com.jiuyi.frame.base;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyi.frame.base.accesscheck.AccessCheckService;
import com.jiuyi.frame.exceptions.JiuyiRuntimeException;
import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.helper.Loggers;
import com.jiuyi.frame.util.DateUtil;

/**
 * @author xutaoyang @Date 2015年3月23日
 * 
 * @Description Controller 的基类，用于实现一些通用的方法，比如统计之类
 *
 * @Copyright 2015 重庆玖壹健康管理有限公司
 */
public abstract class ControllerBase {

	private String cmd;

	@ExceptionHandler
	@ResponseBody
	public ServerResult handleExp(HttpServletRequest request, Exception ex) {
		Loggers.errf(ex, "%s： server controller:<<%s>> throw exception,cmd:%s, ex:%s", DateUtil.getCurString(), this.getClass().getName(), request.getRequestURI(), ex.getMessage());
		if (ex.getClass().equals(JiuyiRuntimeException.class)) {
			JiuyiRuntimeException jiuyiRtEx = (JiuyiRuntimeException) ex;
			return new ServerResult(jiuyiRtEx.rc);
		}
		return new ServerResult(ResultConst.SERVER_ERR);
	}

	public ControllerBase() {
	}

	public ControllerBase(String cmd) throws Exception {
		this.cmd = cmd;
		AccessCheckService.instance().addLoginCheck(this);
		CMDService.instance().addCMD(cmd);
	}

	public String getCMD() {
		return cmd;
	}

}
