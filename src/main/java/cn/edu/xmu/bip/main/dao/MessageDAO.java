/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.main.dao;

import cn.edu.xmu.bip.main.domain.MessageDO;
import cn.edu.xmu.bip.common.exception.ClientException;
import cn.edu.xmu.bip.common.exception.ClientExceptionEnum;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 消息日志DAO
 *
 * @author luoxin
 * @version 2017-6-13
 */
public class MessageDAO extends BaseDAO {
    private static final String SQL_INSERT = "INSERT INTO tbl_message(type, body, timestamp) VALUES (?, ?, ?)";
    private static final String SQL_SELECT = "SELECT id, type, body, timestamp FROM tbl_message WHERE id = ? AND status = 0";
    private static final String SQL_SELECT_BY = "SELECT id, type, body, timestamp FROM tbl_message WHERE {0} AND status = 0 ORDER BY timestamp DESC";

    /**
     * 插入
     *
     * @param domain 试题
     * @throws SQLException 执行数据库操作时出错
     */
    public void insert(MessageDO domain) throws SQLException {
        int insertRowId = super.insert(SQL_INSERT, domain.getType(), domain.getBody(), domain.getTimestamp());
        if (insertRowId <= 0) {
            throw new ClientException(ClientExceptionEnum.DATABASE_INSERT_ERROR);
        }
    }

    /**
     * 按ID查询
     *
     * @param id ID
     * @return 查询结果
     * @throws SQLException 执行数据库操作时出错
     */
    public MessageDO select(int id) throws SQLException {
        MessageDO domain = super.select(MessageDO.class, SQL_SELECT, id);
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
    public List<MessageDO> selectBy(Map<String, Map<String, String>> conditions) throws SQLException {
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
        return super.selectBatch(MessageDO.class, SQL_SELECT_BY.replace("{0}", whereClause), bindArgs);
    }
}
