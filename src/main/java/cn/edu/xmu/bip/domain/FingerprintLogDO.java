/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.domain;

import cn.com.lx1992.lib.client.base.domain.BaseDO;

/**
 * 指纹(扫描仪)日志Domain
 *
 * @author luoxin
 * @version 2017-6-12
 */
public class FingerprintLogDO extends BaseDO {
    /**
     * 类型
     */
    private String type;
    /**
     * 内容
     */
    private String content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
