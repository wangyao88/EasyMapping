package com.mohan.project.easymapping.mapping.bytecode.javassist;


import com.google.common.collect.Lists;
import com.mohan.project.easymapping.EasyMappingConstant;
import com.mohan.project.easymapping.MappingParameter;
import com.mohan.project.easymapping.convert.ConvertType;
import com.mohan.project.easymapping.convert.Converts;
import com.mohan.project.easymapping.exception.MappingIndexOutOfBoundsException;
import com.mohan.project.easymapping.generator.Generator;
import com.mohan.project.easymapping.generator.GeneratorType;
import com.mohan.project.easymapping.generator.Generators;
import com.mohan.project.easytools.common.CollectionTools;
import com.mohan.project.easytools.common.ObjectTools;
import com.mohan.project.easytools.common.StringTools;
import com.mohan.project.easytools.log.LogTools;
import javassist.*;

import java.lang.reflect.Field;
import java.util.Collection;
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
        if (ObjectTools.isNull(setter)) {
            synchronized (lock) {
                setter = SETTER_MAP.get(joinedClassName);
                if (ObjectTools.isNull(setter)) {
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
            pool.importPackage(ConvertType.class.getName());
            pool.importPackage(Converts.class.getName());
            pool.importPackage(NormalByteCodeMapping.class.getName());

            CtClass abstractSetterClazz = pool.getCtClass(AbstractSetter.class.getName());
            final String setterImplClazzName = joinedClassName + "NormalSetter";
            CtClass setterClazz = pool.makeClass(setterImplClazzName, abstractSetterClazz);

            CtConstructor constructor = new CtConstructor(new CtClass[0], setterClazz);
            constructor.setBody("{}");
            setterClazz.addConstructor(constructor);

            configureMethodBody(target, sources, mappingParameters, setterClazz);

            Class<?> javaClazz = setterClazz.toClass();
            return (AbstractSetter) javaClazz.newInstance();
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
            if (Generator.isNotDefault(generator)) {
                if (mappingParameter.isNeedSourceField()) {
                    String sourceValueStr = getSourceFieldValueStr(i, sources, mappingParameters);
                    setMethodStr.append(sourceValueStr).append(StringTools.LINE_BREAK);
                } else {
                    setMethodStr.append("Object sourceValue").append(i).append(" = null;")
                            .append(StringTools.LINE_BREAK);
                }
                setMethodStr.append(StringTools.LINE_BREAK)
                        .append("Generator generator").append(i).append(" = mappingParameter").append(i).append(".getGenerator();")
                        .append(StringTools.LINE_BREAK)
                        .append("Object customerGenerateValue").append(i).append(" = generator").append(i).append(".doGenerate(sourceValue").append(i).append(");")
                        .append(StringTools.LINE_BREAK)
                        .append("realTarget.set")
                        .append(StringTools.toUpperCaseFirstOne(targetFieldName))
                        .append("((").append(type.getName()).append(")customerGenerateValue").append(i).append(");")
                        .append(StringTools.LINE_BREAK);
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

            String sourceValueStr = getSourceFieldValueStr(i, sources, mappingParameters);
            setMethodStr.append(sourceValueStr)
                    .append("realTarget.set")
                    .append(StringTools.toUpperCaseFirstOne(targetFieldName))
                    .append("((").append(type.getName()).append(")sourceValue").append(i).append(");")
                    .append(StringTools.LINE_BREAK);
        }

        setMethodStr.append("}");

        CtMethod cm = CtNewMethod.make(setMethodStr.toString(), setterClazz);
        setterClazz.addMethod(cm);
    }

    private String getSourceFieldValueStr(int mappingParametersIndex, List<Object> sources, List<MappingParameter> mappingParameters) {
        final StringBuilder getSourceFieldValueStr = new StringBuilder();
        String sourceValue = "sourceValue" + mappingParametersIndex;
        getSourceFieldValueStr.append("Object ")
                .append(sourceValue)
                .append(" = null;")
                .append(StringTools.LINE_BREAK);
        MappingParameter currentMappingParameter = mappingParameters.get(mappingParametersIndex);
        String mappingParameter = "mappingParameter" + mappingParametersIndex;
        String convertType = "convertType" + mappingParametersIndex;
        getSourceFieldValueStr.append("ConvertType ")
                .append(convertType)
                .append(" = ")
                .append(mappingParameter)
                .append(".getConvertType();")
                .append(StringTools.LINE_BREAK);

        List<Field> sourcesFieldList = currentMappingParameter.getSources();
        int index = currentMappingParameter.getIndex();
        int sourceSize = sources.size();
        if (index != EasyMappingConstant.DEFAULT_FIELD_INDEX) {
            if (index > sourceSize - 1 && !currentMappingParameter.isIgnoreException()) {
                String msg = configureMappingIndexOutOfBoundsException(sourceSize, currentMappingParameter);
                throw new MappingIndexOutOfBoundsException(msg);
            }
            Object source = sources.get(index);

            String sourceValueStr = doGetSourceFieldValue(sourceValue, sourcesFieldList, source, index);
            getSourceFieldValueStr.append(sourceValueStr).append(StringTools.LINE_BREAK);

            getSourceFieldValueStr.append("if (ConvertType.NONE != ").append(convertType).append(") {")
                    .append(StringTools.LINE_BREAK)
                    .append(sourceValue)
                    .append(" = Converts.convert(")
                    .append(convertType)
                    .append(", ")
                    .append(sourceValue)
                    .append(");")
                    .append(StringTools.LINE_BREAK)
                    .append("}")
                    .append(StringTools.LINE_BREAK);
            return getSourceFieldValueStr.toString();
        }


        getSourceFieldValueStr.append("String sourceClassName = ")
                .append(mappingParameter)
                .append(".getSourceClassName();")
                .append(StringTools.LINE_BREAK)
                .append("int sourceSize = sources.size();")
                .append(StringTools.LINE_BREAK)
                .append("for (int i = 0; i < sourceSize; i++) {")
                .append(StringTools.LINE_BREAK)
                .append("Object source = sources.get(i);")
                .append(StringTools.LINE_BREAK)
                .append("if (!source.getClass().getName().equals(sourceClassName)) {")
                .append(StringTools.LINE_BREAK)
                .append("continue;")
                .append(StringTools.LINE_BREAK)
                .append("}")
                .append(StringTools.LINE_BREAK);

        String sourceClassName = currentMappingParameter.getSourceClassName();
        for (int i = 0; i < sourceSize; i++) {
            Object source = sources.get(i);
            if (!source.getClass().getName().equals(sourceClassName)) {
                continue;
            }
            String sourceValueStr = doGetSourceFieldValue(sourceValue, sourcesFieldList, source, i);
            getSourceFieldValueStr.append(sourceValueStr).append(StringTools.LINE_BREAK);
            getSourceFieldValueStr.append("if (ConvertType.NONE != ").append(convertType).append(") {")
                    .append(StringTools.LINE_BREAK)
                    .append(sourceValue).append(" = Converts.convert(").append(convertType).append(", ").append(sourceValue).append(");")
                    .append(StringTools.LINE_BREAK)
                    .append("}")
                    .append(StringTools.LINE_BREAK)
                    .append(" if (").append(sourceValue).append(" != null) {")
                    .append(StringTools.LINE_BREAK)
                    .append(" break;")
                    .append(StringTools.LINE_BREAK)
                    .append("}")
                    .append(StringTools.LINE_BREAK)
                    .append("}")
                    .append(StringTools.LINE_BREAK);
        }
        return getSourceFieldValueStr.toString();
    }

    private static String doGetSourceFieldValue(String sourceValue, List<Field> sourcesFieldList, Object source, int index) {
        if (CollectionTools.isEmpty(sourcesFieldList)) {
            return StringTools.EMPTY;
        }
        String sourceClassName = source.getClass().getName();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("try {")
                .append(StringTools.LINE_BREAK)
                .append(sourceValue)
                .append(" = ")
                .append("((").append(sourceClassName).append(")sources.get(").append(index).append("))");

        for (Field field : sourcesFieldList) {
            String fieldName = field.getName();
            Class<?> fieldType = field.getType();
            stringBuilder.append(".get").append(StringTools.toUpperCaseFirstOne(fieldName)).append("()");
        }
        stringBuilder.append(";")
                .append(StringTools.LINE_BREAK)
                .append("}catch (Exception e) {")
                .append(StringTools.LINE_BREAK)
                .append(sourceValue)
                .append(" = null;")
                .append(StringTools.LINE_BREAK)
                .append("}");
        return stringBuilder.toString();
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
}
