package com.jiuyi.frame.helper;

import org.apache.log4j.Logger;

/**
 * @author xutaoyang @Date 2015年3月23日
 * 
 * @Description 封装log4f的使用
 *
 * @Copyright 2015 重庆玖壹健康管理有限公司
 */
public class Loggers {

	private static Logger logger = Logger.getLogger("~~~JiuYi Log.");

	public static boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	public static void err(String message) {
		logger.error(message);
	}

	public static void err(String message, Throwable t) {
		logger.error(message, t);
	}

	public static void errf(String massage, Object... param) {
		logger.error(String.format(massage, param));
	}

	public static void errf(Throwable t, String massage, Object... param) {
		logger.error(String.format(massage, param), t);
	}

	public static void info(String message) {
		logger.info(message);
	}

	public static void info(String message, Throwable t) {
		logger.error(message, t);
	}

	public static void infof(String massage, Object... param) {
		logger.info(String.format(massage, param));
	}

	public static void infof(Throwable t, String massage, Object... param) {
		logger.info(String.format(massage, param), t);
	}

	public static void warn(String message) {
		logger.warn(message);
	}

	public static void warn(String message, Throwable t) {
		logger.warn(message, t);
	}

	public static void warnf(String massage, Object... param) {
		logger.warn(String.format(massage, param));
	}

	public static void warnf(Throwable t, String massage, Object... param) {
		logger.error(String.format(massage, param), t);
	}

	public static void debug(String message) {
		logger.debug(message);
	}

	public static void debug(String message, Throwable t) {
		logger.debug(message, t);
	}

	public static void debugf(String massage, Object... param) {
		logger.debug(String.format(massage, param));
	}

	public static void debugf(Throwable t, String massage, Object... param) {
		logger.debug(String.format(massage, param), t);
	}

}
