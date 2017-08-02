/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.dao;

import cn.edu.xmu.bip.domain.ResourceDO;

import java.util.List;

/**
 * 资源表DAO
 *
 * @author luoxin
 * @version 2017-6-13
 */
public interface IResourceDAO extends IBaseDAO {
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
    List<ResourceDO> query(long start, long end);

    /**
     * 查询全部
     *
     * @return 查询结果
     */
    List<ResourceDO> selectAll();

    /**
     * 查询单个
     *
     * @param type 类型
     * @param url  下载地址
     * @return 查询结果
     */
    ResourceDO selectOne(String type, String url);

    /**
     * 插入
     *
     * @param type   类型
     * @param url    下载地址
     * @param path   保存路径
     * @param length 文件大小
     * @param modify 修改时间
     */
    void insert(String type, String url, String path, long length, String modify);

    /**
     * 删除
     *
     * @param id ID
     */
    void delete(int id);
}
