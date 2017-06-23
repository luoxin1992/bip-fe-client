/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.dao.impl;

import cn.edu.xmu.bip.dao.IResourceDAO;
import cn.edu.xmu.bip.domain.ResourceDO;
import cn.edu.xmu.bip.exception.ClientException;
import cn.edu.xmu.bip.exception.ClientExceptionEnum;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author luoxin
 * @version 2017-6-13
 */
public class ResourceDAOImpl extends BaseDAOImpl implements IResourceDAO {
    private static final String SQL_INSERT = "INSERT INTO tbl_resource(type, url, path, filename, md5, timestamp) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE = "UPDATE tbl_resource SET status = 1 WHERE id = ? AND status = 0";
    private static final String SQL_SELECT = "SELECT id, type, url, path, filename, md5, timestamp FROM tbl_resource WHERE id = ? AND status = 0";
    private static final String SQL_SELECT_BY = "SELECT id, type, url, path, filename, md5, timestamp FROM tbl_resource WHERE {0} AND status = 0 ORDER BY timestamp DESC";
    private static final String SQL_SELECT_ALL = "SELECT id, type, url, path, filename, md5, timestamp FROM tbl_resource WHERE status = 0 ORDER BY timestamp DESC";

    @Override
    public void insert(ResourceDO domain) throws SQLException {
        int insertRowId = super.insert(SQL_INSERT, domain.getType(), domain.getUrl(), domain.getPath(),
                domain.getFilename(), domain.getMd5(), domain.getTimestamp());
        if (insertRowId <= 0) {
            throw new ClientException(ClientExceptionEnum.DATABASE_INSERT_ERROR);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        int rowsAffect = super.update(SQL_DELETE, id);
        if (rowsAffect <= 0) {
            throw new ClientException(ClientExceptionEnum.DATABASE_DELETE_ERROR);
        }
    }

    @Override
    public ResourceDO select(int id) throws SQLException {
        ResourceDO domain = super.select(ResourceDO.class, SQL_SELECT, id);
        if (domain == null) {
            throw new ClientException(ClientExceptionEnum.DATABASE_SELECT_ERROR);
        }
        return domain;
    }

    @Override
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

    @Override
    public List<ResourceDO> selectAll() throws SQLException {
        return super.selectBatch(ResourceDO.class, SQL_SELECT_ALL);
    }
}
