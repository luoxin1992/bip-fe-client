/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 主界面-窗口
 *
 * @author luoxin
 * @version 2017-6-5
 */
public class CounterModel {
    /**
     * 窗口编号
     */
    private StringProperty number;
    /**
     * 窗口名称
     */
    private StringProperty name;

    public CounterModel() {
        number = new SimpleStringProperty();
        name = new SimpleStringProperty();
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
}
