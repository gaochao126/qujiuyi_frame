package com.jiuyi.frame.front;

public class FailResult extends ServerResult {
	private static final long serialVersionUID = 8682375421073322833L;

	public FailResult(String desc) {
		super(ResultConst.FAIL.id, desc);
	}
}
