/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.dao.impl;

import cn.com.lx1992.lib.client.util.DateTimeUtil;
import cn.edu.xmu.bip.dao.IResourceDAO;
import cn.edu.xmu.bip.domain.ResourceDO;

import javax.annotation.PostConstruct;
import java.util.List;

public class ResourceDAOImpl extends BaseDAOImpl implements IResourceDAO {
    private static final String INIT_SQL_FILE = "/file/sql/tbl-resource.sql";

    private static final String SQL_COUNT = "SELECT COUNT(1) " +
            "FROM tbl_resource WHERE timestamp >= ? AND timestamp <= ? AND status = 1";
    private static final String SQL_QUERY = "SELECT id, type, url, path, length, modify, timestamp " +
            "FROM tbl_resource WHERE timestamp >= ? AND timestamp <= ? AND status = 1 ORDER BY id DESC";
    private static final String SQL_SELECT_ALL = "SELECT id, type, url, path, length, modify, timestamp " +
            "FROM tbl_resource WHERE status = 1";
    private static final String SQL_SELECT_ONE = "SELECT id, type, url, path, length, modify, timestamp " +
            "FROM tbl_resource WHERE type = ? AND url = ? AND status = 1 LIMIT 1";
    private static final String SQL_INSERT = "INSERT INTO tbl_resource(type, url, path, length, modify, timestamp) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE = "UPDATE tbl_resource SET timestamp = ?, status = 0 " +
            "WHERE id = ? AND status = 1";

    @PostConstruct
    private void initialize() {
        super.executeBatch(INIT_SQL_FILE);
    }

    @Override
    public int count(long start, long end) {
        return super.count(SQL_COUNT, start, end);
    }

    @Override
    public List<ResourceDO> query(long start, long end) {
        return super.selectBatch(ResourceDO.class, SQL_QUERY, start, end);
    }

    @Override
    public List<ResourceDO> selectAll() {
        return super.selectBatch(ResourceDO.class, SQL_SELECT_ALL);
    }

    @Override
    public ResourceDO selectOne(String type, String url) {
        return super.select(ResourceDO.class, SQL_SELECT_ONE, type, url);
    }

    @Override
    public void insert(String type, String url, String path, long length, String modify) {
        long timestamp = DateTimeUtil.getNowEpochSecond();
        super.insert(SQL_INSERT, type, url, path, length, modify, timestamp);
    }

    @Override
    public void delete(int id) {
        long timestamp = DateTimeUtil.getNowEpochSecond();
        super.update(SQL_DELETE, timestamp, id);
    }
}
