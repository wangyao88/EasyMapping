package com.mohan.project.easymapping.mapping;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 实体属性映射器
 *
 * @author WangYao
 * @since 2019-08-23 13:36:23
 */
public final class SmartMapping {

    private final Object lock = new Object();

    private SmartMapping() {
    }

    private static class Single {
        private static final SmartMapping SMART_MAPPING = new SmartMapping();
    }

    public static SmartMapping getInstance() {
        return Single.SMART_MAPPING;
    }

    public <T> Optional<T> mapping(Object target, List<Object> sources) {
        Field[] declaredFields = target.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            setTargetFieldValueFromSource(target, declaredField, sources);
        }
        return Optional.of((T) target);
    }

    private void setTargetFieldValueFromSource(Object target, Field declaredField, List<Object> sources) {
        try {
            String targetFieldName = declaredField.getName();
            for (Object source : sources) {
                Optional<Field> fieldOptional = Arrays.stream(source.getClass().getDeclaredFields()).filter(field -> targetFieldName.equals(field.getName())).findFirst();
                if (fieldOptional.isPresent()) {
                    synchronized (lock) {
                        Field field = fieldOptional.get();
                        field.setAccessible(true);
                        Object sourceValue = field.get(source);
                        declaredField.setAccessible(true);
                        declaredField.set(target, sourceValue);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            // ignore
        }
    }
}
