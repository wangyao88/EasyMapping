package com.mohan.project.easymapping.exception;

/**
 * @author mohan
 * @since 2019-08-23 13:36:23
 */
public class MappingStructSourceNullException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = " @MappingStruct注解的source属性为空！";

    public MappingStructSourceNullException() {
        super(DEFAULT_MESSAGE);
    }

    public MappingStructSourceNullException(String message) {
        super(message + DEFAULT_MESSAGE);
    }

    public MappingStructSourceNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public MappingStructSourceNullException(Throwable cause) {
        super(cause);
    }
}