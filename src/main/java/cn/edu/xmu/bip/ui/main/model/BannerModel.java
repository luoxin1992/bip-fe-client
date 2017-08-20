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
 * 主界面-Banner
 *
 * @author luoxin
 * @version 2017-6-5
 */
public class BannerModel {
    /**
     * 公司LOGO
     */
    private ObjectProperty<Image> logo;
    /**
     * 公司名称
     */
    private StringProperty name;

    public BannerModel() {
        logo = new SimpleObjectProperty<>(new Image(ResourceConstant.DEFAULT_COMPANY_LOGO, true));
        name = new SimpleStringProperty();
    }

    public Image getLogo() {
        return logo.get();
    }

    public ObjectProperty<Image> logoProperty() {
        return logo;
    }

    public void setLogo(Image logo) {
        this.logo.set(logo);
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
}
