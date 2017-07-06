/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.admin.model;

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
     * 绑定网卡
     */
    private StringProperty nic;
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
    private StringProperty hint;
    /**
     * 本机网卡列表
     */
    private ObservableList<String> nicList;

    public ConfigCounterModel() {
        this.number = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.nic = new SimpleStringProperty();
        this.mac = new SimpleStringProperty();
        this.ip = new SimpleStringProperty();
        this.hint = new SimpleStringProperty();
        this.nicList = FXCollections.observableArrayList();
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

    public String getNic() {
        return nic.get();
    }

    public StringProperty nicProperty() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic.set(nic);
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

    public String getHint() {
        return hint.get();
    }

    public StringProperty hintProperty() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint.set(hint);
    }

    public ObservableList<String> getNicList() {
        return nicList;
    }

    public void setNicList(ObservableList<String> nicList) {
        this.nicList = nicList;
    }
}
