/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.result;

import cn.com.lx1992.lib.client.base.result.BaseResult;

import java.util.List;

/**
 * @author luoxin
 * @version 2017-3-27
 */
public class SettingListResult extends BaseResult {
    /**
     * 设置项
     */
    private List<Item> item;

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }

    public static class Item extends BaseResult {
        /**
         * ID
         */
        private Long id;
        /**
         * 键
         */
        private String key;
        /**
         * 值
         */
        private String value;
        /**
         * 校验正则
         */
        private String regExp;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getRegExp() {
            return regExp;
        }

        public void setRegExp(String regExp) {
            this.regExp = regExp;
        }
    }
}
