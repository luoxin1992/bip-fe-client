/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.splash.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 启动界面-加载状态
 *
 * @author luoxin
 * @version 2017-6-5
 */
public class LoadingModel {
    /**
     * 加载状态
     */
    private StringProperty status;

    public LoadingModel() {
        status = new SimpleStringProperty();
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}
