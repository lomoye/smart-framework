package org.smart4j.framework.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.bean.Data;

import java.io.IOException;
import java.util.Date;


/**
 * Created by lomoye on 2017/5/14.
 * ^_^
 */
public final class JsonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    public static <T> String toJson(T obj) {
        String json;
        try {
            json = OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.error("to json failure", e);
            throw new RuntimeException(e);
        }

        return json;
    }

    public static <T> T fromJson(String jsonStr, Class<T> type) {
        T obj;
        try {
            obj = OBJECT_MAPPER.readValue(jsonStr, type);
        } catch (IOException e) {
            LOGGER.error("from json failure", e);
            throw new RuntimeException(e);
        }

        return obj;
    }
}
