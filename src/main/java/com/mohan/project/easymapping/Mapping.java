package com.mohan.project.easymapping;

import com.mohan.project.easymapping.convert.ConvertType;
import com.mohan.project.easymapping.generator.GeneratorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实体属性映射注解
 * @author wangyao
 * @date 2019-08-23 13:36:23
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapping {

    String source() default MappingStructConstant.DEFAULT_FIELD_NAME;

    ConvertType convert() default ConvertType.NONE;

    GeneratorType generator() default GeneratorType.NONE;
}