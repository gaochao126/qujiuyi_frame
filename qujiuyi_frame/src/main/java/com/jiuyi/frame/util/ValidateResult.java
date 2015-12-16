package com.jiuyi.frame.util;

import java.util.Set;

import javax.validation.ConstraintViolation;

import com.jiuyi.frame.front.FailResult;
import com.jiuyi.frame.front.ServerResult;

/**
 * 验证结果
 * 
 * @author xutaoyang
 *
 */
public class ValidateResult {
	private boolean success;
	private String msg;

	public ValidateResult(Set<ConstraintViolation<Object>> constraintViolations) {
		this.success = constraintViolations == null || constraintViolations.size() == 0;
		if (!success) {
			StringBuilder sb = new StringBuilder();
			for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
				sb.append("字段").append(constraintViolation.getPropertyPath()).append(":").append(constraintViolation.getMessage()).append(",");
			}
			this.msg = sb.substring(0, sb.length() - 1).toString();
		} else {
			this.msg = "成功";
		}
	}

	/**
	 * true表示验证成功，false表示验证失败，具体消息调用getMsg()获取
	 * 
	 * @return
	 */
	public boolean isSuccess() {
		return this.success;
	}

	/**
	 * 验证结果说明
	 * 
	 * @return
	 */
	public String getMsg() {
		return this.msg;
	}

	public ServerResult toServerResult() {
		return success ? new ServerResult() : new FailResult(getMsg());
	}
}
