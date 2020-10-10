package com.mohan.project.easymapping.parser;

import com.google.common.collect.Lists;
import com.mohan.project.easymapping.Mapping;
import com.mohan.project.easymapping.MappingParameter;
import com.mohan.project.easymapping.MappingStruct;
import com.mohan.project.easymapping.generator.Generator;
import com.mohan.project.easymapping.generator.GeneratorType;
import com.mohan.project.easytools.common.ArrayTools;
import com.mohan.project.easytools.common.CollectionTools;
import com.mohan.project.easytools.common.ObjectTools;
import com.mohan.project.easytools.common.StringTools;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 实体属性配置器管理类
 *
 * @author mohan
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
            List<MappingParameter> currentMappingParameterList = Lists.newArrayList();
            if (CollectionTools.isEmpty(needMappingFields)) {
                parserParameter.setNeedMappingFields(Stream.of(declaredFields).collect(Collectors.toList()));
                currentMappingParameterList.addAll(ConfigurationFactory.getConfiguration(ConfigurationType.ALL).config(parserParameter));
            } else {
                parserParameter.setNeedMappingFields(needMappingFields);
                currentMappingParameterList.addAll(ConfigurationFactory.getConfiguration(ConfigurationType.SIGNED).config(parserParameter));
            }
            addCurrentMappingParameterList(mappingParameters, currentMappingParameterList);
        }
        return mappingParameters;
    }

    private static void addCurrentMappingParameterList(List<MappingParameter> mappingParameters, List<MappingParameter> currentMappingParameterList) {
        for (MappingParameter currentMappingParameter : currentMappingParameterList) {
            Optional<MappingParameter> storedSameMappingParameterOptional = mappingParameters.stream()
                    .filter(storedMappingParameter ->
                            storedMappingParameter.getTarget().getName().equals(currentMappingParameter.getTarget().getName()))
                    .findFirst();
            if (storedSameMappingParameterOptional.isPresent()) {
                MappingParameter storedSameMappingParameter = storedSameMappingParameterOptional.get();
                int storedScore = score(storedSameMappingParameter);
                int currentScore = score(currentMappingParameter);
                if (currentScore > storedScore) {
                    replaceMappingParameter(mappingParameters, storedSameMappingParameter, currentMappingParameter);
                }
                if (currentScore == storedScore) {
                    mappingParameters.add(currentMappingParameter);
                }
            }else {
                mappingParameters.add(currentMappingParameter);
            }
        }
    }

    private static void replaceMappingParameter(List<MappingParameter> mappingParameters,
                                                MappingParameter storedSameMappingParameter,
                                                MappingParameter currentMappingParameter) {
        mappingParameters.replaceAll(mappingParameter -> {
            if (mappingParameter.getTarget().getName().equals(storedSameMappingParameter.getTarget().getName())) {
                return currentMappingParameter;
            }
            return mappingParameter;
        });
    }

    /**
     * 根据属性值评分 得分高的应该留下，替换掉得分低的
     * 各属性分值参考MappingParameterFieldScore
     *
     * @param mappingParameter 实体属性映射属性
     * @return 实体属性映射属性得分
     */
    private static int score(MappingParameter mappingParameter) {
        if (ObjectTools.isNull(mappingParameter)) {
            return 0;
        }
        int score = 0;
        if (ObjectTools.isNotNull(mappingParameter.getTarget())) {
            score += MappingParameterFieldScore.TARGET;
        }
        if (StringTools.isNotBlank(mappingParameter.getSourceClassName())) {
            score += MappingParameterFieldScore.SOURCE_CLASS_NAME;
        }
        if (CollectionTools.isNotEmpty(mappingParameter.getSources())) {
            score += MappingParameterFieldScore.SOURCES * mappingParameter.getSources().size();
        }
        if (GeneratorType.NONE != mappingParameter.getGeneratorType()) {
            score += MappingParameterFieldScore.GENERATOR_TYPE;
        }
        if (Generator.isNotDefault(mappingParameter.getGenerator())) {
            score += MappingParameterFieldScore.GENERATOR;
        }
        return score;
    }

    private interface MappingParameterFieldScore {
        int TARGET = 10;
        int SOURCE_CLASS_NAME = 10;
        int SOURCES = 10;
        int GENERATOR_TYPE = 100;
        int GENERATOR = 10000;
    }
}
