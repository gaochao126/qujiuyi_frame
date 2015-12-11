package com.jiuyi.frame.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xutaoyang    @Date 2015年3月23日
 * 
 * @Description 一些用于数值计算的工具方法 
 *
 * @Copyright 2015 重庆玖壹健康管理有限公司
 */
public class NumberUtil {
	/**
	 * 对double数据进行取精度.
	 * 
	 * @param value
	 *            double数据.
	 * @param scale
	 *            精度位数(保留的小数位数).
	 * @param roundingMode
	 *            精度取值方式.
	 * @return 精度计算后的数据.
	 */
	public static double round(double value, int scale, int roundingMode) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(scale, roundingMode);
		double d = bd.doubleValue();
		bd = null;
		return d;
	}

	/**
	 * double 相加
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public double add(double d1, double d2) {
		BigDecimal bd1 = new BigDecimal(Double.toString(d1));
		BigDecimal bd2 = new BigDecimal(Double.toString(d2));
		return bd1.add(bd2).doubleValue();
	}

	/**
	 * double 相减
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public double sub(double d1, double d2) {
		BigDecimal bd1 = new BigDecimal(Double.toString(d1));
		BigDecimal bd2 = new BigDecimal(Double.toString(d2));
		return bd1.subtract(bd2).doubleValue();
	}

	/**
	 * double 乘法
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public double mul(double d1, double d2) {
		BigDecimal bd1 = new BigDecimal(Double.toString(d1));
		BigDecimal bd2 = new BigDecimal(Double.toString(d2));
		return bd1.multiply(bd2).doubleValue();
	}

	/**
	 * double 除法
	 * 
	 * @param d1
	 * @param d2
	 * @param scale
	 *            四舍五入 小数点位数
	 * @return
	 */
	public double div(double d1, double d2, int scale) {
		BigDecimal bd1 = new BigDecimal(Double.toString(d1));
		BigDecimal bd2 = new BigDecimal(Double.toString(d2));
		return bd1.divide(bd2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public static int parseInt(String input) {
		return parseDouble(input).intValue();
	}

	public static Double parseDouble(String input) {
		return parseDoubles(input)[0];
	}

	public static Double[] parseDoubles(String input) {
		String[] strArr = input.split("[^\\d|.]");
		List<Double> list = new ArrayList<Double>(strArr.length);
		for (int index = 0; index < strArr.length; index++) {
			if (!StringUtil.isNullOrEmpty(strArr[index])) {
				list.add(Double.parseDouble(strArr[index]));
			}
		}
		return list.toArray(new Double[list.size()]);
	}
}
