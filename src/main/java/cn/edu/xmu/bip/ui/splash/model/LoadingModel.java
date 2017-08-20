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
     * 提示
     */
    private StringProperty message;
    /**
     * 进度
     */
    private StringProperty progress;

    public LoadingModel() {
        message = new SimpleStringProperty();
        progress = new SimpleStringProperty();
    }

    public String getMessage() {
        return message.get();
    }

    public StringProperty messageProperty() {
        return message;
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    public String getProgress() {
        return progress.get();
    }

    public StringProperty progressProperty() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress.set(progress);
    }
}
