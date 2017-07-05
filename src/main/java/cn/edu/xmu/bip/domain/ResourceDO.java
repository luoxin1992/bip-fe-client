/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.domain;

import cn.com.lx1992.lib.client.base.domain.BaseDO;

/**
 * 资源Domain
 *
 * @author luoxin
 * @version 2017-6-12
 */
public class ResourceDO extends BaseDO {
    /**
     * 类型
     */
    private String type;
    /**
     * 下载地址
     */
    private String url;
    /**
     * 保存路径
     */
    private String path;
    /**
     * MD5
     */
    private String md5;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
