/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.main.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 主界面-Banner
 *
 * @author luoxin
 * @version 2017-6-5
 */
public class BannerModel {
    /**
     * 公司LOGO(URL)
     */
    private StringProperty logo;
    /**
     * 公司名称
     */
    private StringProperty name;

    public BannerModel() {
        logo = new SimpleStringProperty();
        name = new SimpleStringProperty();
    }

    public String getLogo() {
        return logo.get();
    }

    public StringProperty logoProperty() {
        return logo;
    }

    public void setLogo(String logo) {
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
