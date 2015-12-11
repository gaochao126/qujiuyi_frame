package com.jiuyi.frame.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jiuyi.frame.base.accesscheck.CheckType;

/**
 * @author xutaoyang @Date 2015年3月23日
 * 
 * @Description 访问权限注解
 *
 * @Copyright 2015 重庆玖壹健康管理有限公司
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessCheck {

	CheckType checkType() default CheckType.VISITOR;

}
