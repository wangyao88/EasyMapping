package com.mohan.project.easymapping.parser;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 实体属性配置器工厂类
 *
 * @author mohan
 * @since 2019-08-23 13:36:23
 */
public class ConfigurationFactory {

    private static final Map<ConfigurationType, Configuration> CONFIGURATIONS = Maps.newEnumMap(ConfigurationType.class);

    public static void register(ConfigurationType type, Configuration configuration) {
        CONFIGURATIONS.put(type, configuration);
    }

    public static Configuration getConfiguration(ConfigurationType type) {
        return CONFIGURATIONS.get(type);
    }
}
