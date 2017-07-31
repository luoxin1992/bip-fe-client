/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.dao;

import cn.com.lx1992.lib.client.util.NativeUtil;
import cn.edu.xmu.bip.exception.ClientException;
import cn.edu.xmu.bip.exception.ClientExceptionEnum;
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
 * DAO基类
 *
 * @author luoxin
 * @version 2017-6-13
 */
class BaseDAO {
    private static final String DB_DRIVER = "org.sqlite.JDBC";
    private static final String DB_URL;
    private static final String DB_URL_PREFIX = "jdbc:sqlite:";
    private static final String DB_FILE_NAME = "bip.db";
    private static final String INIT_FILE_NAME = "/file/init.sql";

    private static final Logger logger = LoggerFactory.getLogger(BaseDAO.class);

    static {
        //拼接数据库路径
        DB_URL = DB_URL_PREFIX + NativeUtil.getAppDataDirectoryPath("db") + File.separatorChar + DB_FILE_NAME;
        //加载数据库驱动
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            logger.error("load db driver failed", e);
            throw new ClientException(ClientExceptionEnum.DB_LOAD_DRIVER_ERROR);
        }
        //初始化数据库
        try {
            initialDb();
        } catch (IOException e) {
            logger.error("read initialization sql file failed", e);
            throw new ClientException(ClientExceptionEnum.DB_READ_INIT_FILE_ERROR);
        } catch (SQLException e) {
            logger.error("execute initialization sql statement failed", e);
            throw new ClientException(ClientExceptionEnum.DB_EXEC_INIT_STMT_ERROR);
        }
    }

    /**
     * 初始化数据库
     *
     * @throws IOException  读取SQL脚本时出错
     * @throws SQLException 执行数据库操作时出错
     */
    private static void initialDb() throws IOException, SQLException {
        try (InputStream is = BaseDAO.class.getResourceAsStream(INIT_FILE_NAME);
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

    /**
     * 插入
     *
     * @param sql      SQL语句
     * @param bindArgs 绑定参数
     */
    void insert(String sql, Object... bindArgs) {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            int id = new QueryRunner().
                    insert(connection, sql, new ScalarHandler<Integer>("last_insert_rowid()"), bindArgs);
            if (id <= 0) {
                logger.error("invalid last insert row id {}", id);
                throw new ClientException(ClientExceptionEnum.DB_INSERT_ERROR);
            }
        } catch (SQLException e) {
            logger.error("execute INSERT statement ({}) failed", sql, e);
            throw new ClientException(ClientExceptionEnum.DB_INSERT_ERROR, e);
        }
    }

    /**
     * 更新
     *
     * @param sql      SQL语句
     * @param bindArgs 绑定参数
     */
    void update(String sql, Object... bindArgs) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            int rows = new QueryRunner().update(conn, sql, bindArgs);
            if (rows <= 0) {
                logger.error("invalid number of rows affected {}", rows);
                throw new ClientException(ClientExceptionEnum.DB_UPDATE_ERROR);
            }
        } catch (SQLException e) {
            logger.error("execute UPDATE statement ({}) failed", sql, e);
            throw new ClientException(ClientExceptionEnum.DB_UPDATE_ERROR, e);
        }
    }

    /**
     * 统计
     *
     * @param sql      SQL语句
     * @param bindArgs 绑定参数
     * @return 统计结果
     */
    Integer count(String sql, Object... bindArgs) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            return new QueryRunner().query(conn, sql, new ScalarHandler<Integer>(), bindArgs);
        } catch (SQLException e) {
            logger.error("execute SELECT statement ({}) failed", sql, e);
            throw new ClientException(ClientExceptionEnum.DB_SELECT_ERROR, e);
        }
    }

    /**
     * 查询
     *
     * @param T        结果集映射类型
     * @param sql      SQL语句
     * @param bindArgs 绑定参数
     * @return 查询结果
     */
    <T> T select(Class<T> T, String sql, Object... bindArgs) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            return new QueryRunner().query(conn, sql, new BeanHandler<>(T), bindArgs);
        } catch (SQLException e) {
            logger.error("execute SELECT statement ({}) failed", sql, e);
            throw new ClientException(ClientExceptionEnum.DB_SELECT_ERROR, e);
        }
    }

    /**
     * 批量查询
     *
     * @param T        结果集映射类型
     * @param sql      SQL语句
     * @param bindArgs 绑定参数
     * @return 查询结果
     */
    <T> List<T> selectBatch(Class<T> T, String sql, Object... bindArgs) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            return new QueryRunner().query(conn, sql, new BeanListHandler<>(T), bindArgs);
        } catch (SQLException e) {
            logger.error("execute SELECT statement ({}) failed", sql, e);
            throw new ClientException(ClientExceptionEnum.DB_SELECT_ERROR, e);
        }
    }
}
