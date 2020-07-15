package com.mohan.project.easymapping;

import com.mohan.project.easymapping.exception.OnceStartException;
import com.mohan.project.easymapping.parser.BaseParser;
import com.mohan.project.easytools.log.LogTools;

/**
 * 实体属性映射启动器
 *
 * @author WangYao
 * @since 2019-08-23 13:36:23
 */
public final class MappingStructStarter {

    private volatile static boolean parsed = false;
    private static Throwable throwable;

    public static final Object LOCK = new Object();

    private MappingStructStarter() {

    }

    public static void start(String scanPath) {
        try {
            synchronized (LOCK) {
                if(parsed) {
                    throw new OnceStartException();
                }
                BaseParser.getInstance().doParse(scanPath);
                parsed = true;
            }
        } catch (Exception e) {
            LogTools.error("解析MappingStruct注解失败!", e);
            throwable = e;
        }
        showMappingInfo();
    }

    public static void showMappingInfo() {
        BaseParser.getInstance().showMappingInfo();
    }

    static boolean valid(boolean useSmartMode) {
        if (throwable != null) {
            LogTools.error("解析MappingStruct注解失败！无法映射！", throwable);
            return false;
        }
        if (!parsed && !useSmartMode) {
            LogTools.error("尚未解析MappingStruct注解！无法映射！请先调用MappingStructStarter.start(String scanPath)方法");
            return false;
        }
        return true;
    }
}
