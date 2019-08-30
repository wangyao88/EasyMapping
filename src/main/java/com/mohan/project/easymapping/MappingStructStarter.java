package com.mohan.project.easymapping;


import com.mohan.project.easytools.common.ArrayTools;
import com.mohan.project.easytools.common.StringTools;

/**
 * 实体属性映射启动类
 * @author mohan
 * @date 2019-08-30 09:24:26
 */
public class MappingStructStarter {

    private static final String DEFAULT_BASE_PACKAGE = StringTools.EMPTY;

    private MappingStructStarter() {}

    private String[] basePackages;
    private boolean enableLog;

    public void start() throws ScanException {
        if(ArrayTools.isEmpty(basePackages)) {
            basePackages = new String[]{DEFAULT_BASE_PACKAGE};
        }
        MappingStructManager.init(basePackages, enableLog);
    }

    public static class MappingStructStarterBuilder {

        private String[] basePackages;
        private boolean enableLog;

        public static MappingStructStarterBuilder newBuilder() {
            return new MappingStructStarterBuilder();
        }

        public MappingStructStarterBuilder basePackages(String... basePackages) {
            this.basePackages = basePackages;
            return this;
        }

        public MappingStructStarterBuilder enableLog() {
            this.enableLog = true;
            return this;
        }

        public MappingStructStarter build() {
            MappingStructStarter mappingStructStarter = new MappingStructStarter();
            mappingStructStarter.basePackages = this.basePackages;
            mappingStructStarter.enableLog = this.enableLog;
            return mappingStructStarter;
        }
    }
}