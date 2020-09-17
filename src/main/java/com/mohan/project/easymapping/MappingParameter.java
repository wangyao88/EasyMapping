package com.mohan.project.easymapping;


import com.mohan.project.easymapping.convert.ConvertType;
import com.mohan.project.easymapping.generator.Generator;
import com.mohan.project.easymapping.generator.GeneratorType;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 实体属性映射属性
 * @author WangYao
 * @since 2019-08-23 13:36:23
 */
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

    public MappingParameter() {
    }

    public MappingParameter(MappingParameterBuilder mappingParameterBuilder) {
        this.target = mappingParameterBuilder.target;
        this.sourceClassName = mappingParameterBuilder.sourceClassName;
        this.sources = mappingParameterBuilder.sources;
        this.convertType = mappingParameterBuilder.convertType;
        this.generatorType = mappingParameterBuilder.generatorType;
        this.generator = mappingParameterBuilder.generator;
        this.ignoreMissing = mappingParameterBuilder.ignoreMissing;
        this.ignoreException = mappingParameterBuilder.ignoreException;
        this.needSourceField = mappingParameterBuilder.needSourceField;
        this.index = mappingParameterBuilder.index;
    }

    public Field getTarget() {
        return target;
    }

    public void setTarget(Field target) {
        this.target = target;
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public List<Field> getSources() {
        return sources;
    }

    public void setSources(List<Field> sources) {
        this.sources = sources;
    }

    public ConvertType getConvertType() {
        return convertType;
    }

    public void setConvertType(ConvertType convertType) {
        this.convertType = convertType;
    }

    public GeneratorType getGeneratorType() {
        return generatorType;
    }

    public void setGeneratorType(GeneratorType generatorType) {
        this.generatorType = generatorType;
    }

    public Generator getGenerator() {
        return generator;
    }

    public void setGenerator(Generator generator) {
        this.generator = generator;
    }

    public boolean isIgnoreMissing() {
        return ignoreMissing;
    }

    public void setIgnoreMissing(boolean ignoreMissing) {
        this.ignoreMissing = ignoreMissing;
    }

    public boolean isIgnoreException() {
        return ignoreException;
    }

    public void setIgnoreException(boolean ignoreException) {
        this.ignoreException = ignoreException;
    }

    public boolean isNeedSourceField() {
        return needSourceField;
    }

    public void setNeedSourceField(boolean needSourceField) {
        this.needSourceField = needSourceField;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public static MappingParameterBuilder builder() {
        return new MappingParameterBuilder();
    }

    public static class MappingParameterBuilder {

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

        public MappingParameterBuilder target(Field target) {
            this.target = target;
            return this;
        }

        public MappingParameterBuilder sourceClassName(String sourceClassName) {
            this.sourceClassName = sourceClassName;
            return this;
        }

        public MappingParameterBuilder sources(List<Field> sources) {
            this.sources = sources;
            return this;
        }

        public MappingParameterBuilder convertType(ConvertType convertType) {
            this.convertType = convertType;
            return this;
        }

        public MappingParameterBuilder generatorType(GeneratorType generatorType) {
            this.generatorType = generatorType;
            return this;
        }

        public MappingParameterBuilder generator(Generator generator) {
            this.generator = generator;
            return this;
        }

        public MappingParameterBuilder ignoreMissing(boolean ignoreMissing) {
            this.ignoreMissing = ignoreMissing;
            return this;
        }

        public MappingParameterBuilder ignoreException(boolean ignoreException) {
            this.ignoreException = ignoreException;
            return this;
        }

        public MappingParameterBuilder needSourceField(boolean needSourceField) {
            this.needSourceField = needSourceField;
            return this;
        }

        public MappingParameterBuilder index(int index) {
            this.index = index;
            return this;
        }

        public MappingParameter build() {
            return new MappingParameter(this);
        }
    }
}