package com.jiuyi.frame.exceptions;

import com.jiuyi.frame.front.ResultConst;

/**
 * @Author: xutaoyang @Date: 上午11:11:18
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class JiuyiRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -3556556850031585938L;
	public final ResultConst rc;

	public JiuyiRuntimeException(ResultConst rc) {
		super(rc.desc);
		this.rc = rc;
	}

}
