package com.mohan.project.easymapping.exception;

/**
 * @author mohan
 * @since 2019-08-23 13:36:23
 */
public class AttributeNotExistException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "原实体类不存在指定属性 ";

    public AttributeNotExistException() {
        super(DEFAULT_MESSAGE);
    }

    public AttributeNotExistException(String message) {
        super(DEFAULT_MESSAGE + message);
    }

    public AttributeNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public AttributeNotExistException(Throwable cause) {
        super(cause);
    }
}