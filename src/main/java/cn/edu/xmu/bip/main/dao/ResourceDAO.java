/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.main.dao;

import cn.edu.xmu.bip.main.domain.ResourceDO;
import cn.edu.xmu.bip.common.exception.ClientException;
import cn.edu.xmu.bip.common.exception.ClientExceptionEnum;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 资源DAO
 *
 * @author luoxin
 * @version 2017-6-13
 */
public class ResourceDAO extends BaseDAO {
    private static final String SQL_INSERT = "INSERT INTO tbl_resource(type, url, path, filename, md5, timestamp) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE = "UPDATE tbl_resource SET status = 1 WHERE id = ? AND status = 0";
    private static final String SQL_SELECT = "SELECT id, type, url, path, filename, md5, timestamp FROM tbl_resource WHERE id = ? AND status = 0";
    private static final String SQL_SELECT_BY = "SELECT id, type, url, path, filename, md5, timestamp FROM tbl_resource WHERE {0} AND status = 0 ORDER BY timestamp DESC";
    private static final String SQL_SELECT_ALL = "SELECT id, type, url, path, filename, md5, timestamp FROM tbl_resource WHERE status = 0 ORDER BY timestamp DESC";

    /**
     * 插入
     *
     * @param domain 实体
     * @throws SQLException 执行数据库操作时出错
     */
    public void insert(ResourceDO domain) throws SQLException {
        int insertRowId = super.insert(SQL_INSERT, domain.getType(), domain.getUrl(), domain.getPath(),
                domain.getFilename(), domain.getMd5(), domain.getTimestamp());
        if (insertRowId <= 0) {
            throw new ClientException(ClientExceptionEnum.DATABASE_INSERT_ERROR);
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

    /**
     * 按ID查询
     *
     * @param id ID
     * @return 查询结果
     * @throws SQLException 执行数据库操作时出错
     */
    public ResourceDO select(int id) throws SQLException {
        ResourceDO domain = super.select(ResourceDO.class, SQL_SELECT, id);
        if (domain == null) {
            throw new ClientException(ClientExceptionEnum.DATABASE_SELECT_ERROR);
        }
        return domain;
    }

    /**
     * 条件查询
     *
     * @param conditions 查询条件
     * @return 查询结果
     * @throws SQLException 执行数据库操作时出错
     */
    public List<ResourceDO> selectBy(Map<String, Map<String, String>> conditions) throws SQLException {
        String whereClause = conditions.entrySet().stream()
                .flatMap(entry -> entry.getValue().keySet().stream()
                        .map(operator -> entry.getKey() + " " + operator + " ?")
                        .collect(Collectors.toList())
                        .stream())
                .collect(Collectors.joining(" AND "));
        Object[] bindArgs = conditions.values()
                .stream()
                .flatMap(entry -> entry.values().stream())
                .toArray();
        return super.selectBatch(ResourceDO.class, SQL_SELECT_BY.replace("{0}", whereClause), bindArgs);
    }

    /**
     * 查询全部
     *
     * @return 查询结果
     * @throws SQLException 执行数据库操作时出错
     */
    public List<ResourceDO> selectAll() throws SQLException {
        return super.selectBatch(ResourceDO.class, SQL_SELECT_ALL);
    }
}
