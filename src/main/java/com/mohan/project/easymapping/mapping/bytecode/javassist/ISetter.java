package com.mohan.project.easymapping.mapping.bytecode.javassist;

import com.mohan.project.easymapping.MappingParameter;

import java.util.List;

/**
 * @author WangYao
 * @since 2020-09-17 15:58
 */
public interface ISetter {

    /**
     * 设置属性
     *
     * @param target 目标实体实例
     * @param sources 源实体实例集合
     */
    void doSet(Object target, List<Object> sources);

    /**
     * 设置属性
     *
     * @param target 目标实体实例
     * @param sources 源实体实例集合
     * @param mappingParameters 实体属性映射属性集合
     */
    void doSet(Object target, List<Object> sources, List<MappingParameter> mappingParameters);
}
