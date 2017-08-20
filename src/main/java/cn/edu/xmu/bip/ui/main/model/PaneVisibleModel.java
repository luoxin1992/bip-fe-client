/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.main.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * 窗格可视状态
 *
 * @author luoxin
 * @version 2017-6-26
 */
public class PaneVisibleModel {
    /**
     * 时钟
     */
    private BooleanProperty clockVisible;
    /**
     * 用户
     */
    private BooleanProperty userVisible;
    /**
     * 窗口
     */
    private BooleanProperty counterVisible;
    /**
     * 消息
     */
    private BooleanProperty messageVisible;

    public PaneVisibleModel() {
        this.clockVisible = new SimpleBooleanProperty(true);
        this.userVisible = new SimpleBooleanProperty(false);
        this.counterVisible = new SimpleBooleanProperty(true);
        this.messageVisible = new SimpleBooleanProperty(false);
    }

    public boolean getClockVisible() {
        return clockVisible.get();
    }

    public BooleanProperty clockVisibleProperty() {
        return clockVisible;
    }

    public void setClockVisible(boolean clockVisible) {
        this.clockVisible.set(clockVisible);
    }

    public boolean getUserVisible() {
        return userVisible.get();
    }

    public BooleanProperty userVisibleProperty() {
        return userVisible;
    }

    public void setUserVisible(boolean userVisible) {
        this.userVisible.set(userVisible);
    }

    public boolean getCounterVisible() {
        return counterVisible.get();
    }

    public BooleanProperty counterVisibleProperty() {
        return counterVisible;
    }

    public void setCounterVisible(boolean counterVisible) {
        this.counterVisible.set(counterVisible);
    }

    public boolean getMessageVisible() {
        return messageVisible.get();
    }

    public BooleanProperty messageVisibleProperty() {
        return messageVisible;
    }

    public void setMessageVisible(boolean messageVisible) {
        this.messageVisible.set(messageVisible);
    }
}
