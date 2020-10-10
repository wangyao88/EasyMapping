package com.mohan.project.easymapping;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * 实体属性映射管理类接口
 *
 * @author mohan
 * @since 2020-09-18 09:22
 */
public interface IEasyMappingManager {

    /**
     * 刷新映射信息
     *
     * @param scanPath 扫描包路径 格式为 cn.com.cis
     */
    void refreshConfiguration(String scanPath);

    /**
     * 普通映射，需要预先根据注解解析映射信息
     *
     * @param target  需要映射的实例
     * @param sources 属性数据源，可传递多个对象
     */
    void normalMapping(Object target, Object... sources);

    /**
     * 普通映射，需要预先根据注解解析映射信息
     *
     * @param target  需要映射的实例
     * @param sources 属性数据源，可传递多个对象
     */
    void fastNormalMapping(Object target, Object... sources);

    /**
     * 普通映射，需要预先根据注解解析映射信息
     * 映射完调用回调函数
     *
     * @param callBack 回调函数
     * @param target   需要映射的实例
     * @param sources  属性数据源，可传递多个对象
     */
    void normalMapping(Consumer<Object> callBack, Object target, Object... sources);

    /**
     * 普通映射，需要预先根据注解解析映射信息
     * 映射完调用回调函数
     *
     * @param callBack 回调函数
     * @param target   需要映射的实例
     * @param sources  属性数据源，可传递多个对象
     */
    void fastNormalMapping(Consumer<Object> callBack, Object target, Object... sources);

    /**
     * 普通映射，需要预先根据注解解析映射信息
     *
     * @param targetClass 需要映射的对象类型
     * @param sources     属性数据源，可传递多个对象
     * @param <T>         对象类型泛型
     * @return 映射的对象类型的实例，由Optional包裹
     */
    <T> Optional<T> normalMapping(Class<T> targetClass, Object... sources);

    /**
     * 普通映射，需要预先根据注解解析映射信息
     *
     * @param targetClass 需要映射的对象类型
     * @param sources     属性数据源，可传递多个对象
     * @param <T>         对象类型泛型
     * @return 映射的对象类型的实例，由Optional包裹
     */
    <T> Optional<T> fastNormalMapping(Class<T> targetClass, Object... sources);

    /**
     * 普通映射，需要预先根据注解解析映射信息
     * 映射完调用回调函数
     *
     * @param callBack    回调函数
     * @param targetClass 需要映射的对象类型
     * @param sources     属性数据源，可传递多个对象
     * @param <T>         对象类型泛型
     */
    <T> void normalMapping(Consumer<T> callBack, Class<T> targetClass, Object... sources);

    /**
     * 普通映射，需要预先根据注解解析映射信息
     * 映射完调用回调函数
     *
     * @param callBack    回调函数
     * @param targetClass 需要映射的对象类型
     * @param sources     属性数据源，可传递多个对象
     * @param <T>         对象类型泛型
     */
    <T> void fastNormalMapping(Consumer<T> callBack, Class<T> targetClass, Object... sources);

    /**
     * 智能映射，不需要预先根据注解解析映射信息
     *
     * @param target  需要映射的实例
     * @param sources 属性数据源，可传递多个对象
     */
    void smartMapping(Object target, Object... sources);

    /**
     * 智能映射，不需要预先根据注解解析映射信息
     *
     * @param target  需要映射的实例
     * @param sources 属性数据源，可传递多个对象
     */
    void fastSmartMapping(Object target, Object... sources);

    /**
     * 智能映射，不需要预先根据注解解析映射信息
     * 映射完调用回调函数
     *
     * @param callBack 回调函数
     * @param target   要映射的实例
     * @param sources  属性数据源，可传递多个对象
     */
    void smartMapping(Consumer<Object> callBack, Object target, Object... sources);

    /**
     * 智能映射，不需要预先根据注解解析映射信息
     * 映射完调用回调函数
     *
     * @param callBack 回调函数
     * @param target   要映射的实例
     * @param sources  属性数据源，可传递多个对象
     */
    void fastSmartMapping(Consumer<Object> callBack, Object target, Object... sources);

    /**
     * 智能映射，不需要预先根据注解解析映射信息
     *
     * @param targetClass 需要映射的对象类型
     * @param sources     属性数据源，可传递多个对象
     * @param <T>         对象类型泛型
     * @return 映射的对象类型的实例，由Optional包裹
     */
    <T> Optional<T> smartMapping(Class<T> targetClass, Object... sources);

    /**
     * 智能映射，不需要预先根据注解解析映射信息
     *
     * @param targetClass 需要映射的对象类型
     * @param sources     属性数据源，可传递多个对象
     * @param <T>         对象类型泛型
     * @return 映射的对象类型的实例，由Optional包裹
     */
    <T> Optional<T> fastSmartMapping(Class<T> targetClass, Object... sources);

    /**
     * 智能映射，不需要预先根据注解解析映射信息
     * 映射完调用回调函数
     *
     * @param callBack    回调函数
     * @param targetClass 需要映射的对象类型
     * @param sources     属性数据源，可传递多个对象
     * @param <T>         对象类型泛型
     */
    <T> void smartMapping(Consumer<T> callBack, Class<T> targetClass, Object... sources);

    /**
     * 智能映射，不需要预先根据注解解析映射信息
     * 映射完调用回调函数
     *
     * @param callBack    回调函数
     * @param targetClass 需要映射的对象类型
     * @param sources     属性数据源，可传递多个对象
     * @param <T>         对象类型泛型
     */
    <T> void fastSmartMapping(Consumer<T> callBack, Class<T> targetClass, Object... sources);
}