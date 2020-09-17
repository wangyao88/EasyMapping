package com.mohan.project.easymapping.parser;

import com.google.common.collect.Lists;
import com.mohan.project.easymapping.CustomerGenerator;
import com.mohan.project.easymapping.MappingParameter;
import com.mohan.project.easymapping.MappingStructConstant;
import com.mohan.project.easymapping.convert.ConvertType;
import com.mohan.project.easymapping.exception.AttributeNotExistException;
import com.mohan.project.easymapping.exception.InitializeCustomerGeneratorException;
import com.mohan.project.easymapping.generator.Generator;
import com.mohan.project.easymapping.generator.GeneratorType;
import com.mohan.project.easytools.common.CollectionTools;
import com.mohan.project.easytools.common.ObjectTools;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

/**
 * 实体属性配置器
 *
 * @author WangYao
 * @since 2019-08-23 13:36:23
 */
@Config
public class ConfigureAll extends BaseConfiguration implements Configuration {

    @Override
    public List<MappingParameter> config(ParserParameter parserParameter) {
        List<MappingParameter> mappingParameters = Lists.newArrayList();
        for (Field needMappingField : parserParameter.getNeedMappingFields()) {
            Optional<MappingParameter> mappingFieldOptional = configureMappingParameter(needMappingField, parserParameter);
            storeConfig(mappingParameters, mappingFieldOptional);
        }
        return mappingParameters;
    }

    private Optional<MappingParameter> configureMappingParameter(Field needMappingField, ParserParameter parserParameter) {
        String sourceFieldName = needMappingField.getName();
        MappingParameter mappingParameter =
                MappingParameter.builder()
                        .target(needMappingField)
                        .convertType(ConvertType.NONE)
                        .generatorType(GeneratorType.NONE)
                        .generator(Generator.getDefault())
                        .ignoreMissing(parserParameter.isIgnoreMissing())
                        .ignoreException(parserParameter.isIgnoreException())
                        .index(MappingStructConstant.DEFAULT_FIELD_INDEX)
                        .build();
        CustomerGenerator customerGenerator = needMappingField.getAnnotation(CustomerGenerator.class);
        if(ObjectTools.isNotNull(customerGenerator)) {
            try {
                boolean needSourceField = customerGenerator.needSourceField();
                Generator generator = customerGenerator.customerGenerator().newInstance();
                mappingParameter.setGenerator(generator);
                mappingParameter.setNeedSourceField(needSourceField);
                if(needSourceField) {
                    configureSource(parserParameter, mappingParameter, sourceFieldName);
                }
                return Optional.of(mappingParameter);
            } catch (Exception e) {
                throw new InitializeCustomerGeneratorException(e);
            }
        }
        boolean configureSource = configureSource(parserParameter, mappingParameter, sourceFieldName);
        if(configureSource) {
            return Optional.of(mappingParameter);
        }
        if (parserParameter.isIgnoreMissing()) {
            return Optional.empty();
        }
        throw new AttributeNotExistException(sourceFieldName);
    }

    private boolean configureSource(ParserParameter parserParameter,  MappingParameter mappingParameter, String sourceFieldName) {
        Optional<List<Field>> sourceFieldOptional = getSourceField(sourceFieldName, parserParameter.getSourceFields());
        if (sourceFieldOptional.isPresent() && CollectionTools.isNotEmpty(sourceFieldOptional.get())) {
            mappingParameter.setSources(sourceFieldOptional.get());
            mappingParameter.setSourceClassName(parserParameter.getSource().getName());
            return true;
        }
        return false;
    }

    @Override
    public ConfigurationType getType() {
        return ConfigurationType.ALL;
    }
}
