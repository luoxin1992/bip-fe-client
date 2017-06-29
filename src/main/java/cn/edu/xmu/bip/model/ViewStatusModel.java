/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * 场景/窗格展示状态
 *
 * @author luoxin
 * @version 2017-6-26
 */
public class ViewStatusModel {
    /**
     * 启动界面
     */
    private BooleanProperty splashSceneHidden;
    /**
     * 主界面
     */
    private BooleanProperty mainSceneHidden;
    /**
     * 开发者界面
     */
    private BooleanProperty developerSceneHidden;
    /**
     * 时钟窗格
     */
    private BooleanProperty clockPaneHidden;
    /**
     * 客户信息窗格
     */
    private BooleanProperty userPaneHidden;
    /**
     * 窗口信息窗格
     */
    private BooleanProperty counterPaneHidden;
    /**
     * 消息窗格
     */
    private BooleanProperty messagePaneHidden;
    /**
     * 信息/警告/错误信息窗格
     */
    private BooleanProperty alertPaneHidden;

    public ViewStatusModel() {
        this.splashSceneHidden = new SimpleBooleanProperty();
        this.mainSceneHidden = new SimpleBooleanProperty();
        this.developerSceneHidden = new SimpleBooleanProperty();

        this.clockPaneHidden = new SimpleBooleanProperty();
        this.userPaneHidden = new SimpleBooleanProperty();
        this.counterPaneHidden = new SimpleBooleanProperty();
        this.messagePaneHidden = new SimpleBooleanProperty();
        this.alertPaneHidden = new SimpleBooleanProperty();
    }

    public boolean getSplashSceneHidden() {
        return splashSceneHidden.get();
    }

    public BooleanProperty splashSceneHiddenProperty() {
        return splashSceneHidden;
    }

    public void setSplashSceneHidden(boolean splashSceneHidden) {
        this.splashSceneHidden.set(splashSceneHidden);
    }

    public boolean isMainSceneHidden() {
        return mainSceneHidden.get();
    }

    public BooleanProperty mainSceneHiddenProperty() {
        return mainSceneHidden;
    }

    public void setMainSceneHidden(boolean mainSceneHidden) {
        this.mainSceneHidden.set(mainSceneHidden);
    }

    public boolean isDeveloperSceneHidden() {
        return developerSceneHidden.get();
    }

    public BooleanProperty developerSceneHiddenProperty() {
        return developerSceneHidden;
    }

    public void setDeveloperSceneHidden(boolean developerSceneHidden) {
        this.developerSceneHidden.set(developerSceneHidden);
    }

    public boolean isClockPaneHidden() {
        return clockPaneHidden.get();
    }

    public BooleanProperty clockPaneHiddenProperty() {
        return clockPaneHidden;
    }

    public void setClockPaneHidden(boolean clockPaneHidden) {
        this.clockPaneHidden.set(clockPaneHidden);
    }

    public boolean isUserPaneHidden() {
        return userPaneHidden.get();
    }

    public BooleanProperty userPaneHiddenProperty() {
        return userPaneHidden;
    }

    public void setUserPaneHidden(boolean userPaneHidden) {
        this.userPaneHidden.set(userPaneHidden);
    }

    public boolean isCounterPaneHidden() {
        return counterPaneHidden.get();
    }

    public BooleanProperty counterPaneHiddenProperty() {
        return counterPaneHidden;
    }

    public void setCounterPaneHidden(boolean counterPaneHidden) {
        this.counterPaneHidden.set(counterPaneHidden);
    }

    public boolean isMessagePaneHidden() {
        return messagePaneHidden.get();
    }

    public BooleanProperty messagePaneHiddenProperty() {
        return messagePaneHidden;
    }

    public void setMessagePaneHidden(boolean messagePaneHidden) {
        this.messagePaneHidden.set(messagePaneHidden);
    }

    public boolean getAlertPaneHidden() {
        return alertPaneHidden.get();
    }

    public BooleanProperty alertPaneHiddenProperty() {
        return alertPaneHidden;
    }

    public void setAlertPaneHidden(boolean alertPaneHidden) {
        this.alertPaneHidden.set(alertPaneHidden);
    }
}
