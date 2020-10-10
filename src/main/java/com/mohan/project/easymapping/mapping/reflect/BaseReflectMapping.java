package com.mohan.project.easymapping.mapping.reflect;

import com.mohan.project.easymapping.MappingParameter;
import com.mohan.project.easymapping.mapping.BaseMapping;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 利用反射进行映射的基础类
 *
 * @author mohan
 * @since 2020-09-17 10:49
 */
public final class BaseReflectMapping extends BaseMapping {

    @Override
    protected <T> Optional<T> doSmartMapping(Object target, List<Object> sources) {
        return SmartReflectMapping.getInstance().mapping(target, sources);
    }

    @Override
    protected <T> Optional<T> doNormalMapping(Object target, List<Object> sources, Collection<MappingParameter> mappingParameters) {
        return NormalReflectMapping.getInstance().mapping(target, sources, mappingParameters);
    }
}