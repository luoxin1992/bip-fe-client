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
     * 文件大小
     */
    private Long length;
    /**
     * 修改时间
     */
    private String modify;

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

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public String getModify() {
        return modify;
    }

    public void setModify(String modify) {
        this.modify = modify;
    }
}
