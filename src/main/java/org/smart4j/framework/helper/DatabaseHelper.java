package org.smart4j.chapter1.helper;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter1.util.NameUtils;
import org.smart4j.chapter1.util.PropsUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by lomoye on 2017/5/12.
 * ^_^ 数据库操作助手类
 */
public class DatabaseHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);

    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<>();

    private static final BasicDataSource DATA_SOURCE;

    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    static {
        Properties conf = PropsUtil.loadProps("config.properties");
        DRIVER = conf.getProperty("jdbc.driver");
        URL = conf.getProperty("jdbc.url");
        USERNAME = conf.getProperty("jdbc.username");
        PASSWORD = conf.getProperty("jdbc.password");

        //数据库连接托管给连接池
        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(DRIVER);
        DATA_SOURCE.setUrl(URL);
        DATA_SOURCE.setUsername(USERNAME);
        DATA_SOURCE.setPassword(PASSWORD);
    }


    /**
     * 获取数据库连接
     */
    public static Connection getConnection() {
        Connection connection = CONNECTION_HOLDER.get();
        if (connection == null) {
            try {
                connection = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                LOGGER.error("get connection failed", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.set(connection);
            }
        }

        return connection;
    }

    public static <T> List<T> queryEntityList(Class<T> clazz, String sql, Object... params) {
        List<T> entityList;
        try {
            Connection conn = getConnection();
            entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<>(clazz), params);
        } catch (SQLException e) {
            LOGGER.error("query entityList failure", e);
            throw new RuntimeException(e);
        }

        return entityList;
    }


    public static <T> T queryEntity(Class<T> clazz, String sql, Object... params) {
        T entity;
        try {
            Connection conn = getConnection();
            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<>(clazz), params);
        } catch (SQLException e) {
            LOGGER.error("query entity failure", e);
            throw new RuntimeException(e);
        }

        return entity;
    }


    public static List<Map<String, Object>> executeQuery(String sql, Object... params) {
        List<Map<String, Object>> result;

        Connection conn = getConnection();
        try {
            result = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
        } catch (SQLException e) {
            LOGGER.error("execute query failure", e);
            throw new RuntimeException(e);
        }

        return result;
    }


    public static int executeUpdate(String sql, Object... params) {
        int rows;

        Connection conn = getConnection();
        try {
            rows = QUERY_RUNNER.update(conn, sql, params);
        } catch (SQLException e) {
            LOGGER.error("execute update failure", e);
            throw new RuntimeException(e);
        }

        return rows;
    }


    public static <T> boolean insertEntity(Class<T> clazz, Map<String, Object> fieldMap) {
        if (fieldMap == null || fieldMap.isEmpty()) {
            LOGGER.error("insert entity failure, fieldMap is empty");
            return false;
        }

        String sql = "INSERT INTO" + getTableName(clazz);

        StringBuilder columns = new StringBuilder(" (");
        StringBuilder values = new StringBuilder(" (");
        for (String filedName : fieldMap.keySet()) {
            columns.append(filedName).append(", ");
            values.append("?, ");
        }

        columns.replace(columns.lastIndexOf(", "), columns.length(), ")");
        values.replace(values.lastIndexOf(", "), values.length(), ")");

        sql += columns + " VALUES " + values;

        Object params = fieldMap.values().toArray();

        return executeUpdate(sql, params) == 1;
    }

    public static <T> boolean updateEntity(Class<T> clazz, long id, Map<String, Object> fieldMap) {
        if (fieldMap == null || fieldMap.isEmpty()) {
            LOGGER.error("insert entity failure, fieldMap is empty");
            return false;
        }

        String sql = "UPDATE " + getTableName(clazz) + " SET ";
        StringBuilder columns = new StringBuilder();
        for (String filedName : fieldMap.keySet()) {
            columns.append(filedName).append("=?, ");
        }

        sql += columns.substring(0, columns.lastIndexOf(", ")) + " WHERE id=?";

        List<Object> paramList = new ArrayList<>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);

        return executeUpdate(sql, paramList.toArray()) == 1;
    }


    public static <T> boolean deleteEntity(Class<T> clazz, long id) {
        String sql = "DELETE FROM " + getTableName(clazz) + " WHERE id=?";
        return executeUpdate(sql, id) == 1;
    }


    /**
     * 执行sql文件
     */
    public static void executeSqlFile(String sqlFilePath) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(sqlFilePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        try {
            String sql;
            while ((sql = reader.readLine()) != null) {
                executeUpdate(sql);
            }
        } catch (IOException e) {
            LOGGER.error("execute sql file failure", e);
            throw new RuntimeException(e);
        }
    }

    private static <T> String getTableName(Class<T> clazz) {
        return NameUtils.camelToUnderline(clazz.getSimpleName());
    }

}
