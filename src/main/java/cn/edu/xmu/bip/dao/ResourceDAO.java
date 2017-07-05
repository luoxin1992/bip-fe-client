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
            "FROM tbl_resource WHERE timestamp > ? AND timestamp < ? AND status != 0 ORDER BY timestamp DESC";
    private static final String SQL_SELECT = "SELECT id, type, url, path, md5, timestamp, status " +
            "FROM tbl_resource WHERE timestamp > ? AND timestamp < ? AND status != 0 ORDER BY timestamp DESC";
    private static final String SQL_SELECT_URL = "SELECT id, type, url, path, md5, timestamp, status " +
            "FROM tbl_resource WHERE url = ? AND status != 0 LIMIT 1";
    private static final String SQL_SELECT_STATUS = "SELECT id, type, url, path, md5, timestamp, status " +
            "FROM tbl_resource WHERE status = ?";
    private static final String SQL_INSERT = "INSERT INTO tbl_resource(type, url, path, md5, timestamp, status) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_STATUS = "UPDATE tbl_resource SET timestamp = ? AND status = ? " +
            "WHERE id = ? AND status != 0";

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
    public List<ResourceDO> select(long minTimestamp, long maxTimestamp) {
        return super.selectBatch(ResourceDO.class, SQL_SELECT, minTimestamp, maxTimestamp);
    }

    /**
     * 根据url查询
     *
     * @param url 下载地址
     * @return 查询结果
     */
    public ResourceDO selectUrl(String url) {
        return select(ResourceDO.class, SQL_SELECT_URL, url);
    }

    /**
     * 根据status查询
     *
     * @param status 状态
     * @return 查询结果
     */
    public List<ResourceDO> selectStatus(int status) {
        return selectBatch(ResourceDO.class, SQL_SELECT_STATUS, status);
    }

    /**
     * 插入
     *
     * @param type   类型
     * @param url    下载地址
     * @param path   保存路径
     * @param md5    MD5
     * @param status 状态
     */
    public void insert(String type, String url, String path, String md5, Integer status) {
        long timestamp = DateTimeUtil.getNowEpochSecond();
        super.insert(SQL_INSERT, type, url, path, md5, timestamp, status);
    }

    /**
     * 更新状态
     *
     * @param id     ID
     * @param status 状态
     */
    public void updateStatus(int id, int status) {
        long timestamp = DateTimeUtil.getNowEpochSecond();
        super.update(SQL_UPDATE_STATUS, timestamp, status, id);
    }
}
