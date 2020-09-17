package com.mohan.project.easymapping.mapping;

import com.mohan.project.easymapping.MappingParameter;
import com.mohan.project.easymapping.mapping.reflect.NormalReflectMapping;
import com.mohan.project.easymapping.mapping.reflect.SmartReflectMapping;
import com.mohan.project.easymapping.parser.BaseParser;
import com.mohan.project.easytools.common.CollectionTools;
import com.mohan.project.easytools.common.ObjectTools;
import com.mohan.project.easytools.common.StringTools;
import com.mohan.project.easytools.log.LogTools;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 实体属性映射器
 *
 * @author WangYao
 * @since 2019-08-23 13:36:23
 */
public abstract class BaseMapping implements Mapping {

    @Override
    public void mapping(boolean useSmartMode, Object target, Object... sources) {
        if (ObjectTools.isAnyNull(target, sources)) {
            return;
        }
        try {
            doMapping(useSmartMode, target, sources);
        } catch (Exception e) {
            LogTools.error(getErrorMessage(target.getClass(), sources), e);
        }
    }

    @Override
    public <T> Optional<T> mapping(boolean useSmartMode, Class<T> targetClass, Object... sources) {
        if (ObjectTools.isAnyNull(targetClass, sources)) {
            return Optional.empty();
        }
        try {
            T target = targetClass.newInstance();
            return doMapping(useSmartMode, target, sources);
        } catch (Exception e) {
            LogTools.error(getErrorMessage(targetClass, sources), e);
            return Optional.empty();
        }
    }

    private <T> Optional<T> doMapping(boolean useSmartMode, Object target, Object[] sources) {
        List<Object> filteredSources = Arrays.stream(sources).filter(ObjectTools::isNotNull).collect(Collectors.toList());
        if (!valid(useSmartMode, target, filteredSources)) {
            return Optional.empty();
        }
        if(useSmartMode) {
            return doSmartMapping(target, filteredSources);
        }
        String targetClassName = target.getClass().getName();
        Map<String, Collection<MappingParameter>> parsedMappingInfo = BaseParser.getInstance().getParsedMappingInfo();
        Collection<MappingParameter> mappingParameters = parsedMappingInfo.get(targetClassName);
        if (CollectionTools.isEmpty(mappingParameters)) {
            LogTools.warn("解析信息中不包含{}！请确认该类是否增加了MappingStruct注解！", targetClassName);
            return Optional.empty();
        }
        return doNormalMapping(target, filteredSources, mappingParameters);
    }

    protected abstract <T> Optional<T> doSmartMapping(Object target, List<Object> sources);

    protected abstract <T> Optional<T> doNormalMapping(Object target, List<Object> sources, Collection<MappingParameter> mappingParameters);

    private boolean valid(boolean useSmartMode, Object target, List<Object> sources) {
        List<String> errorMessage = BaseParser.getInstance().getErrorMessage();
        if (CollectionTools.isNotEmpty(errorMessage)) {
            LogTools.error(String.join(StringTools.LINE_BREAK, errorMessage));
            return false;
        }
        if (ObjectTools.isNull(target) || CollectionTools.isEmpty(sources)) {
            LogTools.error("参数值为空！");
            return false;
        }
        if(useSmartMode) {
            return true;
        }
        String targetName = target.getClass().getName();
        Map<String, Collection<String>> targetSourcesInfoMap = BaseParser.getInstance().getTargetSourcesInfo();
        Collection<String> sourcesClassName = targetSourcesInfoMap.get(targetName);
        if (CollectionTools.isEmpty(sourcesClassName)) {
            LogTools.error("未找到[{}]相关@MappingStruct的配置信息！", targetName);
            return false;
        }
        for (Object source : sources) {
            if (!sourcesClassName.contains(source.getClass().getName())) {
                LogTools.error("[{}]的@MappingStruct注解中未包含[{}]的源信息", targetName, source.getClass().getName());
                return false;
            }
        }
        return true;
    }

    private <T> String getErrorMessage(Class<T> targetClass, Object source) {
        return StringTools.append(StringTools.SPACE, "实体映射失败！", "target class = ", targetClass.getName(), "source = ", String.valueOf(source));
    }
}