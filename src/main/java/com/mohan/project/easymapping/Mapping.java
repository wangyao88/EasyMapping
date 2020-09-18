package com.mohan.project.easymapping;

import com.mohan.project.easymapping.convert.ConvertType;
import com.mohan.project.easymapping.generator.GeneratorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实体属性映射注解
 *
 * @author WangYao
 * @since 2019-08-23 13:36:23
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapping {

    String source() default EasyMappingConstant.DEFAULT_FIELD_NAME;

    /**
     * -1 该属性失效
     * 取值从0开始
     * 0对应@MappingStruct注解的source属性数组的第一个元素，1对应第二个元素，以此类推
     * 未指定 用最后一个匹配的Class类型的属性进行设值
     * 指定
     *  未越界 用指定的Class类型的最后一个实例的属性进行设值
     *  越界 空值
     *
     * @return 该属性来源于@MappingStruct注解的source数组元素的下标
     */
    int index() default EasyMappingConstant.DEFAULT_FIELD_INDEX;

    ConvertType convert() default ConvertType.NONE;

    /**
     * 优先级大于index属性
     * @return
     */
    GeneratorType generator() default GeneratorType.NONE;


}