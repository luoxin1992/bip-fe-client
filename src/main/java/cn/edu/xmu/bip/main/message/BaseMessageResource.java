/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.main.message;

import java.util.List;

/**
 * 消息中的资源
 * <p>
 * 文本
 * 图像
 * 声音
 *
 * @author luoxin
 * @version 2017-5-19
 */
public class BaseMessageResource {
    /**
     * 文本
     */
    private String text;
    /**
     * 图像(URI)
     */
    private String image;
    /**
     * 声音(URI)(将顺序播放)
     */
    private List<String> voices;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

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
