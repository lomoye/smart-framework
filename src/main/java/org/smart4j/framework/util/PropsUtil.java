package org.smart4j.chapter1.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by lomoye on 2017/5/12.
 * ^_^ 读取properties文件的工具类
 */
public final class PropsUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);

    /**
     * 加载属性文件
     */
    public static Properties loadProps(String fileName) {
        Properties properties = null;
        InputStream inputStream = null;
        try {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) {
                throw new FileNotFoundException(fileName + " file is not found");
            }
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            LOGGER.error("load properties file failure", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.error("close inputStream failure", e);
                }
            }
        }

        return properties;
    }

    /**
     * 获取字符型属性 默认为空字符串
     */
    public static String getString(Properties properties, String key) {
        return getString(properties, key, "");
    }

    /**
     * 获取字符型属性 可指定默认值
     */
    public static String getString(Properties properties, String key, String defaultValue) {
        if (properties.containsKey(key)) {
            return properties.getProperty(key);
        } else {
            return defaultValue;
        }
    }

    /**
     * 获取数值类属性（默认值为0）
     */
    public static int getInt(Properties properties, String key) {
        return getInt(properties, key, 0);
    }

    /**
     * 获取数值类属性 可指定默认值
     */
    public static int getInt(Properties properties, String key, int defaultValue) {
        if (properties.containsKey(key)) {
            String value = properties.getProperty(key);
            if (value != null && !value.equals("")) {
                return Integer.valueOf(value);
            }
        }

        return defaultValue;
    }

    /**
     * 获取布尔属性（默认false)
     */
    public static boolean getBoolean(Properties properties, String key) {
        return getBoolean(properties, key, false);
    }

    public static boolean getBoolean(Properties properties, String key, boolean defaultValue) {
        if (properties.containsKey(key)) {
            String value = properties.getProperty(key);
            if (value != null && !value.equals("")) {
                return Boolean.valueOf(value);
            }
        }

        return defaultValue;
    }

}
