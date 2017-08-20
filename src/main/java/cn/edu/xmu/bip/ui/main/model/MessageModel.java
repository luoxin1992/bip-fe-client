/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.main.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

/**
 * 主界面-消息
 *
 * @author luoxin
 * @version 2017-6-5
 */
public class MessageModel {
    /**
     * 消息图片
     */
    private ObjectProperty<Image> image;
    /**
     * 消息内容
     */
    private StringProperty content;

    public MessageModel() {
        image = new SimpleObjectProperty<>();
        content = new SimpleStringProperty();
    }

    public Image getImage() {
        return image.get();
    }

    public ObjectProperty<Image> imageProperty() {
        return image;
    }

    public void setImage(Image image) {
        this.image.set(image);
    }

    public String getContent() {
        return content.get();
    }

    public StringProperty contentProperty() {
        return content;
    }

    public void setContent(String content) {
        this.content.set(content);
    }
}