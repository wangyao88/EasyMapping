package com.mohan.project.easymapping;

import com.mohan.project.easymapping.mapping.BaseMapping;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * 实体属性映射管理类
 *
 * @author WangYao
 * @since 2019-08-23 13:36:23
 */
public final class MappingStructManager {

    private MappingStructManager() {
    }

    /**
     * 刷新映射信息
     *
     * @param scanPath 扫描包路径 格式为 cn.com.cis
     */
    public static void refreshConfiguration(String scanPath) {
        MappingStructStarter.start(scanPath);
    }

    /**
     * 普通映射，需要预先根据注解解析映射信息
     *
     * @param target  需要映射的实例
     * @param sources 属性数据源，可传递多个对象
     */
    public static void normalMapping(Object target, Object... sources) {
        if (MappingStructStarter.valid(false)) {
            BaseMapping.getInstance().mapping(false, target, sources);
        }
    }

    /**
     * 普通映射，需要预先根据注解解析映射信息
     * 映射完调用回调函数
     *
     * @param callBack 回调函数
     * @param target   需要映射的实例
     * @param sources  属性数据源，可传递多个对象
     */
    public static void normalMapping(Consumer<Object> callBack, Object target, Object... sources) {
        if (MappingStructStarter.valid(false)) {
            BaseMapping.getInstance().mapping(false, target, sources);
            callBack.accept(target);
        }
    }

    /**
     * 普通映射，需要预先根据注解解析映射信息
     *
     * @param targetClass 需要映射的对象类型
     * @param sources     属性数据源，可传递多个对象
     * @param <T>         对象类型泛型
     * @return 映射的对象类型的实例，由Optional包裹
     */
    public static <T> Optional<T> normalMapping(Class<T> targetClass, Object... sources) {
        if (MappingStructStarter.valid(false)) {
            return BaseMapping.getInstance().mapping(false, targetClass, sources);
        }
        return Optional.empty();
    }

    /**
     * 普通映射，需要预先根据注解解析映射信息
     * 映射完调用回调函数
     *
     * @param callBack    回调函数
     * @param targetClass 需要映射的对象类型
     * @param sources     属性数据源，可传递多个对象
     * @param <T>         对象类型泛型
     */
    public static <T> void normalMapping(Consumer<T> callBack, Class<T> targetClass, Object... sources) {
        if (MappingStructStarter.valid(false)) {
            BaseMapping.getInstance().mapping(false, targetClass, sources).ifPresent(callBack);
        }
    }

    /**
     * 智能映射，不需要预先根据注解解析映射信息
     *
     * @param target  需要映射的实例
     * @param sources 属性数据源，可传递多个对象
     */
    public static void smartMapping(Object target, Object... sources) {
        if (MappingStructStarter.valid(true)) {
            BaseMapping.getInstance().mapping(true, target, sources);
        }
    }

    /**
     * 智能映射，不需要预先根据注解解析映射信息
     * 映射完调用回调函数
     *
     * @param callBack 回调函数
     * @param target   要映射的实例
     * @param sources  属性数据源，可传递多个对象
     */
    public static void smartMapping(Consumer<Object> callBack, Object target, Object... sources) {
        if (MappingStructStarter.valid(true)) {
            BaseMapping.getInstance().mapping(true, target, sources);
            callBack.accept(target);
        }
    }

    /**
     * 智能映射，不需要预先根据注解解析映射信息
     *
     * @param targetClass 需要映射的对象类型
     * @param sources     属性数据源，可传递多个对象
     * @param <T>         对象类型泛型
     * @return 映射的对象类型的实例，由Optional包裹
     */
    public static <T> Optional<T> smartMapping(Class<T> targetClass, Object... sources) {
        if (MappingStructStarter.valid(true)) {
            return BaseMapping.getInstance().mapping(true, targetClass, sources);
        }
        return Optional.empty();
    }

    /**
     * 智能映射，不需要预先根据注解解析映射信息
     * 映射完调用回调函数
     *
     * @param callBack    回调函数
     * @param targetClass 需要映射的对象类型
     * @param sources     属性数据源，可传递多个对象
     * @param <T>         对象类型泛型
     */
    public static <T> void smartMapping(Consumer<T> callBack, Class<T> targetClass, Object... sources) {
        if (MappingStructStarter.valid(true)) {
            BaseMapping.getInstance().mapping(true, targetClass, sources).ifPresent(callBack);
        }
    }
}