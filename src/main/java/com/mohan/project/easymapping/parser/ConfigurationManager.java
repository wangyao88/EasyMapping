package com.mohan.project.easymapping.parser;
import com.google.common.collect.Lists;
import com.mohan.project.easymapping.Mapping;
import com.mohan.project.easymapping.MappingParameter;
import com.mohan.project.easymapping.MappingStruct;
import com.mohan.project.easytools.common.ArrayTools;
import com.mohan.project.easytools.common.CollectionTools;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 实体属性配置器管理类
 * @author WangYao
 * @since 2019-08-23 13:36:23
 */
public class ConfigurationManager {

    public static List<MappingParameter> config(MappingStruct mappingStruct, Class<?> mappingStructType) {
        List<MappingParameter> mappingParameters = Lists.newArrayList();
        String name = mappingStructType.getName();
        Class<?>[] sources = mappingStruct.source();
        boolean ignoreMissing = mappingStruct.ignoreMissing();
        boolean ignoreException = mappingStruct.ignoreException();
        int length = sources.length;
        for (int i = 0; i < length; i++) {
            Class<?> source = sources[i];
            List<Field> sourceFields = Lists.newArrayList(source.getDeclaredFields());
            Field[] declaredFields = mappingStructType.getDeclaredFields();
            List<Field> needMappingFields = Stream.of(declaredFields).filter(declaredField -> ArrayTools.isNotEmpty(declaredField.getAnnotationsByType(Mapping.class))).collect(Collectors.toList());
            ParserParameter parserParameter =
                    ParserParameter.builder()
                            .name(name)
                            .ignoreMissing(ignoreMissing)
                            .ignoreException(ignoreException)
                            .sourceFields(sourceFields)
                            .source(source)
                            .sourceIndex(i)
                            .build();
            Optional<MappingParameter> mappingParameterOptional;
            if(CollectionTools.isEmpty(needMappingFields)) {
                parserParameter.setNeedMappingFields(Stream.of(declaredFields).collect(Collectors.toList()));
                mappingParameters.addAll(ConfigurationFactory.getConfiguration(ConfigurationType.ALL).config(parserParameter));
            }else {
                parserParameter.setNeedMappingFields(needMappingFields);
                mappingParameters.addAll(ConfigurationFactory.getConfiguration(ConfigurationType.SIGNED).config(parserParameter));
            }
        }
        return mappingParameters;
    }
}
