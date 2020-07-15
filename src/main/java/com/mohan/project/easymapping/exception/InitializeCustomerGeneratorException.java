package com.mohan.project.easymapping.exception;

/**
 * @author WangYao
 * @since 2019-08-23 13:36:23
 */
public class InitializeCustomerGeneratorException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "实例化客户自定义属性生成器失败！";

    public InitializeCustomerGeneratorException() {
        super(DEFAULT_MESSAGE);
    }

    public InitializeCustomerGeneratorException(String message) {
        super(message);
    }

    public InitializeCustomerGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public InitializeCustomerGeneratorException(Throwable cause) {
        super(cause);
    }
}