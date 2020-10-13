package com.mohan.project.easymapping.parser;

import com.google.common.base.Throwables;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.mohan.project.easymapping.EasyMappingConstant;
import com.mohan.project.easymapping.MappingParameter;
import com.mohan.project.easymapping.MappingStruct;
import com.mohan.project.easymapping.exception.MappingStructSourceNullException;
import com.mohan.project.easytools.common.ArrayTools;
import com.mohan.project.easytools.common.StringTools;
import com.mohan.project.easytools.log.LogTools;
import com.mohan.project.strategyfactory.core.StrategyManager;
import org.reflections.Reflections;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 实体属性解析器
 *
 * @author mohan
 * @since 2019-08-23 13:36:23
 */
public class BaseParser implements Parser {

    private static final Multimap<String, MappingParameter> MAPPING_MAP = ArrayListMultimap.create();
    private static final Multimap<String, Class<?>> TARGET_SOURCES_MAP = ArrayListMultimap.create();

    private static Map<String, Collection<MappingParameter>> UNMODIFIABLE_MAPPING_MAP;
    private static Map<String, Collection<Class<?>>> UNMODIFIABLE_TARGET_SOURCES_MAP;

    private List<String> errorMessages;

    private static class Single {
        private static final Parser PARSER = new BaseParser();
    }

    private BaseParser() {
        initErrorMessages();
        initConfigurationAndValidator();
    }

    private void initErrorMessages() {
        errorMessages = Lists.newArrayList();
    }

    private void initConfigurationAndValidator() {
        StrategyManager.init(EasyMappingConstant.BASE_PACKAGE_PATH);
    }

    public static Parser getInstance() {
        return Single.PARSER;
    }

    @Override
    public void doParse(String basePackage) {
        try {
            MAPPING_MAP.clear();
            TARGET_SOURCES_MAP.clear();
            errorMessages = Lists.newArrayList();
            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> mappingStructTypes = reflections.getTypesAnnotatedWith(MappingStruct.class);
            for (Class<?> mappingStructType : mappingStructTypes) {
                String name = mappingStructType.getName();
                MappingStruct mappingStruct = mappingStructType.getAnnotation(MappingStruct.class);
                if (ArrayTools.isEmpty(mappingStruct.source())) {
                    throw new MappingStructSourceNullException(name);
                }
                List<MappingParameter> mappingParameters = ConfigurationManager.config(mappingStruct, mappingStructType);
                MAPPING_MAP.putAll(name, mappingParameters);

                TARGET_SOURCES_MAP.putAll(name, Arrays.stream(mappingStruct.source()).collect(Collectors.toList()));
            }
            UNMODIFIABLE_MAPPING_MAP = Collections.unmodifiableMap(MAPPING_MAP.asMap());
            UNMODIFIABLE_TARGET_SOURCES_MAP = Collections.unmodifiableMap(TARGET_SOURCES_MAP.asMap());
        } catch (Exception e) {
            String msg = Throwables.getStackTraceAsString(e);
            errorMessages.add(msg);
            LogTools.error(msg);
        }
    }

    @Override
    public Map<String, Collection<MappingParameter>> getParsedMappingInfo() {
        return UNMODIFIABLE_MAPPING_MAP;
    }

    @Override
    public Map<String, Collection<Class<?>>> getTargetSourcesInfo() {
        return UNMODIFIABLE_TARGET_SOURCES_MAP;
    }

    @Override
    public List<String> getErrorMessage() {
        return Collections.unmodifiableList(errorMessages);
    }

    @Override
    public void showMappingInfo() {
        System.out.println(StringTools.HR + EasyMappingConstant.PROJECT_NAME + StringTools.HR);
        for (String name : MAPPING_MAP.keySet()) {
            Collection<MappingParameter> mappingParameters = MAPPING_MAP.get(name);
            System.out.println(name + "     " + mappingParameters.size());
            for (MappingParameter mappingParameter : mappingParameters) {
                StringTools.printObject(mappingParameter);
            }
            System.out.println(StringTools.HR);
        }
        System.out.println();
    }
}
