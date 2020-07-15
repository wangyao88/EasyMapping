package com.mohan.project.easymapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实体属性映射注解
 * @author WangYao
 * @since 2019-08-23 13:36:23
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MappingStruct {

    Class<?>[] source();

    boolean ignoreMissing() default false;

    /**
     * 忽略属性映射时发生的异常
     * @return true: 忽略， false：不忽略
     */
    boolean ignoreException() default false;
}