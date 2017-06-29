/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.main.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 任意界面-提示信息
 *
 * @author luoxin
 * @version 2017-6-26
 */
public class AlertModel {
    /**
     * 级别(信息/警告/错误)
     */
    private IntegerProperty level;
    /**
     * 提示内容
     */
    private StringProperty message;
    /**
     * 窗格关闭倒计时
     */
    private IntegerProperty countdown;

    public AlertModel() {
        this.level = new SimpleIntegerProperty();
        this.message = new SimpleStringProperty();
        this.countdown = new SimpleIntegerProperty();
    }

    public int getLevel() {
        return level.get();
    }

    public IntegerProperty levelProperty() {
        return level;
    }

    public void setLevel(int level) {
        this.level.set(level);
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

    public int getCountdown() {
        return countdown.get();
    }

    public IntegerProperty countdownProperty() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown.set(countdown);
    }
}
