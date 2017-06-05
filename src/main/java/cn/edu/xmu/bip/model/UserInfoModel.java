/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 主界面-客户信息
 *
 * @author luoxin
 * @version 2017-6-5
 */
public class UserInfoModel {
    /**
     * 客户编号
     */
    private StringProperty number;
    /**
     * 客户姓名
     */
    private StringProperty name;
    /**
     * 客户照片(URL)
     */
    private StringProperty photo;

    public UserInfoModel() {
        number = new SimpleStringProperty();
        name = new SimpleStringProperty();
        photo = new SimpleStringProperty();
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

    public String getPhoto() {
        return photo.get();
    }

    public StringProperty photoProperty() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo.set(photo);
    }
}
