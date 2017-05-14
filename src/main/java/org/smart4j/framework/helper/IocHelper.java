package org.smart4j.framework.helper;

import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by lomoye on 2017/5/13.
 * ^_^ 依赖注入助手类
 */

public final class IocHelper {

    static {
        //获取所有的Bean类与Bean实例之间的映射关系
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (!beanMap.isEmpty()) {
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                Class<?> clazz = beanEntry.getKey();
                Object obj = beanEntry.getValue();
                Field[] fields = clazz.getDeclaredFields();
                if (fields != null && fields.length != 0) {
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(Inject.class)) {
                            Object injectObj = beanMap.get(field.getType());
                            if (injectObj != null) {
                                ReflectionUtil.setField(obj, field, injectObj);
                            }
                        }
                    }
                }
            }
        }
    }

}
