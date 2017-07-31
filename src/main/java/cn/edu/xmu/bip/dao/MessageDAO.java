/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.dao;

import cn.com.lx1992.lib.client.util.DateTimeUtil;
import cn.edu.xmu.bip.domain.MessageDO;

import java.util.List;

/**
 * 消息DAO
 *
 * @author luoxin
 * @version 2017-6-13
 */
public class MessageDAO extends BaseDAO {
    private static final String SQL_COUNT = "SELECT COUNT(1) " +
            "FROM tbl_message WHERE timestamp >= ? AND timestamp <= ? AND status = 1";
    private static final String SQL_QUERY = "SELECT id, uid, type, body, timestamp " +
            "FROM tbl_message WHERE timestamp >= ? AND timestamp <= ? AND status = 1 ORDER BY timestamp DESC";
    private static final String SQL_INSERT = "INSERT INTO tbl_message(uid, type, body, timestamp) " +
            "VALUES (?, ?, ?, ?)";

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
    public List<MessageDO> query(long minTimestamp, long maxTimestamp) {
        return super.selectBatch(MessageDO.class, SQL_QUERY, minTimestamp, maxTimestamp);
    }

    /**
     * 插入
     *
     * @param uid  UID
     * @param type 类型
     * @param body 消息体
     */
    public void insert(Long uid, String type, String body) {
        long timestamp = DateTimeUtil.getNowEpochSecond();
        super.insert(SQL_INSERT, uid, type, body, timestamp);
    }
}
