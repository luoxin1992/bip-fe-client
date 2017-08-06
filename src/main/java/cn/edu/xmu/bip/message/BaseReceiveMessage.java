/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.message;

import java.util.List;

/**
 * 发送的消息基类
 *
 * @author luoxin
 * @version 2017-5-19
 */
public class BaseReceiveMessage {
    /**
     * 消息ID
     */
    private Long uid;
    /**
     * 类型
     */
    private String type;
    /**
     * 资源
     */
    private List<Resource> resources;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public static class Resource {
        /**
         * 图像
         */
        private String image;
        /**
         * 声音(顺序播放列表)
         */
        private List<String> voices;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public List<String> getVoices() {
            return voices;
        }

        public void setVoices(List<String> voices) {
            this.voices = voices;
        }
    }
}
