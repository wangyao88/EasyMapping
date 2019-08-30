package com.mohan.project.easymapping;


/**
 * 指定包下包含非java文件异常
 * @author mohan
 * @date 2019-08-30 09:42:18
 */
public class ScanException extends Exception {

    private static final String DEFAULT_MESSAGE = "指定包下包含非java文件异常";

    public ScanException() {
        this(DEFAULT_MESSAGE);
    }

    public ScanException(String message) {
        super(message);
    }
}