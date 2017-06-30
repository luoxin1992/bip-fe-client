/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.dao;

import cn.edu.xmu.bip.domain.ResourceDO;
import cn.edu.xmu.bip.exception.ClientException;
import cn.edu.xmu.bip.exception.ClientExceptionEnum;

import java.sql.SQLException;
import java.util.List;

/**
 * 资源DAO
 *
 * @author luoxin
 * @version 2017-6-13
 */
public class ResourceDAO extends BaseDAO {
    private static final String SQL_COUNT = "SELECT COUNT(1) FROM tbl_resource " +
            "WHERE timestamp > ? AND timestamp < ? AND status = 0";
    private static final String SQL_SELECT = "SELECT id, type, url, path, filename, md5, timestamp FROM tbl_resource " +
            "WHERE timestamp > ? AND timestamp < ? AND status = 0 ORDER BY timestamp DESC LIMIT ?, ?";
    private static final String SQL_SELECT_ID = "SELECT id FROM tbl_resource " +
            "WHERE type = ? AND filename = ? AND status = 0";
    private static final String SQL_INSERT = "INSERT INTO tbl_resource(type, url, path, filename, md5, timestamp) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE tbl_resource " +
            "SET type = ?, url = ?, path = ?, filename = ?, md5 = ?, timestamp = ? WHERE id = ? AND status = 0";
    private static final String SQL_DELETE = "UPDATE tbl_resource SET status = 1 WHERE id = ? AND status = 0";

    /**
     * 统计
     *
     * @param minTimestamp 时间戳下限
     * @param maxTimestamp 时间戳上限
     * @return 查询结果
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
    public List<ResourceDO> select(long minTimestamp, long maxTimestamp, int offset, int row) throws SQLException {
        return super.selectBatch(ResourceDO.class, SQL_SELECT, minTimestamp, maxTimestamp, offset, row);
    }

    /**
     * 查询ID
     *
     * @param type     类型
     * @param filename 文件名
     * @return ID
     * @throws SQLException 执行数据库操作时出错
     */
    public int selectId(String type, String filename) throws SQLException {
        ResourceDO domain = super.select(ResourceDO.class, SQL_SELECT_ID, type, filename);
        return domain != null ? domain.getId() : -1;
    }

    /**
     * 插入
     *
     * @param domain 实体
     * @throws SQLException 执行数据库操作时出错
     */
    public void insert(ResourceDO domain) throws SQLException {
        int id = super.insert(SQL_INSERT, domain.getType(), domain.getUrl(), domain.getPath(),
                domain.getFilename(), domain.getMd5(), domain.getTimestamp());
        if (id <= 0) {
            throw new ClientException(ClientExceptionEnum.DATABASE_INSERT_ERROR);
        }
    }

    /**
     * 更新
     *
     * @param domain 实体
     * @throws SQLException 执行数据库操作时出错
     */
    public void update(ResourceDO domain) throws SQLException {
        int rows = super.update(SQL_UPDATE, domain.getType(), domain.getUrl(), domain.getPath(),
                domain.getFilename(), domain.getMd5(), domain.getTimestamp(), domain.getId());
        if (rows <= 0) {
            throw new ClientException(ClientExceptionEnum.DATABASE_UPDATE_ERROR);
        }
    }

    /**
     * 逻辑删除
     *
     * @param id ID
     * @throws SQLException 执行数据库操作时出错
     */
    public void delete(int id) throws SQLException {
        int rowsAffect = super.update(SQL_DELETE, id);
        if (rowsAffect <= 0) {
            throw new ClientException(ClientExceptionEnum.DATABASE_DELETE_ERROR);
        }
    }
}
