package com.mohan.project.easymapping.parser;


import java.lang.reflect.Field;
import java.util.List;

/**
 * 实体属性解析器参数
 *
 * @author mohan
 * @since 2019-08-23 13:36:23
 */
public class ParserParameter {

    private String name;
    private List<Field> needMappingFields;
    private List<Field> sourceFields;
    private Class<?> source;
    private boolean ignoreMissing;
    private boolean ignoreException;
    private int sourceIndex;

    public ParserParameter() {
    }

    public ParserParameter(ParserParameterBuilder parserParameterBuilder) {
        this.name = parserParameterBuilder.name;
        this.needMappingFields = parserParameterBuilder.needMappingFields;
        this.sourceFields = parserParameterBuilder.sourceFields;
        this.source = parserParameterBuilder.source;
        this.ignoreMissing = parserParameterBuilder.ignoreMissing;
        this.ignoreException = parserParameterBuilder.ignoreException;
        this.sourceIndex = parserParameterBuilder.sourceIndex;
    }

    public static ParserParameterBuilder builder() {
        return new ParserParameterBuilder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Field> getNeedMappingFields() {
        return needMappingFields;
    }

    public void setNeedMappingFields(List<Field> needMappingFields) {
        this.needMappingFields = needMappingFields;
    }

    public List<Field> getSourceFields() {
        return sourceFields;
    }

    public void setSourceFields(List<Field> sourceFields) {
        this.sourceFields = sourceFields;
    }

    public Class<?> getSource() {
        return source;
    }

    public void setSource(Class<?> source) {
        this.source = source;
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

    public int getSourceIndex() {
        return sourceIndex;
    }

    public void setSourceIndex(int sourceIndex) {
        this.sourceIndex = sourceIndex;
    }

    public static class ParserParameterBuilder {

        private String name;
        private List<Field> needMappingFields;
        private List<Field> sourceFields;
        private Class<?> source;
        private boolean ignoreMissing;
        private boolean ignoreException;
        private int sourceIndex;

        public ParserParameterBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ParserParameterBuilder needMappingFields(List<Field> needMappingFields) {
            this.needMappingFields = needMappingFields;
            return this;
        }

        public ParserParameterBuilder sourceFields(List<Field> sourceFields) {
            this.sourceFields = sourceFields;
            return this;
        }

        public ParserParameterBuilder source(Class<?> source) {
            this.source = source;
            return this;
        }

        public ParserParameterBuilder ignoreMissing(boolean ignoreMissing) {
            this.ignoreMissing = ignoreMissing;
            return this;
        }

        public ParserParameterBuilder ignoreException(boolean ignoreException) {
            this.ignoreException = ignoreException;
            return this;
        }

        public ParserParameterBuilder sourceIndex(int sourceIndex) {
            this.sourceIndex = sourceIndex;
            return this;
        }

        public ParserParameter build() {
            return new ParserParameter(this);
        }
    }
}
