package com.jiuyi.frame.event;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

/**
 * @Author: xutaoyang @Date: 上午11:30:29
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class MethodHandletest {

	static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
	static final int run_time = 1000000;

	public static void main(String[] args) {
		try {
			MethodHandle methodHandle = LOOKUP.findVirtual(MethodHandletest.class, "test", MethodType.methodType(void.class, int.class));
			MethodHandletest test = new MethodHandletest();

			long start2 = System.currentTimeMillis();
			for (int index = 0; index < run_time; index++) {
				test.test((int) (Math.random() * 100));
			}
			long end2 = System.currentTimeMillis();
			System.out.println("direct invoke use:" + (end2 - start2));

			Method method = MethodHandletest.class.getDeclaredMethod("test", int.class);
			long start3 = System.currentTimeMillis();
			for (int index = 0; index < run_time; index++) {
				method.invoke(test, (int) (Math.random() * 100));
			}
			long end3 = System.currentTimeMillis();
			System.out.println("reflection use:" + (end3 - start3));

			long start = System.currentTimeMillis();
			for (int index = 0; index < run_time; index++) {
				// methodHandle.invoke(test, (int) (Math.random() * 100));
				methodHandle.invokeExact(test, (int) (Math.random() * 100));
			}
			long end = System.currentTimeMillis();
			System.out.println("method handle use:" + (end - start));
		} catch (NoSuchMethodException | IllegalAccessException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void test(int var) {
		var++;
		// var = (int) Math.pow(var + var * var / 1000 + 10000 - var, 10);
	}
}
