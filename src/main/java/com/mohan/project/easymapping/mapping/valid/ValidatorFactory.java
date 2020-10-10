package com.mohan.project.easymapping.mapping.valid;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author WangYao
 * @since 2020-10-10 17:06
 */
public class ValidatorFactory {

    private static final Map<ValidatorType, Validator> VALIDATOR_ENUM_MAP = Maps.newEnumMap(ValidatorType.class);

    public static void register(ValidatorType type, Validator validator) {
        VALIDATOR_ENUM_MAP.put(type, validator);
    }

    public static Validator getValidator(ValidatorType type) {
        return VALIDATOR_ENUM_MAP.get(type);
    }

}