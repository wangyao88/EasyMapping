package com.mohan.project.easymapping.convert;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.function.Function;

/**
 * 实体属性值转换器
 *
 * @author mohan
 * @since 2019-08-23 13:36:23
 */
public class Converts {

    private static final Map<ConvertType, Function<Object, Object>> CONVERT_TYPE_FUNCTION_MAP = Maps.newEnumMap(ConvertType.class);

    static {
        CONVERT_TYPE_FUNCTION_MAP.put(ConvertType.NONE, (obj) -> null);
        CONVERT_TYPE_FUNCTION_MAP.put(ConvertType.String, String::valueOf);
    }

    public static Object convert(ConvertType convertType, Object sourceValue) {
        Function<Object, Object> function = CONVERT_TYPE_FUNCTION_MAP.getOrDefault(convertType, (obj) -> null);
        return function.apply(sourceValue);
    }
}