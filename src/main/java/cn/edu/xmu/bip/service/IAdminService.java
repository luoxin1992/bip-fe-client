/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.service;

/**
 * 管理工具Service
 * <p>
 * 参数配置-后端环境
 * 参数配置-窗口信息
 * 数据浏览
 *
 * @author luoxin
 * @version 2017-8-2
 */
public interface IAdminService extends IBaseService {
    /**
     * 获取当前后端环境
     */
    void getCurrentBackend();

    /**
     * 获取预置后端环境
     *
     * @param preset 预置环境
     */
    void getPresetBackend(String preset);

    /**
     * 保存后端环境
     */
    void saveBackend();

    /**
     * 选择绑定网卡
     */
    void selectNic();

    /**
     * 刷新网卡列表
     */
    void refreshNicList();

    /**
     * 查询窗口信息
     */
    void queryCounter();

    /**
     * 提交窗口信息
     */
    void submitCounter();

    /**
     * 查询指纹(仪)(日志)表
     */
    void queryFingerprint();

    /**
     * 查询消息(日志)表
     */
    void queryMessage();

    /**
     * 查询资源表
     */
    void queryResource();
}
