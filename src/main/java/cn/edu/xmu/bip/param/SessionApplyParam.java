/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.param;

import cn.com.lx1992.lib.client.base.param.BaseParam;

/**
 * @author luoxin
 * @version 2017-7-1
 */
public class SessionApplyParam extends BaseParam {
    /**
     * 窗口ID
     */
    private Long counterId;

    public Long getCounterId() {
        return counterId;
    }

    public void setCounterId(Long counterId) {
        this.counterId = counterId;
    }
}
