package org.smart4j.framework.helper;

import org.apache.commons.lang3.ArrayUtils;
import org.smart4j.framework.annotation.Action;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Request;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by lomoye on 2017/5/14.
 * ^_^ 控制器助手类
 */
public class ControllerHelper {

    /**
     * 用于存放请求与处理器的映射关系
     */
    private static final Map<Request, Handler> ACTION_MAP = new HashMap<>();

    static {
        //获取所有的controller类
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();

        for (Class<?> controllerClass : controllerClassSet) {
            Method[] methods = controllerClass.getDeclaredMethods();
            if (ArrayUtils.isNotEmpty(methods)) {
                for (Method method : methods) {
                    if (method.isAnnotationPresent(Action.class)) {
                        Action action = method.getAnnotation(Action.class);
                        String value = action.value();
                        if (value.matches("\\w+:/\\w+")) {
                            String[] params = value.split(":");
                            String requestMethod = params[0];
                            String requestPath = params[1];

                            Request request = new Request(requestMethod, requestPath);
                            Handler handler = new Handler(controllerClass, method);

                            ACTION_MAP.put(request, handler);
                        }
                    }
                }
            }
        }
    }


    public static Handler getHandler(String requestMethod, String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        return ACTION_MAP.get(request);
    }
}
