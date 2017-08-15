/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.admin.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * 参数配置-窗口信息
 *
 * @author luoxin
 * @version 2017-6-30
 */
public class ConfigCounterModel {
    /**
     * 编号
     */
    private StringProperty number;
    /**
     * 名称
     */
    private StringProperty name;
    /**
     * 本机网卡列表
     */
    private ObjectProperty<ObservableList<String>> nicList;
    /**
     * 绑定网卡
     */
    private StringProperty nicCurrent;
    /**
     * MAC地址
     */
    private StringProperty mac;
    /**
     * IP地址
     */
    private StringProperty ip;
    /**
     * 提示信息
     */
    private StringProperty message;

    public ConfigCounterModel() {
        this.number = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.nicList = new SimpleObjectProperty<>(FXCollections.observableArrayList());
        this.nicCurrent = new SimpleStringProperty();
        this.mac = new SimpleStringProperty();
        this.ip = new SimpleStringProperty();
        this.message = new SimpleStringProperty();
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

    public ObservableList<String> getNicList() {
        return nicList.get();
    }

    public ObjectProperty<ObservableList<String>> nicListProperty() {
        return nicList;
    }

    public void setNicList(ObservableList<String> nicList) {
        this.nicList.set(nicList);
    }

    public String getNicCurrent() {
        return nicCurrent.get();
    }

    public StringProperty nicCurrentProperty() {
        return nicCurrent;
    }

    public void setNicCurrent(String nicCurrent) {
        this.nicCurrent.set(nicCurrent);
    }

    public String getMac() {
        return mac.get();
    }

    public StringProperty macProperty() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac.set(mac);
    }

    public String getIp() {
        return ip.get();
    }

    public StringProperty ipProperty() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip.set(ip);
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
}
