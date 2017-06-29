/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.main.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 主界面-消息
 *
 * @author luoxin
 * @version 2017-6-5
 */
public class MessageModel {
    /**
     * 消息图片(URL)
     */
    private StringProperty image;
    /**
     * 附加内容
     */
    private StringProperty extra;

    public MessageModel() {
        image = new SimpleStringProperty();
        extra = new SimpleStringProperty();
    }

    public String getImage() {
        return image.get();
    }

    public StringProperty imageProperty() {
        return image;
    }

    public void setImage(String image) {
        this.image.set(image);
    }

    public String getExtra() {
        return extra.get();
    }

    public StringProperty extraProperty() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra.set(extra);
    }
}