package com.mohan.project.easymapping.mapping;

import com.mohan.project.easymapping.MappingParameter;
import com.mohan.project.easymapping.mapping.valid.DefaultValidator;
import com.mohan.project.easymapping.parser.BaseParser;
import com.mohan.project.easytools.common.CollectionTools;
import com.mohan.project.easytools.common.ObjectTools;
import com.mohan.project.easytools.common.StringTools;
import com.mohan.project.easytools.log.LogTools;
import com.mohan.project.strategyfactory.core.StrategyFactory;
import com.mohan.project.strategyfactory.core.ThreeArgStrategy;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 实体属性映射器
 *
 * @author mohan
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
        Optional<DefaultValidator> defaultValidatorOptional = StrategyFactory.getThreeArgStrategyByClass(DefaultValidator.class);
        if(defaultValidatorOptional.isPresent()) {
            DefaultValidator defaultValidator = defaultValidatorOptional.get();
            Boolean validResult = defaultValidator.handle(useSmartMode, target, filteredSources);
            if (!validResult) {
                return Optional.empty();
            }
        }
        if(useSmartMode) {
            return doSmartMapping(target, filteredSources);
        }
        String targetClassName = target.getClass().getName();
        Map<String, Collection<MappingParameter>> parsedMappingInfo = BaseParser.getInstance().getParsedMappingInfo();
        Collection<MappingParameter> mappingParameters = parsedMappingInfo.get(targetClassName);
        if (CollectionTools.isEmpty(mappingParameters)) {
            LogTools.warn("解析信息中不包含{0}！请确认该类是否增加了MappingStruct注解！", targetClassName);
            return Optional.empty();
        }
        return doNormalMapping(target, filteredSources, mappingParameters);
    }

    protected abstract <T> Optional<T> doSmartMapping(Object target, List<Object> sources);

    protected abstract <T> Optional<T> doNormalMapping(Object target, List<Object> sources, Collection<MappingParameter> mappingParameters);

    private <T> String getErrorMessage(Class<T> targetClass, Object source) {
        return StringTools.append(StringTools.SPACE, "实体映射失败！", "target class = ", targetClass.getName(), "source = ", String.valueOf(source));
    }
}