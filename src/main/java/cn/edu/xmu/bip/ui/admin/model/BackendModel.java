/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.admin.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author luoxin
 * @version 2017-6-30
 */
public class BackendModel {
    private StringProperty env;
    private StringProperty api;
    private StringProperty ws;

    public BackendModel() {
        this.env = new SimpleStringProperty();
        this.api = new SimpleStringProperty();
        this.ws = new SimpleStringProperty();
    }

    public String getEnv() {
        return env.get();
    }

    public StringProperty envProperty() {
        return env;
    }

    public void setEnv(String env) {
        this.env.set(env);
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
}
