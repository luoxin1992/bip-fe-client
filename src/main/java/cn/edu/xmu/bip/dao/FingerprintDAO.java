/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.dao;

import cn.com.lx1992.lib.client.util.DateTimeUtil;
import cn.edu.xmu.bip.domain.FingerprintDO;

import java.util.List;

/**
 * 指纹DAO
 *
 * @author luoxin
 * @version 2017-6-13
 */
public class FingerprintDAO extends BaseDAO {
    private static final String SQL_COUNT = "SELECT COUNT(1) " +
            "FROM tbl_fingerprint WHERE timestamp >= ? AND timestamp <= ? AND status = 1";
    private static final String SQL_QUERY = "SELECT id, event, extra, timestamp " +
            "FROM tbl_fingerprint WHERE timestamp >= ? AND timestamp <= ? AND status = 1 ORDER BY timestamp DESC";
    private static final String SQL_INSERT = "INSERT INTO tbl_fingerprint(event, extra, timestamp) " +
            "VALUES (?, ?, ?)";

    /**
     * 统计
     *
     * @param minTimestamp 时间戳下限
     * @param maxTimestamp 时间戳上限
     * @return 统计结果
     */
    public int count(long minTimestamp, long maxTimestamp) {
        return super.count(SQL_COUNT, minTimestamp, maxTimestamp);
    }

    /**
     * 查询
     *
     * @param minTimestamp 时间戳下限
     * @param maxTimestamp 时间戳上限
     * @return 查询结果
     */
    public List<FingerprintDO> query(long minTimestamp, long maxTimestamp) {
        return super.selectBatch(FingerprintDO.class, SQL_QUERY, minTimestamp, maxTimestamp);
    }

    /**
     * 插入
     *
     * @param event 事件
     * @param extra 附加信息
     */
    public void insert(String event, String extra) {
        long timestamp = DateTimeUtil.getNowEpochSecond();
        super.insert(SQL_INSERT, event, extra, timestamp);
    }
}
