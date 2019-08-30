package com.mohan.project.easymapping;


import com.mohan.project.easytools.common.ArrayTools;

/**
 * 实体属性映射启动类
 * @author mohan
 * @date 2019-08-30 09:24:26
 */
public class MappingStructStarter {

    private MappingStructStarter() {}

    private String[] basePackages;

    public void start() throws NoBasePackageException, ScanException {
        if(ArrayTools.isEmpty(basePackages)) {
            throw new NoBasePackageException();
        }
        MappingStructManager.init(basePackages);
    }

    public static class MappingStructStarterBuilder {

        private String[] basePackages;

        public static MappingStructStarterBuilder newBuilder() {
            return new MappingStructStarterBuilder();
        }

        public MappingStructStarterBuilder basePackages(String... basePackages) {
            this.basePackages = basePackages;
            return this;
        }

        public MappingStructStarter build() {
            MappingStructStarter mappingStructStarter = new MappingStructStarter();
            mappingStructStarter.basePackages = this.basePackages;
            return mappingStructStarter;
        }
    }
}