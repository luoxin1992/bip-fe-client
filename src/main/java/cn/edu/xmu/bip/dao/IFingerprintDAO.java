/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.dao;

import cn.edu.xmu.bip.domain.FingerprintDO;

import java.util.List;

/**
 * 指纹(日志)表DAO
 *
 * @author luoxin
 * @version 2017-6-13
 */
public interface IFingerprintDAO extends IBaseDAO {
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
    List<FingerprintDO> query(long start, long end);

    /**
     * 插入
     *
     * @param event 事件
     * @param extra 附加信息
     */
    void insert(String event, String extra);
}
