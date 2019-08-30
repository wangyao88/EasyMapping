package com.mohan.project.easymapping;


import com.mohan.project.easymapping.convert.ConvertType;
import com.mohan.project.easymapping.generator.GeneratorType;
import com.mohan.project.easytools.common.CollectionTools;
import com.mohan.project.easytools.common.ObjectTools;
import com.mohan.project.easytools.common.StringTools;
import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 实体属性映射属性
 * @author mohan
 * @date 2019-08-23 13:36:23
 */
@Data
@Builder
public class MappingParameter {

    private Field target;
    private List<Field> sources;
    private ConvertType convertType;
    private GeneratorType generatorType;

    private static final String DEFAULT_NULL = "无";

    public String getMappingParameterInfo() {
        StringBuilder info = new StringBuilder();

        String targetName = ObjectTools.isNull(target) ? DEFAULT_NULL : target.getName();
        info.append("目标属性：").append(targetName).append(StringTools.TAB);

        String sourceNames = DEFAULT_NULL;
        if(CollectionTools.isNotEmpty(sources)) {
            List<String> sourceFieldNames = sources.stream().map(Field::getName).collect(Collectors.toList());
            sourceNames = StringTools.appendJoinComma(sourceFieldNames);
        }
        info.append("源属性：").append(sourceNames).append(StringTools.TAB);

        String convertTypeName = ObjectTools.isNull(convertType) ? DEFAULT_NULL : convertType.name();
        info.append("属性转换器：").append(convertTypeName).append(StringTools.TAB);

        String generatorTypeName = ObjectTools.isNull(generatorType) ? DEFAULT_NULL : generatorType.name();
        info.append("属性生成器：").append(generatorTypeName).append(StringTools.TAB);

        return info.toString();
    }
}