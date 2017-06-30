/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.admin.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author luoxin
 * @version 2017-6-29
 */
public class ResourceModel {
    private IntegerProperty id;
    private StringProperty type;
    private StringProperty url;
    private StringProperty path;
    private StringProperty filename;
    private StringProperty md5;
    private StringProperty timestamp;

    public ResourceModel() {
        this.id = new SimpleIntegerProperty();
        this.type = new SimpleStringProperty();
        this.url = new SimpleStringProperty();
        this.path = new SimpleStringProperty();
        this.filename = new SimpleStringProperty();
        this.md5 = new SimpleStringProperty();
        this.timestamp = new SimpleStringProperty();
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getUrl() {
        return url.get();
    }

    public StringProperty urlProperty() {
        return url;
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public String getPath() {
        return path.get();
    }

    public StringProperty pathProperty() {
        return path;
    }

    public void setPath(String path) {
        this.path.set(path);
    }

    public String getFilename() {
        return filename.get();
    }

    public StringProperty filenameProperty() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename.set(filename);
    }

    public String getMd5() {
        return md5.get();
    }

    public StringProperty md5Property() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5.set(md5);
    }

    public String getTimestamp() {
        return timestamp.get();
    }

    public StringProperty timestampProperty() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp.set(timestamp);
    }
}
