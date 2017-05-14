package org.smart4j.framework;

import org.apache.commons.lang3.StringUtils;
import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.View;
import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ConfigHelper;
import org.smart4j.framework.helper.ControllerHelper;
import org.smart4j.framework.util.ReflectionUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lomoye on 2017/5/14.
 * ^_^ 请求转发器
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        //初始化相关helper类
        HelperLoader.init();
        //获取Servlet上下文(用于注册servlet)
        ServletContext servletContext = config.getServletContext();

        //注册jsp servlet
        ServletRegistration jspRegistration = servletContext.getServletRegistration("jsp");
        jspRegistration.addMapping(ConfigHelper.getAppJspPath() + "*");

        //注册静态资源 servlet
        ServletRegistration defaultRegistration = servletContext.getServletRegistration("default");
        defaultRegistration.addMapping(ConfigHelper.getAppAssetPath() + "*");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求方法和路径
        String requestMethod = req.getMethod().toLowerCase();
        String requestPath = req.getPathInfo();

        //获取 Action 处理器
        Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
        if (handler == null) {
            return;
        }
        Class<?> controllerClass = handler.getClass();
        Object controller = BeanHelper.getBean(controllerClass);

        //获取请求参数
        Map<String, Object> paramMap = getRequestParamMap(req);


        Method method = handler.getActionMethod();
        Object result = ReflectionUtil.invokeMethod(controller, method/*, param*/);

        //处理result的流转
        if (result == null) {
            return;
        }

        if (result instanceof View) {

        } else if (result instanceof Data) {

        } else {
            throw new RuntimeException("not support result type");
        }

    }

    private Map<String, Object> getRequestParamMap(HttpServletRequest req) {
        Map<String, Object> paramMap = new HashMap<>();
        Enumeration<String> paramNames = req.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            Object paramValue = req.getParameter(paramName);
            paramMap.put(paramName, paramValue);
        }
        String body = "";//TODO

        if (StringUtils.isEmpty(body)) {
            return paramMap;
        }

        String params[] = StringUtils.split(body, "&");

        return null;
    }


}
