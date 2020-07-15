package com.mohan.project.easymapping;


import com.mohan.project.easymapping.convert.ConvertType;
import com.mohan.project.easymapping.generator.Generator;
import com.mohan.project.easymapping.generator.GeneratorType;
import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 实体属性映射属性
 * @author WangYao
 * @since 2019-08-23 13:36:23
 */
@Data
@Builder
public class MappingParameter {

    private Field target;
    private String sourceClassName;
    private List<Field> sources;
    private ConvertType convertType;
    private GeneratorType generatorType;
    private Generator generator;
    private boolean ignoreMissing;
    private boolean ignoreException;
    private boolean needSourceField;
    private int index;
}