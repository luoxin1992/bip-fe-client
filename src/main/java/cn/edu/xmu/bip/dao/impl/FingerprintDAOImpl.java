/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.dao.impl;

import cn.edu.xmu.bip.dao.IFingerprintDAO;
import cn.edu.xmu.bip.domain.FingerprintDO;
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
public class FingerprintDAOImpl extends BaseDAOImpl implements IFingerprintDAO {
    private static final String SQL_INSERT = "INSERT INTO tbl_fingerprint(type, content, timestamp) VALUES (?, ?, ?)";
    private static final String SQL_SELECT = "SELECT id, type, content, timestamp FROM tbl_fingerprint WHERE id = ? AND status = 0";
    private static final String SQL_SELECT_BY = "SELECT id, type, content, timestamp FROM tbl_fingerprint WHERE {0} AND status = 0 ORDER BY timestamp DESC";

    @Override
    public void insert(FingerprintDO domain) throws SQLException {
        int insertRowId = super.insert(SQL_INSERT, domain.getType(), domain.getContent(), domain.getTimestamp());
        if (insertRowId <= 0) {
            throw new ClientException(ClientExceptionEnum.DATABASE_INSERT_ERROR);
        }
    }

    @Override
    public FingerprintDO select(int id) throws SQLException {
        FingerprintDO domain = super.select(FingerprintDO.class, SQL_SELECT, id);
        if (domain == null) {
            throw new ClientException(ClientExceptionEnum.DATABASE_SELECT_ERROR);
        }
        return domain;
    }

    public List<FingerprintDO> selectBy(Map<String, Map<String, String>> conditions) throws SQLException {
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
        return super.selectBatch(FingerprintDO.class, SQL_SELECT_BY.replace("{0}", whereClause), bindArgs);
    }
}
