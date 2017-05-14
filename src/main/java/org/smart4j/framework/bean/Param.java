package org.smart4j.framework.bean;

import java.util.Map;

/**
 * Created by lomoye on 2017/5/14.
 * ^_^ 请求参数对象
 */
public class Param {
    private Map<String, Object> paramMap;

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public Long getLong(String name) {
        Object object = paramMap.get(name);
        return object == null ? null : (Long) object;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }
}
