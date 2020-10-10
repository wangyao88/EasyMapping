package com.mohan.project.easymapping.exception;

import java.lang.reflect.Field;

/**
 * @author mohan
 * @since 2019-08-23 13:36:23
 */
public class ConfigureTargetFieldFromSourceValueException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "原实体类不存在指定属性 ";

    public ConfigureTargetFieldFromSourceValueException() {
        super(DEFAULT_MESSAGE);
    }

    public ConfigureTargetFieldFromSourceValueException(String message) {
        super(message);
    }

    public ConfigureTargetFieldFromSourceValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigureTargetFieldFromSourceValueException(Throwable cause) {
        super(cause);
    }

    public ConfigureTargetFieldFromSourceValueException(Object target, Field targetField, Object sourceValue, Throwable cause) {
        super("类名：[" + target.getClass().getName() +
                "]，" +
                "属性名：[" +
                targetField.getName() +
                "]，属性类型：[" +
                targetField.getType().getName() +
                "]，目标属性类型：[" +
                sourceValue.getClass().getName() +
                "]，目标属性值：[" +
                sourceValue +
                "]", cause);
    }
}