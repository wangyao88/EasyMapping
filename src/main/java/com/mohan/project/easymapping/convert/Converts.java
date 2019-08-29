package com.mohan.project.easymapping.convert;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 实体属性值转换器
 * @author wangyao
 * @date 2019-08-23 13:36:23
 */
public class Converts {

    private static final Map<ConvertType, Function> CONVERTS = new HashMap<>();

    static {
        CONVERTS.put(ConvertType.NONE, (obj) -> null);
        CONVERTS.put(ConvertType.String, String::valueOf);
    }

    public static Object convert(ConvertType convertType, Object sourceValue) {
        Function function = CONVERTS.getOrDefault(convertType, (obj) -> null);
        return function.apply(sourceValue);
    }
}
