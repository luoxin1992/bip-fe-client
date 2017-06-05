/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 主界面-日期时间
 *
 * @author luoxin
 * @version 2017-6-5
 */
public class DateTimeModel {
    /**
     * 日期
     */
    private StringProperty date;
    /**
     * 时间
     */
    private StringProperty time;

    public DateTimeModel() {
        date = new SimpleStringProperty();
        time = new SimpleStringProperty();
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getTime() {
        return time.get();
    }

    public StringProperty timeProperty() {
        return time;
    }

    public void setTime(String time) {
        this.time.set(time);
    }
}
