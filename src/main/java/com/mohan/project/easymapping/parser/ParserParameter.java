package com.mohan.project.easymapping.parser;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 实体属性解析器参数
 * @author WangYao
 * @since 2019-08-23 13:36:23
 */
@Data
@Builder
public class ParserParameter {

    private String name;
    private List<Field> needMappingFields;
    private List<Field> sourceFields;
    private Class<?> source;
    private boolean ignoreMissing;
    private boolean ignoreException;
    private int sourceIndex;
}
