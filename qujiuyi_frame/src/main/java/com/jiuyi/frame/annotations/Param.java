package com.jiuyi.frame.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: xutaoyang @Date: 下午6:44:06
 *
 * @Description 需要从json中获取值的参数
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {

	public String value() default "";

	public boolean page() default false;

}
