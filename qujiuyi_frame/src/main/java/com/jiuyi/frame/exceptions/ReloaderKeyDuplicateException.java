package com.jiuyi.frame.exceptions;

public class ReloaderKeyDuplicateException extends Exception {

	String key;

	public ReloaderKeyDuplicateException(String key) {
		this.key = key;
	}

	private static final long serialVersionUID = -875835507519812982L;

	@Override
	public String getMessage() {
		return "reloader key is existed:" + key;
	}
}
