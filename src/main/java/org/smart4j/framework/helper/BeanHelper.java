package org.smart4j.framework.helper;


import org.smart4j.framework.bean.TestBeanService;
import org.smart4j.framework.util.ClassUtil;
import org.smart4j.framework.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by lomoye on 2017/5/13.
 * ^_^ Bean 助手类
 */
public class BeanHelper {
    /**
     * 定义 Bean 映射 （用于存放Bean类和Bean实例的映射关系）
     */
    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<>();


    static {
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();

        for (Class<?> clazz : beanClassSet) {
            Object obj = ReflectionUtil.newInstance(clazz);
            BEAN_MAP.put(clazz, obj);
        }
    }

    /**
     * 获取 Bean 映射
     */
    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    /**
     * 获取 Bean 实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        if (!BEAN_MAP.containsKey(clazz)) {
            throw new RuntimeException("can not get bean by class: " + clazz);
        }

        return (T) BEAN_MAP.get(clazz);
    }


}
