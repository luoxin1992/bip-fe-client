/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.dao;

import cn.com.lx1992.lib.client.util.DateTimeUtil;
import cn.edu.xmu.bip.domain.MessageLogDO;

import java.util.List;

/**
 * 消息日志DAO
 *
 * @author luoxin
 * @version 2017-6-13
 */
public class MessageLogDAO extends BaseDAO {
    private static final String SQL_COUNT = "SELECT COUNT(1) " +
            "FROM tbl_message_log WHERE timestamp > ? AND timestamp < ? AND status != 0";
    private static final String SQL_SELECT = "SELECT id, type, body, timestamp " +
            "FROM tbl_message_log WHERE timestamp > ? AND timestamp < ? AND status != 0 ORDER BY timestamp DESC";
    private static final String SQL_INSERT = "INSERT INTO tbl_message_log(type, body, timestamp) VALUES (?, ?, ?)";

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
    public List<MessageLogDO> select(long minTimestamp, long maxTimestamp) {
        return super.selectBatch(MessageLogDO.class, SQL_SELECT, minTimestamp, maxTimestamp);
    }

    /**
     * 插入
     *
     * @param type 类型
     * @param body 消息体
     */
    public void insert(String type, String body) {
        long timestamp = DateTimeUtil.getNowEpochSecond();
        super.insert(SQL_INSERT, type, body, timestamp);
    }
}
