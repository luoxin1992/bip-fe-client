/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.dao;

import cn.com.lx1992.lib.client.util.DateTimeUtil;
import cn.edu.xmu.bip.domain.PreferenceDO;

import java.util.List;

/**
 * 配置DAO
 *
 * @author luoxin
 * @version 2017-6-13
 */
public class PreferenceDAO extends BaseDAO {
    private static final String SQL_COUNT = "SELECT COUNT(1) " +
            "FROM tbl_preference WHERE timestamp >= ? AND timestamp <= ? AND status = 1";
    private static final String SQL_QUERY = "SELECT id, key, value, timestamp " +
            "FROM tbl_preference WHERE timestamp >= ? AND timestamp <= ? AND status = 1 ORDER BY timestamp DESC";
    private static final String SQL_SELECT_ONE = "SELECT id, key, value, timestamp " +
            "FROM tbl_preference WHERE key = ? AND status = 1 LIMIT 1";
    private static final String SQL_UPDATE = "UPDATE tbl_preference SET timestamp = ?, value = ? " +
            "WHERE key = ? AND status = 1";

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
    public List<PreferenceDO> query(long minTimestamp, long maxTimestamp) {
        return super.selectBatch(PreferenceDO.class, SQL_QUERY, minTimestamp, maxTimestamp);
    }

    /**
     * 查询单个
     *
     * @param key 键
     * @return 查询结果
     */
    public PreferenceDO selectOne(String key) {
        return select(PreferenceDO.class, SQL_SELECT_ONE, key);
    }

    /**
     * 更新
     *
     * @param key   键
     * @param value 值
     */
    public void update(String key, String value) {
        long timestamp = DateTimeUtil.getNowEpochSecond();
        super.update(SQL_UPDATE, timestamp, value, key);
    }
}
