package com.mohan.project.easymapping.parser;

import com.google.common.collect.Lists;
import com.mohan.project.easymapping.MappingParameter;
import com.mohan.project.easytools.common.CollectionTools;
import com.mohan.project.easytools.common.ObjectTools;
import com.mohan.project.easytools.common.StringTools;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 实体属性配置器
 *
 * @author mohan
 * @since 2019-08-23 13:36:23
 */
public abstract class BaseConfiguration implements Configuration {

    protected Optional<List<Field>> getSourceField(String sourceFieldName, List<Field> sourceFields) {
        if(sourceFieldName.contains(StringTools.POINT)) {
            Iterator<String> sourceFieldNameIterator = StringTools.splitWithSeparator(StringTools.POINT, sourceFieldName).iterator();
            List<Field> result = Lists.newArrayList();
            recursiveGetSourceField(sourceFieldNameIterator, sourceFields, result);
            return Optional.of(result);
        }
        List<Field> results = sourceFields.stream().filter(sourceField -> sourceField.getName().equals(sourceFieldName)).collect(Collectors.toList());
        return Optional.of(results);
    }

    protected void recursiveGetSourceField(Iterator<String> sourceFieldNameIterator, List<Field> sourceFields, List<Field> results) {
        if(sourceFieldNameIterator.hasNext()) {
            String sourceFieldName = sourceFieldNameIterator.next();
            Optional<Field> result = sourceFields.stream().filter(sourceField -> sourceField.getName().equals(sourceFieldName)).findFirst();
            if(!sourceFieldNameIterator.hasNext()) {
                result.ifPresent(results::add);
                return;
            }
            if(result.isPresent()) {
                Field sourceField = result.get();
                results.add(sourceField);
                Class<?> type = sourceField.getType();
                recursiveGetSourceField(sourceFieldNameIterator, Lists.newArrayList(type.getDeclaredFields()), results);
            }
        }
    }

    protected void storeConfig(List<MappingParameter> mappingParameters, Optional<MappingParameter> mappingFieldOptional) {
        if(mappingFieldOptional.isPresent()) {
            MappingParameter mappingParameter = mappingFieldOptional.get();
            if (ObjectTools.isNotNull(mappingParameter.getGeneratorType())) {
                mappingParameters.add(mappingParameter);
                return;
            }
            if (CollectionTools.isNotEmpty(mappingParameter.getSources())) {
                mappingParameters.add(mappingParameter);
            }
        }
    }
}
