package com.mohan.project.easymapping.generator;

/**
 * 定制化属性默认值生成规则接口
 *
 * @author WangYao
 * @since 2019-08-23 13:36:23
 */
public interface Generator {

    Object doGenerate(Object sourceValue);

    static Generator getDefault()  {
        return DefaultCustomerGenerator.getInstance();
    }

    static boolean isDefault(Generator generator) {
        return getDefault().equals(generator);
    }

    static boolean isNotDefault(Generator generator) {
        return !isDefault(generator);
    }
}
