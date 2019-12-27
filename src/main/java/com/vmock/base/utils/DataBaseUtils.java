package com.vmock.base.utils;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.sqlite.Function;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static cn.hutool.core.util.StrUtil.EMPTY;

/**
 * DataBaseUtils
 * 目前为处理sqlite的相关工具
 *
 * @author vt
 * @since 2019年12月24日
 */
@UtilityClass
public class DataBaseUtils {

    /**
     * 给指定的connection创建REGEXP支持
     *
     * @param connection 链接
     */
    @SneakyThrows
    public static void createRegexpFun(Connection connection) {
        Function.create(connection, "REGEXP", new Function() {
            @Override
            protected void xFunc() throws SQLException {
                String expression = value_text(0);
                String value = value_text(1);
                // null to empty
                value = value == null ? EMPTY : value;
                Pattern pattern = Pattern.compile(expression);
                result(pattern.matcher(value).find() ? 1 : 0);
            }
        });
    }

    /**
     * 创建查询，并返回map
     *
     * @param connection 链接
     * @param sql        sql
     */
    @SneakyThrows
    public static Map<String, Object> queryMap(Connection connection, String sql) {
        // 使用
        @Cleanup Statement statement = connection.createStatement();
        @Cleanup ResultSet resultSet = statement.executeQuery(sql);
        ResultSetMetaData md = resultSet.getMetaData();
        int columns = md.getColumnCount();
        HashMap<String, Object> row = new HashMap();
        // limit 1, no while
        if (resultSet.next()) {
            for (int i = 1; i <= columns; i++) {
                row.put(md.getColumnName(i), resultSet.getObject(i));
            }
            return row;
        }
        return null;
    }
}
