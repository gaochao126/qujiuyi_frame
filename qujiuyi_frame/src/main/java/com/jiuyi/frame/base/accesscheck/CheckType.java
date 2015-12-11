package com.jiuyi.frame.base.accesscheck;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.helper.ConstKeys;
import com.jiuyi.frame.util.StringUtil;

/**
 * @author xutaoyang @Date 2015年3月23日
 * 
 * @Description 访问权限的等级
 *
 * @Copyright 2015 重庆玖壹健康管理有限公司
 */
public enum CheckType {

	VISITOR(new AbsCheckHandler()), /** 任何人 */
	LOGIN(new CheckHandler_Login()), /** 需要登录 */
	Doctor(new CheckHandler_Doctor());

	private ICheckHandler checkHandler;

	private CheckType(ICheckHandler checkHandler) {
		this.checkHandler = checkHandler;
	}

	public boolean needCheck() {
		return !this.equals(VISITOR);
	}

	protected ICheckHandler getCheckHandler() {
		return checkHandler;
	}

	static interface ICheckHandler {
		ResultConst checkAccess(HttpServletRequest request);
	}

	static class AbsCheckHandler implements ICheckHandler {

		@Override
		public ResultConst checkAccess(HttpServletRequest request) {
			return ResultConst.SUCCESS;
		}

	}

	static class CheckHandler_Login implements ICheckHandler {

		@Override
		public ResultConst checkAccess(HttpServletRequest request) {
			String token = (String) request.getAttribute(ConstKeys.TOKEN);
			if (StringUtil.isNullOrEmpty(token)) {
				return ResultConst.NO_AUTH;
			}
			return ResultConst.SUCCESS;
		}
	}

	static class CheckHandler_Doctor implements ICheckHandler, ApplicationContextAware {

		ApplicationContext applicationContext;

		@Override
		public ResultConst checkAccess(HttpServletRequest request) {
			// String token = (String) request.getAttribute(ConstKeys.TOKEN);
			// if (StringUtil.isNullOrEmpty(token)) {
			// return ResultConst.NO_AUTH;
			// }
			// UserManager userManager = (UserManager)
			// this.applicationContext.getBean("UserManager");
			// if (!userManager.containsDoctor(token)) {
			// return ResultConst.NO_AUTH;
			// }
			return ResultConst.SUCCESS;
		}

		@Override
		public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
			this.applicationContext = applicationContext;
		}
	}

}
