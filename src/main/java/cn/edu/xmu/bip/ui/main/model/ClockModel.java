/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.main.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 主界面-时钟
 *
 * @author luoxin
 * @version 2017-6-5
 */
public class ClockModel {
    /**
     * 日期
     */
    private StringProperty date;
    /**
     * 时间
     */
    private StringProperty time;
    /**
     * 时
     */
    private IntegerProperty hour;
    /**
     * 分
     */
    private IntegerProperty minute;
    /**
     * 秒
     */
    private IntegerProperty second;

    public ClockModel() {
        date = new SimpleStringProperty();
        time = new SimpleStringProperty();
        hour = new SimpleIntegerProperty();
        minute = new SimpleIntegerProperty();
        second = new SimpleIntegerProperty();
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

    public int getHour() {
        return hour.get();
    }

    public IntegerProperty hourProperty() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour.set(hour);
    }

    public int getMinute() {
        return minute.get();
    }

    public IntegerProperty minuteProperty() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute.set(minute);
    }

    public int getSecond() {
        return second.get();
    }

    public IntegerProperty secondProperty() {
        return second;
    }

    public void setSecond(int second) {
        this.second.set(second);
    }
}
