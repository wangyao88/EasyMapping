package com.mohan.project.easymapping;

import com.google.common.base.Throwables;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.mohan.project.easymapping.convert.ConvertType;
import com.mohan.project.easymapping.convert.Converts;
import com.mohan.project.easymapping.generator.GeneratorType;
import com.mohan.project.easymapping.generator.Generators;
import com.mohan.project.easytools.common.ArrayTools;
import com.mohan.project.easytools.common.ObjectTools;
import com.mohan.project.easytools.common.StringTools;
import com.mohan.project.easytools.file.FileTools;
import com.mohan.project.easytools.log.LogTools;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 实体属性映射管理类
 * @author mohan
 * @date 2019-08-23 13:36:23
 */
public class MappingStructManager {

    public static final String PROJECT_NAME= "MappingStruct";
    private static final Multimap<String, MappingParameter> MAPPING_MAP = ArrayListMultimap.create();

    static void init(String[] basePackages, boolean enableLog) throws ScanException {
        Set<Class<?>> mappingStructTypes = Sets.newHashSet();
        try{
            Reflections reflections = new Reflections(basePackages);
            mappingStructTypes.addAll(reflections.getTypesAnnotatedWith(MappingStruct.class));
        }catch (Exception e) {
            throw new ScanException();
        }
        for (Class<?> mappingStructType : mappingStructTypes) {
            String name = mappingStructType.getName();
            MappingStruct mappingStruct = mappingStructType.getAnnotation(MappingStruct.class);
            Class<?> source = mappingStruct.source();
            List<Field> sourceFields = Lists.newArrayList(source.getDeclaredFields());
            Field[] declaredFields = mappingStructType.getDeclaredFields();
            List<Field> needMappingFields = Stream.of(declaredFields).filter(declaredField -> ArrayTools.isNotEmpty(declaredField.getAnnotationsByType(Mapping.class))).collect(Collectors.toList());
            if(needMappingFields.isEmpty()) {
                mappingAllField(name, Stream.of(declaredFields).collect(Collectors.toList()), sourceFields);
            }else {
                mappingSignedField(name, needMappingFields, sourceFields);
            }
        }
        if(enableLog) {
            showMappings();
        }
    }

    private static void mappingAllField(String name, List<Field> needMappingFields, List<Field> sourceFields) {
        for (Field needMappingField : needMappingFields) {
            Optional<MappingParameter> mappingFieldOptional = configureMappingParameter(needMappingField, sourceFields);
            mappingFieldOptional.ifPresent(mappingParameter -> MAPPING_MAP.put(name, mappingParameter));
        }
    }

    private static void mappingSignedField(String name, List<Field> needMappingFields, List<Field> sourceFields) {
        for (Field declaredField : needMappingFields) {
            Mapping[] mappings = declaredField.getAnnotationsByType(Mapping.class);
            Optional<MappingParameter> mappingFieldOptional = configureMappingParameter(declaredField, mappings[0], sourceFields);
            mappingFieldOptional.ifPresent(mappingParameter -> MAPPING_MAP.put(name, mappingParameter));
        }
    }

    private static Optional<MappingParameter> configureMappingParameter(Field targetField, List<Field> sourceFields) {
        String targetFieldName = targetField.getName();
        String sourceFieldName = targetFieldName;
        Optional<List<Field>> sourceFieldOptional = getSourceField(sourceFieldName, sourceFields);
        if(sourceFieldOptional.isPresent()) {
            MappingParameter mappingParameter = MappingParameter.builder().target(targetField).sources(sourceFieldOptional.get()).convertType(ConvertType.NONE).generatorType(GeneratorType.NONE).build();
            return Optional.of(mappingParameter);
        }
        throw new RuntimeException("原实体类不存在指定属性：" + sourceFieldName);
    }

    private static Optional<MappingParameter> configureMappingParameter(Field targetField, Mapping mapping, List<Field> sourceFields) {
        String targetFieldName = targetField.getName();
        GeneratorType generatorType = mapping.generator();
        if(!generatorType.equals(GeneratorType.NONE)) {
            MappingParameter mappingParameter = MappingParameter.builder().target(targetField).generatorType(generatorType).build();
            return Optional.of(mappingParameter);
        }
        ConvertType convertType = mapping.convert();
        String source = mapping.source();
        String sourceFieldName = MappingStructConstant.DEFAULT_FIELD_NAME.equals(source) ? targetFieldName : source;
        Optional<List<Field>> sourceFieldOptional = getSourceField(sourceFieldName, sourceFields);
        if(sourceFieldOptional.isPresent()) {
            MappingParameter mappingParameter = MappingParameter.builder().target(targetField).sources(sourceFieldOptional.get()).convertType(convertType).generatorType(generatorType).build();
            return Optional.of(mappingParameter);
        }
        throw new RuntimeException("原实体类不存在指定属性：" + sourceFieldName);
    }

