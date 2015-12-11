package com.jiuyi.frame.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * long型转化为可读的日期类型
 * 
 * @author xutaoyang
 *
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ReadableDate {
	public String value() default "yyyy-MM-dd HH:mm";
}
