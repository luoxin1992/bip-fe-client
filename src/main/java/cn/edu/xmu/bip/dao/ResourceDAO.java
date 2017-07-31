/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.dao;

import cn.com.lx1992.lib.client.util.DateTimeUtil;
import cn.edu.xmu.bip.domain.ResourceDO;

import java.util.List;

/**
 * 资源DAO
 *
 * @author luoxin
 * @version 2017-6-13
 */
public class ResourceDAO extends BaseDAO {
    private static final String SQL_COUNT = "SELECT COUNT(1) " +
            "FROM tbl_resource WHERE timestamp >= ? AND timestamp <= ? AND status = 1";
    private static final String SQL_QUERY = "SELECT id, type, url, path, length, modify, timestamp " +
            "FROM tbl_resource WHERE timestamp >= ? AND timestamp <= ? AND status = 1 ORDER BY timestamp DESC";
    private static final String SQL_SELECT_ALL = "SELECT id, type, url, path, length, modify, timestamp " +
            "FROM tbl_resource WHERE status = 1";
    private static final String SQL_SELECT_ONE = "SELECT id, type, url, path, length, modify, timestamp " +
            "FROM tbl_resource WHERE type = ? AND url = ? AND status = 1 LIMIT 1";
    private static final String SQL_INSERT = "INSERT INTO tbl_resource(type, url, path, length, modify, timestamp) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE = "UPDATE tbl_resource SET timestamp = ?, status = 0 " +
            "WHERE id = ? AND status = 1";

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
    public List<ResourceDO> query(long minTimestamp, long maxTimestamp) {
        return super.selectBatch(ResourceDO.class, SQL_QUERY, minTimestamp, maxTimestamp);
    }

    /**
     * 查询全部
     *
     * @return 查询结果
     */
    public List<ResourceDO> selectAll() {
        return super.selectBatch(ResourceDO.class, SQL_SELECT_ALL);
    }

    /**
     * 查询单个
     *
     * @param type 类型
     * @param url  下载地址
     * @return 查询结果
     */
    public ResourceDO selectOne(String type, String url) {
        return super.select(ResourceDO.class, SQL_SELECT_ONE, type, url);
    }

    /**
     * 插入
     *
     * @param type   类型
     * @param url    下载地址
     * @param path   保存路径
     * @param length 文件大小
     * @param modify 修改时间
     */
    public void insert(String type, String url, String path, long length, String modify) {
        long timestamp = DateTimeUtil.getNowEpochSecond();
        super.insert(SQL_INSERT, type, url, path, length, modify, timestamp);
    }

    /**
     * 删除
     *
     * @param id ID
     */
    public void delete(int id) {
        long timestamp = DateTimeUtil.getNowEpochSecond();
        super.update(SQL_DELETE, timestamp, id);
    }
}
