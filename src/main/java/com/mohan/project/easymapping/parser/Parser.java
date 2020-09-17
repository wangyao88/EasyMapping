package com.mohan.project.easymapping.parser;


import com.mohan.project.easymapping.MappingParameter;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 实体属性解析器
 * @author WangYao
 * @since 2019-08-23 13:36:23
 */
public interface Parser {

    void doParse(String basePackage);

    Map<String, Collection<MappingParameter>> getParsedMappingInfo();

    Map<String, Collection<String>> getTargetSourcesInfo();

    List<String> getErrorMessage();

    void showMappingInfo();
}
