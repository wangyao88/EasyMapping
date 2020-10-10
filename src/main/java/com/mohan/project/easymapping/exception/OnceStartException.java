package com.mohan.project.easymapping.exception;

/**
 * @author mohan
 * @since 2019-08-23 13:36:23
 */
public class OnceStartException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "只能启动一次";

    public OnceStartException() {
        super(DEFAULT_MESSAGE);
    }
}