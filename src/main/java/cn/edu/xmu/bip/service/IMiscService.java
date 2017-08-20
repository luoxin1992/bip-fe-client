/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.service;

import javafx.concurrent.Task;

import java.util.Map;

/**
 * 杂项Service
 *
 * @author luoxin
 * @version 2017-6-21
 */
public interface IMiscService extends IBaseService {
    /**
     * 检查运行环境
     */
    Task<Void> checkRuntime();

    /**
     * 获取基础数据
     */
    Task<Map<String, Map<String,String>>> getBasicData();

    /**
     * 启动(主界面)时钟
     */
    void startClock();

    /**
     * 停止(主界面)时钟
     */
    void stopClock();
}
