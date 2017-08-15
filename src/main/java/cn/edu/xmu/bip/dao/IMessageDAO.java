/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.dao;

import cn.edu.xmu.bip.domain.MessageDO;

import java.util.List;

/**
 * 消息(日志)表DAO
 *
 * @author luoxin
 * @version 2017-6-13
 */
public interface IMessageDAO extends IBaseDAO {
    /**
     * 统计
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 统计结果
     */
    int count(long start, long end);

    /**
     * 查询
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 查询结果
     */
    List<MessageDO> query(long start, long end);

    /**
     * 查询单个
     *
     * @param uid UID
     * @return 查询结果
     */
    MessageDO selectOne(long uid);

    /**
     * 插入
     *
     * @param uid  UID
     * @param type 类型
     * @param body 消息体
     */
    void insert(Long uid, String type, String body);
}
