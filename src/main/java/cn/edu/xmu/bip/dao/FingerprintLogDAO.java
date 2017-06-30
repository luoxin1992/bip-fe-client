/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.dao;

import cn.edu.xmu.bip.domain.FingerprintLogDO;
import cn.edu.xmu.bip.exception.ClientException;
import cn.edu.xmu.bip.exception.ClientExceptionEnum;

import java.sql.SQLException;
import java.util.List;

/**
 * 指纹(扫描仪)日志DAO
 *
 * @author luoxin
 * @version 2017-6-13
 */
public class FingerprintLogDAO extends BaseDAO {
    private static final String SQL_COUNT = "SELECT COUNT(1) FROM tbl_fingerprint_log " +
            "WHERE timestamp > ? AND timestamp < ? AND status = 0";
    private static final String SQL_SELECT = "SELECT id, type, content, timestamp FROM tbl_fingerprint_log " +
            "WHERE timestamp > ? AND timestamp < ? AND status = 0 ORDER BY timestamp DESC LIMIT ?, ?";
    private static final String SQL_INSERT = "INSERT INTO tbl_fingerprint_log(type, content, timestamp) " +
            "VALUES (?, ?, ?)";

    /**
     * 统计
     *
     * @param minTimestamp 时间戳下限
     * @param maxTimestamp 时间戳上限
     * @return 统计结果
     * @throws SQLException 执行数据库操作时出错
     */
    public int count(long minTimestamp, long maxTimestamp) throws SQLException {
        return super.count(SQL_COUNT, minTimestamp, maxTimestamp);
    }

    /**
     * 条件查询
     *
     * @param minTimestamp 时间戳下限
     * @param maxTimestamp 时间戳上限
     * @param offset       页起始
     * @param row          页长度
     * @return 查询结果
     * @throws SQLException 执行数据库操作时出错
     */
    public List<FingerprintLogDO> select(long minTimestamp, long maxTimestamp, int offset, int row) throws
            SQLException {
        return super.selectBatch(FingerprintLogDO.class, SQL_SELECT, minTimestamp, maxTimestamp, offset, row);
    }

    /**
     * 插入
     *
     * @param domain 实体
     * @throws SQLException 执行数据库操作时出错
     */
    public void insert(FingerprintLogDO domain) throws SQLException {
        int id = super.insert(SQL_INSERT, domain.getType(), domain.getContent(), domain.getTimestamp());
        if (id <= 0) {
            throw new ClientException(ClientExceptionEnum.DATABASE_INSERT_ERROR);
        }
    }
}
