/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.dao.impl;

import cn.com.lx1992.lib.client.util.DateTimeUtil;
import cn.edu.xmu.bip.dao.IFingerprintDAO;
import cn.edu.xmu.bip.domain.FingerprintDO;

import javax.annotation.PostConstruct;
import java.util.List;

public class FingerprintDAOImpl extends BaseDAOImpl implements IFingerprintDAO {
    private static final String INIT_SQL_FILE = "/file/sql/tbl-fingerprint.sql";

    private static final String SQL_COUNT = "SELECT COUNT(1) " +
            "FROM tbl_fingerprint WHERE timestamp >= ? AND timestamp <= ? AND status = 1";
    private static final String SQL_QUERY = "SELECT id, event, extra, timestamp " +
            "FROM tbl_fingerprint WHERE timestamp >= ? AND timestamp <= ? AND status = 1 ORDER BY id DESC";
    private static final String SQL_INSERT = "INSERT INTO tbl_fingerprint(event, extra, timestamp) " +
            "VALUES (?, ?, ?)";

    @PostConstruct
    private void initialize() {
        executeBatch(INIT_SQL_FILE);
    }

    @Override
    public int count(long start, long end) {
        return super.count(SQL_COUNT, start, end);
    }

    @Override
    public List<FingerprintDO> query(long start, long end) {
        return super.selectBatch(FingerprintDO.class, SQL_QUERY, start, end);
    }

    @Override
    public void insert(String event, String extra) {
        long timestamp = DateTimeUtil.getNowEpochSecond();
        super.insert(SQL_INSERT, event, extra, timestamp);
    }
}
