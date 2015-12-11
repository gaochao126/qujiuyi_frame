package com.jiuyi.frame.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xutaoyang @Date 2015年3月23日
 * 
 * @Description 字符串辅助方法集合
 *
 * @Copyright 2015 重庆玖壹健康管理有限公司
 */
public class StringUtil {

	private static final Random random = new Random();
	private static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-";
	private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	public static void main(String[] args) {
		System.out.println(md5Str("123456"));
	}

	/**
	 * 获取任意位的随机字符串(0-9 a-z A-Z)
	 * 
	 * @param len
	 *            位数
	 * @return
	 */
	public static final String getRandomStr(int len) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
		}
		return sb.toString();
	}

	public static boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty();
	}

	/** 是不是电子邮件格式 */
	public static boolean isEmail(String str) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(str);
		return matcher.find();
	}

	/** 是不是手机号码格式 */
	public static boolean isPhoneNum(String key) {
		return key.matches("\\d{11}");
	}

	/**
	 * md5加密(ITS)
	 * 
	 * @param str
	 * @param charSet
	 * @return
	 */
	public synchronized static final String md5Str(String str) {
		return md5Str(str, "UTF-8");
	}

	/**
	 * md5加密(ITS)
	 * 
	 * @param str
	 * @param charSet
	 * @return
	 */
	public synchronized static final String md5Str(String str, String charSet) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			if (charSet == null) {
				messageDigest.update(str.getBytes());
			} else {
				messageDigest.update(str.getBytes(charSet));
			}
		} catch (NoSuchAlgorithmException e) {
		} catch (UnsupportedEncodingException e) {
		}
		if (messageDigest == null) {
			return str;
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();
	}

	/**
	 * 隐藏字符串的某几位，设置为*号
	 * 
	 * @param uid
	 * @return
	 */
	public static String hideStr(String str, int start, int end) {
		return hideStr(str, start, end, "*");
	}

	/**
	 * 隐藏字符串的某几位，设置为指定的符号
	 * 
	 * @param uid
	 * @return
	 */
	public static String hideStr(String str, int start, int end, String replacement) {
		String hidden = "";
		for (int index = start; index <= end; index++) {
			hidden += replacement;
		}
		return str.substring(0, start) + hidden + str.substring(end + 1, str.length());
	}

	public static String joinArr(Object[] arr, String splitter) {
		StringBuilder sb = new StringBuilder();
		for (Object obj : arr) {
			sb.append(obj.toString()).append(splitter);
		}
		if (sb.length() > 0) {
			return sb.substring(0, sb.length() - 1);
		}
		return null;
	}

	public static String escapeUnsafe(String input) {
		return input;
	}

	public static String joinArr(List<? extends Object> list, String splitter) {
		return joinArr(list, splitter, "");
	}

	public static String joinArr(List<? extends Object> list, String splitter, String defaultValue) {
		if (CollectionUtil.isNullOrEmpty(list)) {
			return defaultValue;
		}
		StringBuilder sb = new StringBuilder();
		for (Object obj : list) {
			sb.append(obj.toString()).append(splitter);
		}
		if (sb.length() > 0) {
			return sb.substring(0, sb.length() - 1);
		}
		return defaultValue;
	}

}
