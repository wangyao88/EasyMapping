package com.mohan.project.easymapping.mapping.bytecode.javassist;

import java.util.List;

/**
 * 设置属性抽象类
 *
 * @author WangYao
 * @since 2020-09-17 13:17
 */
public abstract class AbstractSetter {

    /**
     * 设置属性
     *
     * @param target 目标实体实例
     * @param sources 源实体实例集合
     */
    public abstract void doSet(Object target, List<Object> sources);

}