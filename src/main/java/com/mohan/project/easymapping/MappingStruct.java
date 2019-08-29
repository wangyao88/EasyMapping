package com.mohan.project.easymapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实体属性映射注解
 * @author wangyao
 * @date 2019-08-23 13:36:23
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MappingStruct {

    Class<?> source();
}