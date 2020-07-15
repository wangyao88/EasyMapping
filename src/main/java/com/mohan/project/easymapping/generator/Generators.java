package com.mohan.project.easymapping.generator;

import com.google.common.collect.Maps;
import com.mohan.project.easytools.common.UUIDTools;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 实体属性默认值生成策略
 * @author WangYao
 * @since 2019-08-23 13:36:23
 */
public class Generators {

    private static final Map<GeneratorType, Supplier<Object>> GENERATOR_TYPE_SUPPLIER_MAP = Maps.newEnumMap(GeneratorType.class);

    static {
        GENERATOR_TYPE_SUPPLIER_MAP.put(GeneratorType.NONE, () -> null);
        GENERATOR_TYPE_SUPPLIER_MAP.put(GeneratorType.UUID, UUIDTools::getUUID32);
        GENERATOR_TYPE_SUPPLIER_MAP.put(GeneratorType.NOW_DATE, Date::new);
        GENERATOR_TYPE_SUPPLIER_MAP.put(GeneratorType.NOW_LOCAL_DATE, LocalDate::now);
        GENERATOR_TYPE_SUPPLIER_MAP.put(GeneratorType.NOW_LOCAL_DATE_TIME, LocalDateTime::now);
    }

    public static Object generator(GeneratorType generatorType) {
        Supplier<Object> supplier = GENERATOR_TYPE_SUPPLIER_MAP.getOrDefault(generatorType, () -> null);
        return supplier.get();
    }
}