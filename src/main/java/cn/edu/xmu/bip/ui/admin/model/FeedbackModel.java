/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.admin.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

/**
 * 反馈问题
 *
 * @author luoxin
 * @version 2017-6-30
 */
public class FeedbackModel {
    /**
     * 界面截图
     */
    private ObjectProperty<Image> screenshot;
    /**
     * 运行日志
     */
    private StringProperty log;
    /**
     * 意见建议
     */
    private StringProperty suggestion;

    public FeedbackModel() {
        this.screenshot = new SimpleObjectProperty<>();
        this.log = new SimpleStringProperty();
        this.suggestion = new SimpleStringProperty();
    }

    public Image getScreenshot() {
        return screenshot.get();
    }

    public ObjectProperty<Image> screenshotProperty() {
        return screenshot;
    }

    public void setScreenshot(Image screenshot) {
        this.screenshot.set(screenshot);
    }

    public String getLog() {
        return log.get();
    }

    public StringProperty logProperty() {
        return log;
    }

    public void setLog(String log) {
        this.log.set(log);
    }

    public String getSuggestion() {
        return suggestion.get();
    }

    public StringProperty suggestionProperty() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion.set(suggestion);
    }
}
