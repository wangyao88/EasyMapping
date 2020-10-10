package com.mohan.project.easymapping.exception;

import java.lang.reflect.Field;

/**
 * @author mohan
 * @since 2019-08-23 13:36:23
 */
public class ObtainSourceFieldValueException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "获取源实例属性值失败";

    public ObtainSourceFieldValueException() {
        super(DEFAULT_MESSAGE);
    }

    public ObtainSourceFieldValueException(String message) {
        super(DEFAULT_MESSAGE + message);
    }

    public ObtainSourceFieldValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObtainSourceFieldValueException(Throwable cause) {
        super(cause);
    }

    public ObtainSourceFieldValueException(Object source, Field sourceField, Throwable cause) {
        super("源实例：[" + source.toString() +
                "]，" +
                "属性名：[" +
                sourceField.getName() +
                "]，属性类型：[" +
                sourceField.getType().getName() +
                "]", cause);
    }
}