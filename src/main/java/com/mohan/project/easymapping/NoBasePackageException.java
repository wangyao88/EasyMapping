package com.mohan.project.easymapping;


/**
 * 未指定需要扫描的包
 * @author mohan
 * @date 2019-08-30 09:42:18
 */
public class NoBasePackageException extends Exception {

    private static final String DEFAULT_MESSAGE = "未指定需要扫描的包。请按如下代码启动实体属性映射器\nMappingStructStarterBuilder.newBuilder().basePackages(basePackage1, basePackage2, ...).build().start();\n";

    public NoBasePackageException() {
        this(DEFAULT_MESSAGE);
    }

    public NoBasePackageException(String message) {
        super(message);
    }
}