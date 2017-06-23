/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.dao;

import cn.edu.xmu.bip.domain.ResourceDO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 资源DAO
 *
 * @author luoxin
 * @version 2017-6-13
 */
public interface IResourceDAO extends IBaseDAO {
    /**
     * 插入
     *
     * @param domain 实体
     * @throws SQLException 发生数据库错误
     */
    void insert(ResourceDO domain) throws SQLException;

    /**
     * 逻辑删除
     *
     * @param id ID
     * @throws SQLException 发生数据库错误
     */
    void delete(int id) throws SQLException;

    /**
     * 按ID查询
     *
     * @param id ID
     * @return 查询结果
     * @throws SQLException 发生数据库错误
     */
    ResourceDO select(int id) throws SQLException;

    /**
     * 条件查询
     *
     * @param conditions 查询条件
     * @return 查询结果
     * @throws SQLException 发生数据库错误
     */
    List<ResourceDO> selectBy(Map<String, Map<String, String>> conditions) throws SQLException;

    /**
     * 查询全部
     *
     * @return 查询结果
     * @throws SQLException 发生数据库错误
     */
    List<ResourceDO> selectAll() throws SQLException;
}
