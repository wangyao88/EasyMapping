package com.mohan.project.easymapping.generator;


import com.mohan.project.easytools.common.UUIDTools;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 实体属性默认值生成策略
 * @author wangyao
 * @date 2019-08-23 13:36:23
 */
public class Generators {

    private static final Map<GeneratorType, Supplier> GENERATORS = new HashMap<>();

    static {
        GENERATORS.put(GeneratorType.NONE, () -> null);
        GENERATORS.put(GeneratorType.UUID, UUIDTools::getUUID);
        GENERATORS.put(GeneratorType.NOW, Date::new);
    }

    public static Object generate(GeneratorType generatorType) {
        Supplier supplier = GENERATORS.getOrDefault(generatorType, () -> null);
        return supplier.get();
    }
}
