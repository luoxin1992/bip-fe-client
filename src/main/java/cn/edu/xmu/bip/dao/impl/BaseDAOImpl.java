/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.dao.impl;

import cn.com.lx1992.lib.client.util.NativeUtil;
import cn.edu.xmu.bip.dao.IBaseDAO;
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

class BaseDAOImpl implements IBaseDAO {
    private static final String DB_DRIVER = "org.sqlite.JDBC";
    private static final String DB_URL;
    private static final String DB_URL_PREFIX = "jdbc:sqlite:";
    private static final String DB_FILE_NAME = "bip.db";

    private final Logger logger = LoggerFactory.getLogger(BaseDAOImpl.class);

    static {
        //拼接数据库路径
        DB_URL = DB_URL_PREFIX + NativeUtil.getAppDataDirectoryPath("db") + File.separatorChar + DB_FILE_NAME;
        //加载数据库驱动
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化数据库
     *
     * @param path SQL脚本路径
     */
    void executeBatch(String path) {
        try (InputStream is = BaseDAOImpl.class.getResourceAsStream(path);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String sql;
            while ((sql = br.readLine()) != null) {
                if (!StringUtils.isEmpty(sql)) {
                    try (Connection connection = getConnection()) {
                        new QueryRunner().batch(connection, sql, new Object[1][]);
                    } catch (SQLException e) {
                        logger.error("execute sql statement ({}) failed", sql, e);
                        throw new ClientException(ClientExceptionEnum.DB_EXECUTE_SQL_ERROR, e);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("read sql file {} failed", path, e);
            throw new ClientException(ClientExceptionEnum.DB_READ_SQL_ERROR);
        }
    }

    /**
     * 插入
     *
     * @param sql  SQL语句
     * @param args 绑定参数
     */
    void insert(String sql, Object... args) {
        try (Connection connection = getConnection()) {
            int id = new QueryRunner().
                    insert(connection, sql, new ScalarHandler<Integer>("last_insert_rowid()"), args);
            if (id <= 0) {
                logger.error("invalid last insert row id {}", id);
                throw new ClientException(ClientExceptionEnum.DB_INSERT_ERROR);
            }
        } catch (SQLException e) {
            logger.error("execute sql statement ({}) failed", sql, e);
            throw new ClientException(ClientExceptionEnum.DB_INSERT_ERROR, e);
        }
    }

    /**
     * 更新
     *
     * @param sql  SQL语句
     * @param args 绑定参数
     */
    void update(String sql, Object... args) {
        try (Connection connection = getConnection()) {
            int rows = new QueryRunner().update(connection, sql, args);
            if (rows <= 0) {
                logger.error("invalid number of rows affected {}", rows);
                throw new ClientException(ClientExceptionEnum.DB_UPDATE_ERROR);
            }
        } catch (SQLException e) {
            logger.error("execute sql statement ({}) failed", sql, e);
            throw new ClientException(ClientExceptionEnum.DB_UPDATE_ERROR, e);
        }
    }

    /**
     * 统计
     *
     * @param sql  SQL语句
     * @param args 绑定参数
     * @return 统计结果
     */
    Integer count(String sql, Object... args) {
        try (Connection connection = getConnection()) {
            return new QueryRunner().query(connection, sql, new ScalarHandler<Integer>(), args);
        } catch (SQLException e) {
            logger.error("execute sql statement ({}) failed", sql, e);
            throw new ClientException(ClientExceptionEnum.DB_SELECT_ERROR, e);
        }
    }

    /**
     * 查询
     *
     * @param type 结果集映射类型
     * @param sql  SQL语句
     * @param args 绑定参数
     * @return 查询结果
     */
    <T> T select(Class<T> type, String sql, Object... args) {
        try (Connection connection = getConnection()) {
            return new QueryRunner().query(connection, sql, new BeanHandler<>(type), args);
        } catch (SQLException e) {
            logger.error("execute sql statement ({}) failed", sql, e);
            throw new ClientException(ClientExceptionEnum.DB_SELECT_ERROR, e);
        }
    }

    /**
     * 批量查询
     *
     * @param type 结果集映射类型
     * @param sql  SQL语句
     * @param args 绑定参数
     * @return 查询结果
     */
    <T> List<T> selectBatch(Class<T> type, String sql, Object... args) {
        try (Connection connection = getConnection()) {
            return new QueryRunner().query(connection, sql, new BeanListHandler<>(type), args);
        } catch (SQLException e) {
            logger.error("execute sql statement ({}) failed", sql, e);
            throw new ClientException(ClientExceptionEnum.DB_SELECT_ERROR, e);
        }
    }

    /**
     * 获取数据库连接
     * 同步方法防止对SQLite多线程写产生并发错误
     *
     * @return 数据库连接
     * @throws SQLException 发生数据库错误
     */
    private synchronized Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
