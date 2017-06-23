/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.dao.impl;

import cn.edu.xmu.bip.dao.IBaseDAO;
import cn.edu.xmu.bip.util.NativeUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * @author luoxin
 * @version 2017-6-13
 */
class BaseDAOImpl implements IBaseDAO {
    private static final String DB_DRIVER = "org.sqlite.JDBC";
    private static final String DB_URL;
    private static final String DB_URL_PREFIX = "jdbc:sqlite:";
    private static final String DB_URL_SUFFIX = "db" + File.separatorChar + "bip.db";
    private static final String INITIAL_SQL_FILE = "/init.sql";

    private static final Logger logger = LoggerFactory.getLogger(BaseDAOImpl.class);

    static {
        //加载数据库驱动
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            logger.error("error loading db driver", e);
            System.exit(-1);
        }
        //拼接数据库路径
        DB_URL = DB_URL_PREFIX + NativeUtil.getAppDataPath() + DB_URL_SUFFIX;
        logger.info("db url is {}", DB_URL);
        //初始化数据库
        try {
            initial();
        } catch (IOException e) {
            logger.error("error reading initialization sql file", e);
            System.exit(-1);
        } catch (SQLException e) {
            logger.error("error executing initialization sql statement", e);
            System.exit(-1);
        }
    }

    private static void initial() throws IOException, SQLException {
        try (InputStream is = BaseDAOImpl.class.getResourceAsStream(INITIAL_SQL_FILE);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String sql;
            while ((sql = br.readLine()) != null) {
                if (!StringUtils.isEmpty(sql)) {
                    try (Connection conn = DriverManager.getConnection(DB_URL)) {
                        new QueryRunner().batch(conn, sql, new Object[1][]);
                    }
                }
            }
        }
    }

    Integer insert(String sql, Object... bindArgs) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            return new QueryRunner().
                    insert(connection, sql, new ScalarHandler<Integer>("last_insert_rowid()"), bindArgs);
        }
    }

    int update(String sql, Object... bindArgs) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            return new QueryRunner().update(conn, sql, bindArgs);
        }
    }

    <T> T select(Class<T> T, String sql, Object... bindArgs) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            return new QueryRunner().query(conn, sql, new BeanHandler<>(T), bindArgs);
        }
    }

    <T> List<T> selectBatch(Class<T> T, String sql, Object... bindArgs) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            return new QueryRunner().query(conn, sql, new BeanListHandler<>(T), bindArgs);
        }
    }
}
