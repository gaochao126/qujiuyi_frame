package com.jiuyi.frame.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author: xutaoyang @Date: 下午1:46:01
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class DateUtil {

	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private final static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public static String getCurString() {
		return sdf.format(Calendar.getInstance().getTime());
	}

	public static int getYearGap(Date from, Date to) {
		Calendar fromCa = Calendar.getInstance();
		fromCa.setTime(from);
		Calendar toCa = Calendar.getInstance();
		toCa.setTime(to);
		return toCa.get(Calendar.YEAR) - fromCa.get(Calendar.YEAR);
	}

	public static int getYearGap(Date from) {
		return getYearGap(from, new Date());
	}

	public static int calcAge(Date from) {
		Calendar dob = Calendar.getInstance();
		dob.setTime(from);
		Calendar today = Calendar.getInstance();
		int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
		if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
			age--;
		} else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
			age--;
		}
		return age;
	}

	public static String date2Str(Date date) {
		return sdf2.format(date);
	}

	public static String date2Str(Date date, String pattern) {
		return new SimpleDateFormat(pattern).format(date);
	}

}
