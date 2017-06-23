/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.dao;

import cn.edu.xmu.bip.domain.FingerprintDO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 指纹(扫描仪)日志DAO
 *
 * @author luoxin
 * @version 2017-6-13
 */
public interface IFingerprintDAO extends IBaseDAO {
    /**
     * 插入
     *
     * @param domain 实体
     * @throws SQLException 发生数据库错误
     */
    void insert(FingerprintDO domain) throws SQLException;

    /**
     * 按ID查询
     *
     * @param id ID
     * @return 查询结果
     * @throws SQLException 发生数据库错误
     */
    FingerprintDO select(int id) throws SQLException;

    /**
     * 条件查询
     *
     * @param conditions 查询条件
     * @return 查询结果
     * @throws SQLException 发生数据库错误
     */
    List<FingerprintDO> selectBy(Map<String, Map<String, String>> conditions) throws SQLException;
}
