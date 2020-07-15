package com.mohan.project.easymapping.mapping;

import java.util.Optional;

/**
 * 实体属性映射器
 * @author WangYao
 * @since 2019-08-23 13:36:23
 */
public interface Mapping {

    void mapping(boolean useSmartMode, Object target, Object ...sources);

    <T> Optional<T> mapping(boolean useSmartMode, Class<T> targetClass, Object ...sources);
}
