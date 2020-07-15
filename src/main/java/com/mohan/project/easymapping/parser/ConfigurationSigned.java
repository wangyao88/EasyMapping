package com.mohan.project.easymapping.parser;

import com.google.common.collect.Lists;
import com.mohan.project.easymapping.CustomerGenerator;
import com.mohan.project.easymapping.Mapping;
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
public class ConfigurationSigned extends BaseConfiguration implements Configuration {

    @Override
    public List<MappingParameter> config(ParserParameter parserParameter) {
        List<MappingParameter> mappingParameters = Lists.newArrayList();
        for (Field declaredField : parserParameter.getNeedMappingFields()) {
            Optional<MappingParameter> mappingFieldOptional = configureMappingParameter(declaredField, parserParameter);
            storeConfig(mappingParameters, mappingFieldOptional);
        }
        return mappingParameters;
    }

    private Optional<MappingParameter> configureMappingParameter(Field targetField, ParserParameter parserParameter) {
        String targetFieldName = targetField.getName();
        Mapping mapping = targetField.getAnnotation(Mapping.class);
        MappingParameter mappingParameter =
                MappingParameter.builder()
                        .target(targetField)
                        .convertType(ConvertType.NONE)
                        .generatorType(GeneratorType.NONE)
                        .generator(Generator.getDefault())
                        .ignoreMissing(parserParameter.isIgnoreMissing())
                        .ignoreException(parserParameter.isIgnoreException())
                        .index(mapping.index())
                        .build();
        CustomerGenerator customerGenerator = targetField.getAnnotation(CustomerGenerator.class);
        if(ObjectTools.isNotNull(customerGenerator)) {
            try {
                Generator generator = customerGenerator.customerGenerator().newInstance();
                mappingParameter.setGenerator(generator);
                mappingParameter.setNeedSourceField(customerGenerator.needSourceField());
                return Optional.of(mappingParameter);
            } catch (Exception e) {
                throw new InitializeCustomerGeneratorException(e);
            }
        }
        GeneratorType generatorType = mapping.generator();
        if(GeneratorType.NONE != generatorType) {
            mappingParameter.setGeneratorType(generatorType);
            return Optional.of(mappingParameter);
        }
        int index = mapping.index();
        if(index != MappingStructConstant.DEFAULT_FIELD_INDEX && index != parserParameter.getSourceIndex()) {
            return Optional.empty();
        }
        String source = mapping.source();
        String sourceFieldName = MappingStructConstant.DEFAULT_FIELD_NAME.equals(source) ? targetFieldName : source;
        Optional<List<Field>> sourceFieldOptional = getSourceField(sourceFieldName, parserParameter.getSourceFields());
        if(sourceFieldOptional.isPresent() && CollectionTools.isNotEmpty(sourceFieldOptional.get())) {
            mappingParameter.setSources(sourceFieldOptional.get());
            mappingParameter.setSourceClassName(parserParameter.getSource().getName());
            mappingParameter.setConvertType(mapping.convert());
            return Optional.of(mappingParameter);
        }
        if (parserParameter.isIgnoreMissing()) {
            return Optional.empty();
        }
        throw new AttributeNotExistException(sourceFieldName);
    }

    @Override
    public ConfigurationType getType() {
        return ConfigurationType.SIGNED;
    }
}
