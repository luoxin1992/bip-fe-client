/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.main.model;

import cn.edu.xmu.bip.constant.ResourceConstant;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

/**
 * 主界面-用户信息
 *
 * @author luoxin
 * @version 2017-6-5
 */
public class UserModel {
    /**
     * 用户编号
     */
    private StringProperty number;
    /**
     * 用户姓名
     */
    private StringProperty name;
    /**
     * 用户照片
     */
    private ObjectProperty<Image> photo;

    public UserModel() {
        number = new SimpleStringProperty();
        name = new SimpleStringProperty();
        photo = new SimpleObjectProperty<>(new Image(ResourceConstant.DEFAULT_USER_PHOTO));
    }

    public String getNumber() {
        return number.get();
    }

    public StringProperty numberProperty() {
        return number;
    }

    public void setNumber(String number) {
        this.number.set(number);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public Image getPhoto() {
        return photo.get();
    }

    public ObjectProperty<Image> photoProperty() {
        return photo;
    }

    public void setPhoto(Image photo) {
        this.photo.set(photo);
    }
}
