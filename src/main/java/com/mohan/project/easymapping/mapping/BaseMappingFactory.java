package com.mohan.project.easymapping.mapping;

import com.mohan.project.easymapping.mapping.bytecode.javassist.BaseByteCodeMapping;
import com.mohan.project.easymapping.mapping.reflect.BaseReflectMapping;

/**
 * @author WangYao
 * @since 2020-09-17 10:50
 */
public class BaseMappingFactory {

    private static final BaseMapping BASE_REFLECT_MAPPING = new BaseReflectMapping();
    private static final BaseMapping BASE_BYTE_CODE_MAPPING = new BaseByteCodeMapping();

    public static BaseMapping getBaseReflectMapping() {
        return BASE_REFLECT_MAPPING;
    }

    public static BaseMapping getBaseByteCodeMapping() {
        return BASE_BYTE_CODE_MAPPING;
    }

}