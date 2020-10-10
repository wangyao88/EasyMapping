package com.mohan.project.easymapping.parser;


import com.mohan.project.easymapping.MappingParameter;

import java.util.List;

/**
 * 实体属性配置器
 *
 * @author mohan
 * @since 2019-08-23 13:36:23
 */
public interface Configuration {

    List<MappingParameter> config(ParserParameter  parserParameter);

    ConfigurationType getType();
}
