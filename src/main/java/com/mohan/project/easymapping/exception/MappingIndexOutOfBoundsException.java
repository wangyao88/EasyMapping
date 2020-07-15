package com.mohan.project.easymapping.exception;

/**
 * @author WangYao
 * @since 2019-08-23 13:36:23
 */
public class MappingIndexOutOfBoundsException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "@Mapping的index值超越mapping方法sources数组界限";

    public MappingIndexOutOfBoundsException() {
        super(DEFAULT_MESSAGE);
    }

    public MappingIndexOutOfBoundsException(String message) {
        super(message);
    }

    public MappingIndexOutOfBoundsException(String message, Throwable cause) {
        super(message, cause);
    }

    public MappingIndexOutOfBoundsException(Throwable cause) {
        super(cause);
    }
}