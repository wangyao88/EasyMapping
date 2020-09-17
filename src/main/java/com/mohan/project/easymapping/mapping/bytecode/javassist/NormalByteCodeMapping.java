package com.mohan.project.easymapping.mapping.bytecode.javassist;


import com.google.common.collect.Lists;
import com.mohan.project.easymapping.EasyMappingConstant;
import com.mohan.project.easymapping.MappingParameter;
import com.mohan.project.easymapping.convert.ConvertType;
import com.mohan.project.easymapping.convert.Converts;
import com.mohan.project.easymapping.exception.ConfigureTargetFieldFromSourceValueException;
import com.mohan.project.easymapping.exception.MappingIndexOutOfBoundsException;
import com.mohan.project.easymapping.exception.ObtainSourceFieldValueException;
import com.mohan.project.easymapping.generator.Generator;
import com.mohan.project.easymapping.generator.GeneratorType;
import com.mohan.project.easymapping.generator.Generators;
import com.mohan.project.easytools.common.ObjectTools;
import com.mohan.project.easytools.common.StringTools;
import com.mohan.project.easytools.log.LogTools;
import javassist.*;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * 实体属性映射器
 *
 * @author WangYao
 * @since 2019-08-23 13:36:23
 */
public final class NormalByteCodeMapping extends BaseByteCodeMapping {

    private final Object lock = new Object();

    private NormalByteCodeMapping() {
    }

    private static class Single {
        private static final NormalByteCodeMapping NORMAL_BYTE_CODE_MAPPING = new NormalByteCodeMapping();
    }

    public static NormalByteCodeMapping getInstance() {
        return Single.NORMAL_BYTE_CODE_MAPPING;
    }

    public <T> Optional<T> mapping(Object target, List<Object> sources, Collection<MappingParameter> mappingParameters) {
        List<MappingParameter> mappingParameterList = Lists.newArrayList(mappingParameters);
        getSetter(target, sources, mappingParameterList).doSet(target, sources, mappingParameterList);
        return Optional.of((T) target);
    }

    private AbstractSetter getSetter(Object target, List<Object> sources, List<MappingParameter> mappingParameters) {
        String joinedClassName = getJoinedClassName(target, sources);
        AbstractSetter setter = SETTER_MAP.get(joinedClassName);
        if(ObjectTools.isNull(setter)) {
            synchronized (lock) {
                setter = SETTER_MAP.get(joinedClassName);
                if(ObjectTools.isNull(setter)) {
                    setter = createSetter(joinedClassName, target, sources, mappingParameters);
                    putSetter(joinedClassName, setter);
                }
            }
        }
        return setter;
    }

    private AbstractSetter createSetter(String joinedClassName, Object target, List<Object> sources, List<MappingParameter> mappingParameters) {
        try {
            ClassPool pool = ClassPool.getDefault();
            pool.appendSystemPath();

            pool.importPackage(List.class.getName());
            pool.importPackage(target.getClass().getName());
            for (Object source : sources) {
                pool.importPackage(source.getClass().getName());
            }
            pool.importPackage(Collection.class.getName());
            pool.importPackage(MappingParameter.class.getName());
            pool.importPackage(Generators.class.getName());
            pool.importPackage(GeneratorType.class.getName());
            pool.importPackage(Generator.class.getName());

            CtClass abstractSetterClazz = pool.getCtClass(AbstractSetter.class.getName());
            final String setterImplClazzName = joinedClassName + "Setter";
            CtClass setterClazz = pool.makeClass(setterImplClazzName, abstractSetterClazz);

            CtConstructor constructor = new CtConstructor(new CtClass[0], setterClazz);
            constructor.setBody("{}");
            setterClazz.addConstructor(constructor);

            configureMethodBody(target, sources, mappingParameters, setterClazz);

            setterClazz.writeFile();

            Class<?> javaClazz = setterClazz.toClass();
            return  (AbstractSetter) javaClazz.newInstance();
        } catch (Exception e) {
            LogTools.error("生成Setter字节码失败！", e);
            return null;
        }
    }

    private void configureMethodBody(Object target, List<Object> sources, List<MappingParameter> mappingParameters, CtClass setterClazz) throws CannotCompileException {
        final StringBuilder setMethodStr = new StringBuilder();
        setMethodStr.append("public void doSet(Object target, List sources, List mappingParameters) {").append(StringTools.LINE_BREAK);

        String targetClassName = target.getClass().getName();
        setMethodStr.append(targetClassName)
                .append(" realTarget = (")
                .append(targetClassName)
                .append(")target;\n");

        String mappingParameterClassName = MappingParameter.class.getName();
        int size = mappingParameters.size();
        for (int i = 0; i < size; i++) {
            MappingParameter mappingParameter = mappingParameters.get(i);
            Field targetField = mappingParameter.getTarget();
            String targetFieldName = targetField.getName();
            Class<?> type = targetField.getType();

            setMethodStr.append("MappingParameter mappingParameter").append(i).append(" = (")
                    .append(mappingParameterClassName)
                    .append(")mappingParameters.get(")
                    .append(i).append(");")
                    .append(StringTools.LINE_BREAK);

            Generator generator = mappingParameter.getGenerator();
//            Object sourceValue = null;
            if (Generator.isNotDefault(generator)) {
                if (mappingParameter.isNeedSourceField()) {
//                    sourceValue = getSourceFieldValue(sources, mappingParameter);
                    setMethodStr.append("Object sourceValue").append(i).append(" = \"wy\";")
                            .append(StringTools.LINE_BREAK)
                            .append("Generator generator").append(i).append(" = mappingParameter").append(i).append(".getGenerator();")
                            .append(StringTools.LINE_BREAK)
                            .append("Object customerGenerateValue").append(i).append(" = generator").append(i).append(".doGenerate(sourceValue").append(i).append(");")
                            .append(StringTools.LINE_BREAK)
                            .append("realTarget.set")
                            .append(StringTools.toUpperCaseFirstOne(targetFieldName))
                            .append("((").append(type.getName()).append(")customerGenerateValue").append(i).append(");")
                            .append(StringTools.LINE_BREAK);
                }
                continue;
            }

            GeneratorType generatorType = mappingParameter.getGeneratorType();
            if (GeneratorType.NONE != generatorType) {
                setMethodStr.append("GeneratorType generatorType").append(i).append(" = mappingParameter").append(i).append(".getGeneratorType();")
                        .append(StringTools.LINE_BREAK)
                        .append("Object generateValue").append(i).append(" = Generators.generator(generatorType").append(i).append(");")
                        .append(StringTools.LINE_BREAK)
                        .append("realTarget.set")
                        .append(StringTools.toUpperCaseFirstOne(targetFieldName))
                        .append("((").append(type.getName()).append(")generateValue").append(i).append(");")
                        .append(StringTools.LINE_BREAK);
                continue;
            }

//            sourceValue = getSourceFieldValue(sources, mappingParameter);
//            if (ObjectTools.isNotNull(sourceValue)) {
//                setTargetFieldValue(target, targetField, sourceValue, mappingParameter);
//            }
        }

        setMethodStr.append("}");

        System.out.println(setMethodStr.toString());

        CtMethod cm = CtNewMethod.make(setMethodStr.toString(), setterClazz);
        setterClazz.addMethod(cm);
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
