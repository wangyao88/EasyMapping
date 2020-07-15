package com.mohan.project.easymapping;



import com.mohan.project.easymapping.generator.Generator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定制化属性默认值生成规则注解
 * 优先级大于@Mapping的generator配置属性
 * 优先级大于@Mapping的index配置属性
 * 必须同@Mapping一起使用，否则该注解不生效
 *
 * @author WangYao
 * @since 2019-08-23 13:36:23
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomerGenerator {

    boolean needSourceField() default false;

    Class<? extends Generator>  customerGenerator();
}