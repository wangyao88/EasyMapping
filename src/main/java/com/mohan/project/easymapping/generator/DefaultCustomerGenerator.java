package com.mohan.project.easymapping.generator;

/**
 * 定制化属性默认值生成规则实现类
 * 缺省值
 *
 * @author WangYao
 * @since 2019-08-23 13:36:23
 */
public class DefaultCustomerGenerator implements Generator {

    private DefaultCustomerGenerator() {}

    private static class Single {
        public static final DefaultCustomerGenerator GENERATOR = new DefaultCustomerGenerator();
    }

    public static Generator getInstance() {
        return Single.GENERATOR;
    }

    public Object doGenerate(Object sourceValue) {
        return null;
    }
}
