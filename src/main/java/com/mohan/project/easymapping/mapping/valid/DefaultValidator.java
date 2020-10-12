package com.mohan.project.easymapping.mapping.valid;

import com.mohan.project.easymapping.parser.BaseParser;
import com.mohan.project.easytools.common.CollectionTools;
import com.mohan.project.easytools.common.ObjectTools;
import com.mohan.project.easytools.common.StringTools;
import com.mohan.project.easytools.log.LogTools;
import com.mohan.project.strategyfactory.core.ThreeArgStrategy;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 默认校验器
 *
 * @author mohan
 * @since 2020-10-10 17:04
 */
public class DefaultValidator implements ThreeArgStrategy<Boolean, Boolean, Object, List> {

    @Override
    public Boolean handle(Boolean useSmartMode, Object target, List sources) {
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
        Map<String, Collection<Class<?>>> targetSourcesInfoMap = BaseParser.getInstance().getTargetSourcesInfo();
        Collection<Class<?>> sourcesClass = targetSourcesInfoMap.get(targetName);
        if (CollectionTools.isEmpty(sourcesClass)) {
            LogTools.error("未找到[{0}]相关@MappingStruct的配置信息！", targetName);
            return false;
        }
        for (Object source : sources) {
            if (!sourcesClass.contains(source.getClass())) {
                boolean flag = false;
                for (Class<?> sourceClass : sourcesClass) {
                    if(source.getClass().isAssignableFrom(sourceClass)) {
                        flag = true;
                        break;
                    }
                }
                if(!flag) {
                    LogTools.error("[{0}]的@MappingStruct注解中未包含[{1}]的源信息", targetName, source.getClass().getName());
                    return false;
                }
            }
        }
        return true;
    }
}