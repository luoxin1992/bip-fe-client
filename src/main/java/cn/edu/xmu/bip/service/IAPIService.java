/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.service;

import cn.edu.xmu.bip.param.CounterCreateParam;
import cn.edu.xmu.bip.param.CounterModifyParam;
import cn.edu.xmu.bip.param.CounterQueryBindParam;
import cn.edu.xmu.bip.param.SessionApplyParam;
import cn.edu.xmu.bip.param.SettingListParam;
import cn.edu.xmu.bip.result.CounterQueryBindResult;
import cn.edu.xmu.bip.result.ResourceListResult;
import cn.edu.xmu.bip.result.SessionApplyResult;
import cn.edu.xmu.bip.result.SettingQueryResult;

/**
 * API Service
 *
 * @author luoxin
 * @version 2017-8-3
 */
public interface IAPIService extends IBaseService {
    /**
     * 获取URL前缀
     *
     * @return URL前缀
     */
    String getUrlPrefix();

    /**
     * 调用“存活检查”API
     */
    void invokeAlive();

    /**
     * 调用“查询窗口绑定”API
     *
     * @param param 请求参数
     * @return 响应结果
     */
    CounterQueryBindResult invokeCounterQueryBind(CounterQueryBindParam param);

    /**
     * 调用“新增窗口”API
     *
     * @param param 请求参数
     */
    void invokeCounterCreate(CounterCreateParam param);

    /**
     * 调用“修改窗口”API
     *
     * @param param 请求参数
     */
    void invokeCounterModify(CounterModifyParam param);

    /**
     * 调用“会话上线”API
     *
     * @param param 请求参数
     * @return 响应结果
     */
    SessionApplyResult invokeSessionApply(SessionApplyParam param);

    /**
     * 调用“查询全部资源”API
     *
     * @return 响应结果
     */
    ResourceListResult invokeResourceList();

    /**
     * 调用“查询全部设置”API
     *
     * @param param 请求参数
     * @return 响应结果
     */
    SettingQueryResult invokeSettingList(SettingListParam param);
}
