/*
 * Copyright © 2017 Xiamen University.All Rights Reserved.
 */
package cn.edu.xmu.bip.service;

/**
 * 消息服务
 * <p>
 * 接收消息处理
 * 指纹仪SDK回调入口
 *
 * @author luoxin
 * @version 2017-6-26
 */
public interface IMessageService extends IBaseService {
    /**
     * 处理接收到的消息
     *
     * @param body 消息体
     */
    void receive(String body);

    /**
     * 指纹登记(辨识) 指纹仪错误回调
     * <p>
     * 1.后置条件 状态=空闲
     * 2.回复 指纹仪错误
     * 3.播放声音和更新UI
     */
    void onFingerprintScannerError();

    /**
     * 指纹登记 手指按压回调
     * <p>
     * 1.播放声音和更新UI
     *
     * @param next 下次按压计数
     */
    void onFingerprintEnrollCount(int next);

    /**
     * 指纹登记 模板提取回调
     * <p>
     * 1.回复指纹登记
     * 2.播放声音和更新UI
     *
     * @param template 指纹模板
     */
    void onFingerprintEnrollTemplate(String template);

    /**
     * 指纹辨识 模板提取回调
     * <p>
     * 1.回复 指纹辨识
     * 2.播放声音和更新UI
     *
     * @param template 指纹模板
     */
    void onFingerprintIdentifyTemplate(String template);
}