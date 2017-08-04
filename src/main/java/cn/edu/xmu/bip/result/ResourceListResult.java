/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.result;

import cn.com.lx1992.lib.client.base.result.BaseResult;

import java.util.List;

/**
 * @author luoxin
 * @version 2017-7-1
 */
public class ResourceListResult extends BaseResult {
    /**
     * 资源数量
     */
    private Integer total;
    /**
     * 资源列表
     */
    private List<Item> list;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Item> getList() {
        return list;
    }

    public void setList(List<Item> list) {
        this.list = list;
    }

    public static class Item extends BaseResult {
        /**
         * ID
         */
        private Long id;
        /**
         * 类型
         */
        private String type;
        /**
         * URL
         */
        private String url;
        /**
         * MD5
         */
        private String md5;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

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

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }
    }
}


