package com.mohan.project.easymapping.mapping.bytecode.javassist;

import com.mohan.project.easytools.common.ObjectTools;
import com.mohan.project.easytools.common.StringTools;
import com.mohan.project.easytools.log.LogTools;
import javassist.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 实体属性映射器
 *
 * @author WangYao
 * @since 2019-08-23 13:36:23
 */
public final class SmartByteCodeMapping extends BaseByteCodeMapping {

    private final Object lock = new Object();

    private SmartByteCodeMapping() {
    }

    private static class Single {
        private static final SmartByteCodeMapping SMART_BYTE_CODE_MAPPING = new SmartByteCodeMapping();
    }

    public static SmartByteCodeMapping getInstance() {
        return Single.SMART_BYTE_CODE_MAPPING;
    }

    public <T> Optional<T> mapping(Object target, List<Object> sources) {
        getSetter(target, sources).doSet(target, sources);
        return Optional.of((T) target);
    }

    private AbstractSetter getSetter(Object target, List<Object> sources) {
        String joinedClassName = getJoinedClassName(target, sources);
        AbstractSetter setter = SETTER_MAP.get(joinedClassName);
        if(ObjectTools.isNull(setter)) {
            synchronized (lock) {
                setter = SETTER_MAP.get(joinedClassName);
                if(ObjectTools.isNull(setter)) {
                    setter = createSetter(joinedClassName, target, sources);
                    putSetter(joinedClassName, setter);
                }
            }
        }
        return setter;
    }

    private AbstractSetter createSetter(String joinedClassName, Object target, List<Object> sources) {
        try {
            ClassPool pool = ClassPool.getDefault();
            pool.appendSystemPath();

            pool.importPackage(List.class.getName());
            pool.importPackage(target.getClass().getName());
            for (Object source : sources) {
                pool.importPackage(source.getClass().getName());
            }

            CtClass abstractSetterClazz = pool.getCtClass(AbstractSetter.class.getName());
            final String setterImplClazzName = joinedClassName + "SmartSetter";
            CtClass setterClazz = pool.makeClass(setterImplClazzName, abstractSetterClazz);

            CtConstructor constructor = new CtConstructor(new CtClass[0], setterClazz);
            constructor.setBody("{}");
            setterClazz.addConstructor(constructor);

            configureMethodBody(target, sources, setterClazz);

            Class<?> javaClazz = setterClazz.toClass();
            return  (AbstractSetter) javaClazz.newInstance();
        } catch (Exception e) {
            LogTools.error("生成Setter字节码失败！", e);
            return null;
        }
    }

    private void configureMethodBody(Object target, List<Object> sources, CtClass setterClazz) throws CannotCompileException {
        final StringBuilder setMethodStr = new StringBuilder();
        setMethodStr.append("public void doSet(Object target, List sources) {").append(StringTools.LINE_BREAK);

        String targetClassName = target.getClass().getName();
        setMethodStr.append(targetClassName)
                .append(" realTarget = (")
                .append(targetClassName)
                .append(")target;")
                .append(StringTools.LINE_BREAK);
        List<Field> targetFields = getFieldNames(target);
        int size = sources.size();
        for (Field targetField : targetFields) {
            String targetFieldName = targetField.getName();
            for (int i = 0; i < size; i++) {
                Object currentSource = sources.get(i);
                List<Field> sourceFields = getFieldNames(currentSource);
                Optional<Field> fieldOptional = sourceFields.stream().filter(field -> field.getName().equals(targetFieldName) && field.getType() == targetField.getType()).findFirst();
                if(fieldOptional.isPresent()) {
                    String currentSourceClassName = currentSource.getClass().getName();
                    setMethodStr.append("try{")
                            .append(StringTools.LINE_BREAK)
                            .append("realTarget.set")
                            .append(StringTools.toUpperCaseFirstOne(targetFieldName))
                            .append("(")
                            .append("((").append(currentSourceClassName).append(")sources.get(").append(i).append("))")
                            .append(".get").append(StringTools.toUpperCaseFirstOne(targetFieldName))
                            .append("()")
                            .append(");")
                            .append(StringTools.LINE_BREAK)
                            .append("}catch (Exception e) {")
                            .append(StringTools.LINE_BREAK)
                            .append("}")
                            .append(StringTools.LINE_BREAK);
                    break;
                }
            }
        }

        setMethodStr.append("}");

        CtMethod cm = CtNewMethod.make(setMethodStr.toString(), setterClazz);
        setterClazz.addMethod(cm);
    }
}