    private static Optional<List<Field>> getSourceField(String sourceFieldName, List<Field> sourceFields) {
        if(sourceFieldName.contains(StringTools.POINT)) {
            Iterator<String> sourceFieldNameIterator = StringTools.splitWithSeparator(StringTools.POINT, sourceFieldName).iterator();
            List<Field> result = Lists.newArrayList();
            recursiveGetSourceField(sourceFieldNameIterator, sourceFields, result);
            return Optional.of(result);
        }
        List<Field> results = sourceFields.stream().filter(sourceField -> sourceField.getName().equals(sourceFieldName)).collect(Collectors.toList());
        return Optional.of(results);
    }

    private static void recursiveGetSourceField(Iterator<String> sourceFieldNameIterator, List<Field> sourceFields, List<Field> results) {
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

    private static void showMappings() {
//        String banner = FileTools.getBanner();
//        System.out.println(banner);
        LogTools.info("{0}成功启动！扫描解析结果如下：", MappingStructManager.PROJECT_NAME);
        MAPPING_MAP.asMap().forEach((key, mappingParameters) -> {
            System.out.println(key);
            for (MappingParameter mappingParameter : mappingParameters) {
                System.out.println(mappingParameter.getMappingParameterInfo());
            }
            LogTools.printDividingLine(85);
        });
    }

    public static void mapping(Object target, Object source) {
        if(ObjectTools.isAnyNull(target, source)) {
            return;
        }
        try {
            doMapping(target, source);
        }catch (Exception e) {
            String errorMag = StringTools.appendJoinEmpty(getErrorMessage(target.getClass(), source), FileTools.LF, Throwables.getStackTraceAsString(e));
            System.out.println(errorMag);
        }
    }

    public static <T> Optional<T> mapping(Class<T> targetClass, Object source) {
        if(ObjectTools.isAnyNull(targetClass, source)) {
            return Optional.empty();
        }
        try {
            T target = targetClass.newInstance();
            return doMapping(target, source);
        } catch (Exception e) {
            String errorMag = StringTools.appendJoinEmpty(getErrorMessage(targetClass, source), FileTools.LF, Throwables.getStackTraceAsString(e));
            System.out.println(errorMag);
            return Optional.empty();
        }
    }

    private static <T> Optional<T> doMapping(Object target, Object source) throws Exception {
        if(ObjectTools.isNull(target)) {
            return Optional.empty();
        }
        String targetClassName = target.getClass().getName();
        if(MAPPING_MAP.containsKey(targetClassName)) {
            Collection<MappingParameter> mappingParameters = MAPPING_MAP.get(targetClassName);
            for (MappingParameter mappingParameter : mappingParameters) {
                Field targetField = mappingParameter.getTarget();
                GeneratorType generatorType = mappingParameter.getGeneratorType();
                if(!generatorType.equals(GeneratorType.NONE)) {
                    Object generator = Generators.generate(generatorType);
                    setTargetFieldValue(target, targetField, generator);
                    continue;
                }
                Object sourceValue = getSourceFieldValue(source, mappingParameter);
                setTargetFieldValue(target, targetField, sourceValue);
            }
            return Optional.of((T)target);
        }
        return Optional.empty();
    }

    private static Object getSourceFieldValue(Object source, MappingParameter mappingParameter) throws IllegalAccessException {
        ConvertType convertType = mappingParameter.getConvertType();
        Iterator<Field> sourceFieldIterator = mappingParameter.getSources().iterator();
        Object sourceValue = recursiveGetSourceFieldValue(sourceFieldIterator, source);
        if(!convertType.equals(ConvertType.NONE)) {
            return Converts.convert(convertType, sourceValue);
        }
        return sourceValue;
    }

    private static Object recursiveGetSourceFieldValue(Iterator<Field> sourceFieldIterator, Object source) throws IllegalAccessException {
        if(ObjectTools.isNull(source)) {
            return null;
        }
        if(sourceFieldIterator.hasNext()) {
            Field sourceField = sourceFieldIterator.next();
            Object sourceValue;
            synchronized (sourceField) {
                sourceField.setAccessible(true);
                sourceValue = sourceField.get(source);
                sourceField.setAccessible(false);
            }
            if(!sourceFieldIterator.hasNext()) {
                return sourceValue;
            }
            return recursiveGetSourceFieldValue(sourceFieldIterator, sourceValue);
        }
        return null;
    }

    private synchronized static void setTargetFieldValue(Object target, Field targetField, Object sourceValue) throws IllegalAccessException {
        targetField.setAccessible(true);
        targetField.set(target, sourceValue);
        targetField.setAccessible(false);
    }

    private static <T> String getErrorMessage(Class<T> targetClass, Object source) {
        return StringTools.append(StringTools.SPACE, "实体映射失败！", "target class = ", targetClass.getName(), "source = ", String.valueOf(source));
    }
}