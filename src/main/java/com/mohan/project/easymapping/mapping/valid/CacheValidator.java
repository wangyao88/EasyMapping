package com.mohan.project.easymapping.mapping.valid;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.mohan.project.easymapping.parser.BaseParser;
import com.mohan.project.easytools.common.CollectionTools;
import com.mohan.project.easytools.common.ObjectTools;
import com.mohan.project.easytools.common.StringTools;
import com.mohan.project.easytools.log.LogTools;
import com.mohan.project.strategyfactory.core.ThreeArgStrategy;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 具备缓存校验结果功能的校验器
 *
 * @author mohan
 * @since 2020-10-10 17:04
 */
public class CacheValidator implements ThreeArgStrategy<Boolean, Boolean, Object, List> {

    private static final Cache<ValidKey, ValidResult> CACHE =
            CacheBuilder.newBuilder()
                    .expireAfterAccess(1, TimeUnit.HOURS)
                    .build();

    @Override
    public Boolean handle(Boolean useSmartMode, Object target, List sources) {
        ValidKey validKey = ValidKey.build(useSmartMode, target, sources);
        try {
            ValidResult validResult = CACHE.get(validKey, () -> doValid(useSmartMode, target, sources));
            if(!validResult.result) {
                LogTools.error(validResult.errorMessage);
            }
            return validResult.result;
        } catch (ExecutionException e) {
            LogTools.error("CacheValidator校验失败！", e);
            return false;
        }
    }

    private ValidResult doValid(boolean useSmartMode, Object target, List<Object> sources) {
        List<String> errorMessage = BaseParser.getInstance().getErrorMessage();
        if (CollectionTools.isNotEmpty(errorMessage)) {
            return new ValidResult(false, String.join(StringTools.LINE_BREAK, errorMessage));
        }
        if (ObjectTools.isNull(target) || CollectionTools.isEmpty(sources)) {
            return new ValidResult(false, "参数值为空！");
        }
        if (useSmartMode) {
            return new ValidResult(true);
        }
        String targetName = target.getClass().getName();
        Map<String, Collection<Class<?>>> targetSourcesInfoMap = BaseParser.getInstance().getTargetSourcesInfo();
        Collection<Class<?>> sourcesClass = targetSourcesInfoMap.get(targetName);
        if (CollectionTools.isEmpty(sourcesClass)) {
            return new ValidResult(false, StringTools.appendJoinEmpty("未找到[", targetName, "]相关@MappingStruct的配置信息！"));

        }
        for (Object source : sources) {
            if (!sourcesClass.contains(source.getClass())) {
                boolean flag = false;
                for (Class<?> sourceClass : sourcesClass) {
                    if (source.getClass().isAssignableFrom(sourceClass)) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    return new ValidResult(false, StringTools.appendJoinEmpty("[", targetName, "]的@MappingStruct注解中未包含[", source.getClass().getName(), "]的源信息"));
                }
            }
        }
        return new ValidResult(true);
    }

    @Override
    public String generate() {
        return defaultGenerate(this.getClass());
    }

    private static class ValidKey {

        private final boolean useSmartMode;
        private final Object target;
        private List<Object> sources;

        public ValidKey(boolean useSmartMode, Object target, List<Object> sources) {
            this.useSmartMode = useSmartMode;
            this.target = target;
            this.sources = sources;
        }

        public static ValidKey build(boolean useSmartMode, Object target, List<Object> sources) {
            return new ValidKey(useSmartMode, target, sources);
        }

        private boolean sourcesEquals(ValidKey other) {
            List<Object> thisSources = this.sources;
            List<Object> otherSources = other.sources;
            String thisSourcesJoinedName = StringTools.EMPTY;
            String otherSourcesJoinedName = StringTools.EMPTY;
            if(CollectionTools.isNotEmpty(thisSources)) {
                thisSourcesJoinedName = thisSources.stream().filter(ObjectTools::isNotNull).map(obj -> obj.getClass().getName()).collect(Collectors.joining());
            }
            if(CollectionTools.isNotEmpty(otherSources)) {
                otherSourcesJoinedName = otherSources.stream().filter(ObjectTools::isNotNull).map(obj -> obj.getClass().getName()).collect(Collectors.joining());
            }
            return thisSourcesJoinedName.equals(otherSourcesJoinedName);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ValidKey validKey = (ValidKey) o;
            return useSmartMode == validKey.useSmartMode && sourcesEquals(validKey);
        }

        @Override
        public int hashCode() {
            if(CollectionTools.isEmpty(sources)) {
                sources = Lists.newArrayList();
            }
            String sourcesClassName = sources.stream().filter(ObjectTools::isNotNull).map(source -> source.getClass().getName()).collect(Collectors.joining());
            int booleanHashCode = useSmartMode ? 1231 : 1237;
            if(target == null) {
                return doHashCode(booleanHashCode, sourcesClassName);
            }
            return doHashCode(booleanHashCode, target.getClass().getName(), sourcesClassName);
        }

        private int doHashCode(int booleanHashCode, String sourcesClassName) {
            int result = booleanHashCode;
            result = 31 * result + sourcesClassName.hashCode();
            return result;
        }

        private int doHashCode(int booleanHashCode, String targetClassName, String sourcesClassName) {
            int result = booleanHashCode;
            result = 31 * result + targetClassName.hashCode();
            result = 31 * result + sourcesClassName.hashCode();
            return result;
        }
    }

    private static class ValidResult {

        private final boolean result;
        private String errorMessage;

        public ValidResult(boolean result, String errorMessage) {
            this.result = result;
            this.errorMessage = errorMessage;
        }

        public ValidResult(boolean result) {
            this.result = result;
        }
    }
}