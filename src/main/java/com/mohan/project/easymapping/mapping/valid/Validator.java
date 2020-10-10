package com.mohan.project.easymapping.mapping.valid;

import java.util.List;

/**
 * 校验接口
 *
 * @author mohan
 * @since 2020-10-10 17:02
 */
public interface Validator {

    /**
     * 校验
     * @param useSmartMode 是否使用智能匹配模式
     * @param target 目标实例
     * @param sources 源实例集合
     * @return 校验结果
     */
    boolean valid(boolean useSmartMode, Object target, List<Object> sources);

    /**
     * 获取校验器具体类型
     * @return 校验器具体类型
     */
    ValidatorType getType();
}