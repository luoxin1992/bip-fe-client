/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.param;


import cn.com.lx1992.lib.client.base.param.BaseParam;

/**
 * @author luoxin
 * @version 2017-7-27
 */
public class SettingListParam extends BaseParam {
    /**
     * 设置组Key
     */
    private String parent;

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}
