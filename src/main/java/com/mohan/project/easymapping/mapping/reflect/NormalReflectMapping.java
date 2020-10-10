package com.mohan.project.easymapping.mapping.reflect;


import com.mohan.project.easymapping.MappingParameter;
import com.mohan.project.easymapping.EasyMappingConstant;
import com.mohan.project.easymapping.convert.ConvertType;
import com.mohan.project.easymapping.convert.Converts;
import com.mohan.project.easymapping.exception.ConfigureTargetFieldFromSourceValueException;
import com.mohan.project.easymapping.exception.MappingIndexOutOfBoundsException;
import com.mohan.project.easymapping.exception.ObtainSourceFieldValueException;
import com.mohan.project.easymapping.generator.Generator;
import com.mohan.project.easymapping.generator.GeneratorType;
import com.mohan.project.easymapping.generator.Generators;
import com.mohan.project.easytools.common.ObjectTools;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * 实体属性映射器
 *
 * @author mohan
 * @since 2019-08-23 13:36:23
 */
public final class NormalReflectMapping {

    private final Object lock = new Object();

    private NormalReflectMapping() {
    }

    private static class Single {
        private static final NormalReflectMapping NORMAL_REFLECT_MAPPING = new NormalReflectMapping();
    }

    public static NormalReflectMapping getInstance() {
        return Single.NORMAL_REFLECT_MAPPING;
    }

    public <T> Optional<T> mapping(Object target, List<Object> sources, Collection<MappingParameter> mappingParameters) {
        for (MappingParameter mappingParameter : mappingParameters) {
            Field targetField = mappingParameter.getTarget();
            Generator generator = mappingParameter.getGenerator();
            Object sourceValue = null;
            if (Generator.isNotDefault(generator)) {
                if (mappingParameter.isNeedSourceField()) {
                    sourceValue = getSourceFieldValue(sources, mappingParameter);
                }
                Object customerGenerateValue = generator.doGenerate(sourceValue);
                setTargetFieldValue(target, targetField, customerGenerateValue, mappingParameter);
                continue;
            }
            GeneratorType generatorType = mappingParameter.getGeneratorType();
            if (GeneratorType.NONE != generatorType) {
                Object generateValue = Generators.generator(generatorType);
                setTargetFieldValue(target, targetField, generateValue, mappingParameter);
                continue;
            }
            sourceValue = getSourceFieldValue(sources, mappingParameter);
            if (ObjectTools.isNotNull(sourceValue)) {
                setTargetFieldValue(target, targetField, sourceValue, mappingParameter);
            }
        }
        return Optional.of((T) target);
    }

    private Object getSourceFieldValue(List<Object> sources, MappingParameter mappingParameter) {
        Object sourceValue = null;
        ConvertType convertType = mappingParameter.getConvertType();
        int index = mappingParameter.getIndex();
        if (index != EasyMappingConstant.DEFAULT_FIELD_INDEX) {
            if (index > sources.size() - 1 && !mappingParameter.isIgnoreException()) {
                String msg = configureMappingIndexOutOfBoundsException(sources.size(), mappingParameter);
                throw new MappingIndexOutOfBoundsException(msg);
            }
            Object source = sources.get(index);
            Iterator<Field> sourceFieldIterator = mappingParameter.getSources().iterator();
            sourceValue = recursiveGetSourceFieldValue(sourceFieldIterator, source);
            if (ConvertType.NONE != convertType) {
                sourceValue = Converts.convert(convertType, sourceValue);
            }
            return sourceValue;
        }
        String sourceClassName = mappingParameter.getSourceClassName();
        for (Object source : sources) {
            if (!source.getClass().getName().equals(sourceClassName)) {
                continue;
            }
            Iterator<Field> sourceFieldIterator = mappingParameter.getSources().iterator();
            sourceValue = recursiveGetSourceFieldValue(sourceFieldIterator, source);
            if (ConvertType.NONE != convertType) {
                sourceValue = Converts.convert(convertType, sourceValue);
            }
            if (sourceValue != null) {
                break;
            }
        }
        return sourceValue;
    }

    private String configureMappingIndexOutOfBoundsException(int length, MappingParameter mappingParameter) {
        StringBuilder msg = new StringBuilder();
        msg.append("mapping方法sources数组长度为：")
                .append(length)
                .append("，@Mapping注解的属性是：")
                .append(mappingParameter.getTarget().getName())
                .append("，index为")
                .append(mappingParameter.getIndex());
        return msg.toString();
    }

    private Object recursiveGetSourceFieldValue(Iterator<Field> sourceFieldIterator, Object source) {
        if(source == null) {
            return null;
        }
        if (sourceFieldIterator.hasNext()) {
            Field sourceField = sourceFieldIterator.next();
            Object sourceValue = null;
            synchronized (lock) {
                try {
                    sourceField.setAccessible(true);
                    sourceValue = sourceField.get(source);
                } catch (Exception e) {
                    throw new ObtainSourceFieldValueException(source, sourceField, e);
                }
            }
            if (!sourceFieldIterator.hasNext()) {
                return sourceValue;
            }
            return recursiveGetSourceFieldValue(sourceFieldIterator, sourceValue);
        }
        return null;
    }

    private void setTargetFieldValue(Object target, Field targetField, Object sourceValue, MappingParameter mappingParameter) {
        if (ObjectTools.isAnyNull(target, targetField, sourceValue)) {
            return;
        }
        //TODO 校验数据类型是否匹配
//        if (sourceValue.getClass().isAssignableFrom(targetField.getType())) {
//
//        }
        synchronized (lock) {
            try {
                targetField.setAccessible(true);
                targetField.set(target, sourceValue);
            } catch (Exception e) {
                if (!mappingParameter.isIgnoreException()) {
                    throw new ConfigureTargetFieldFromSourceValueException(target, targetField, sourceValue, e);
                }
            }
        }
    }
}
