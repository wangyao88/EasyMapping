package com.mohan.project.easymapping.mapping.bytecode.javassist;

import com.google.common.collect.Maps;
import com.mohan.project.easymapping.MappingParameter;
import com.mohan.project.easymapping.mapping.BaseMapping;
import com.mohan.project.easytools.common.CollectionTools;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 利用字节码进行映射的基础类
 *
 * @author WangYao
 * @since 2020-09-17 10:49
 */
public class BaseByteCodeMapping extends BaseMapping {

    protected static final Map<String, AbstractSetter> SETTER_MAP = Maps.newConcurrentMap();
    protected static final Map<String, List<Field>> TARGET_FIELD_NAME_MAP = Maps.newConcurrentMap();

    @Override
    protected <T> Optional<T> doSmartMapping(Object target, List<Object> sources) {
        return SmartByteCodeMapping.getInstance().mapping(target, sources);
    }

    @Override
    protected <T> Optional<T> doNormalMapping(Object target, List<Object> sources, Collection<MappingParameter> mappingParameters) {
        return NormalByteCodeMapping.getInstance().mapping(target, sources, mappingParameters);
    }

    protected String getJoinedClassName(Object target, List<Object> sources) {
        return target.getClass().getSimpleName() + sources.stream().map(obj -> obj.getClass().getSimpleName()).collect(Collectors.joining());
    }

    protected void putSetter(String joinedClassName, AbstractSetter setter) {
        SETTER_MAP.put(joinedClassName, setter);
    }

    protected List<Field> getFieldNames(Object object) {
        String targetName = object.getClass().getName();
        List<Field> fieldNames = TARGET_FIELD_NAME_MAP.get(targetName);
        if(CollectionTools.isEmpty(fieldNames)) {
            fieldNames = Arrays.stream(object.getClass().getDeclaredFields()).collect(Collectors.toList());
            putFieldNames(targetName, fieldNames);
        }
        return fieldNames;
    }

    protected void putFieldNames(String className, List<Field> fieldNames) {
        TARGET_FIELD_NAME_MAP.put(className, fieldNames);
    }
}