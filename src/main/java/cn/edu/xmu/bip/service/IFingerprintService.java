/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.service;

/**
 * 指纹仪SDK Service
 * <p>
 * 指纹登记/辨识
 *
 * @author luoxin
 * @version 2017-7-25
 */
public interface IFingerprintService extends IBaseService {
    /**
     * 开始指纹登记
     * 如果SDK状态已经是‘登记’，则操作无效
     *
     * @param count 采集次数
     */
    void enroll(int count);

    /**
     * 开始指纹辨识
     * 如果SDK状态已经是‘辨识’，则操作无效
     */
    void identify();

    /**
     * 取消操作
     * 如果SDK状态已经是‘空闲’，则操作无效
     */
    void cancel();
}
