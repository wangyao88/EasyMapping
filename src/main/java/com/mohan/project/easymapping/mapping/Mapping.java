package com.mohan.project.easymapping.mapping;

import java.util.Optional;

/**
 * 实体属性映射器
 *
 * @author WangYao
 * @since 2019-08-23 13:36:23
 */
public interface Mapping {

    /**
     * 实体映射
     *
     * @param useSmartMode 是否使用智能模式
     * @param target 目标实体实例
     * @param sources 源实体实施例数组
     */
    void mapping(boolean useSmartMode, Object target, Object ...sources);

    /**
     * 实体映射
     *
     * @param useSmartMode 是否使用智能模式
     * @param targetClass 目标实体类型
     * @param sources 源实体实施例数组
     * @param <T> 目标实体类型的泛型
     * @return Optional 目标实体实例
     */
    <T> Optional<T> mapping(boolean useSmartMode, Class<T> targetClass, Object ...sources);
}
