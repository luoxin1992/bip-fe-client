/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.dao.impl;

import cn.com.lx1992.lib.client.util.DateTimeUtil;
import cn.edu.xmu.bip.dao.IMessageDAO;
import cn.edu.xmu.bip.domain.MessageDO;

import javax.annotation.PostConstruct;
import java.util.List;

public class MessageDAOImpl extends BaseDAOImpl implements IMessageDAO {
    private static final String INIT_SQL_FILE = "/file/sql/tbl-message.sql";

    private static final String SQL_COUNT = "SELECT COUNT(1) " +
            "FROM tbl_message WHERE timestamp >= ? AND timestamp <= ? AND status = 1";
    private static final String SQL_QUERY = "SELECT id, uid, type, body, timestamp " +
            "FROM tbl_message WHERE timestamp >= ? AND timestamp <= ? AND status = 1 ORDER BY timestamp DESC";
    private static final String SQL_INSERT = "INSERT INTO tbl_message(uid, type, body, timestamp) " +
            "VALUES (?, ?, ?, ?)";

    @PostConstruct
    private void initialize() {
        super.executeBatch(INIT_SQL_FILE);
    }

    @Override
    public int count(long start, long end) {
        return super.count(SQL_COUNT, start, end);
    }

    @Override
    public List<MessageDO> query(long start, long end) {
        return super.selectBatch(MessageDO.class, SQL_QUERY, start, end);
    }

    @Override
    public void insert(Long uid, String type, String body) {
        long timestamp = DateTimeUtil.getNowEpochSecond();
        super.insert(SQL_INSERT, uid, type, body, timestamp);
    }
}
