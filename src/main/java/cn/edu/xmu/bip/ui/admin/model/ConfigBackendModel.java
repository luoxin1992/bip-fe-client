/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.admin.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 参数配置-后端环境
 *
 * @author luoxin
 * @version 2017-6-30
 */
public class ConfigBackendModel {
    /**
     * API
     */
    private StringProperty api;
    /**
     * WebSocket
     */
    private StringProperty ws;
    /**
     * 提示信息
     */
    private StringProperty hint;

    public ConfigBackendModel() {
        this.api = new SimpleStringProperty();
        this.ws = new SimpleStringProperty();
        this.hint = new SimpleStringProperty();
    }

    public String getApi() {
        return api.get();
    }

    public StringProperty apiProperty() {
        return api;
    }

    public void setApi(String api) {
        this.api.set(api);
    }

    public String getWs() {
        return ws.get();
    }

    public StringProperty wsProperty() {
        return ws;
    }

    public void setWs(String ws) {
        this.ws.set(ws);
    }

    public String getHint() {
        return hint.get();
    }

    public StringProperty hintProperty() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint.set(hint);
    }
}
