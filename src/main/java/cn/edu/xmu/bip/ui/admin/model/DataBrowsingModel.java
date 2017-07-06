/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.admin.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

/**
 * @author luoxin
 * @version 2017-7-3
 */
public class DataBrowsingModel {
    /**
     * 开始日期
     */
    private ObjectProperty<LocalDate> start;
    /**
     * 结束日期
     */
    private ObjectProperty<LocalDate> end;
    /**
     * 提示信息
     */
    private StringProperty hint;
    /**
     * 指纹(扫描仪)日志表查询结果
     */
    private ObservableList<FingerprintLogModel> fingerprintLogs;
    /**
     * 消息日志表查询结果
     */
    private ObservableList<MessageLogModel> messageLogs;
    /**
     * 资源表查询结果
     */
    private ObservableList<ResourceModel> resources;

    public DataBrowsingModel() {
        this.start = new SimpleObjectProperty<>();
        this.end = new SimpleObjectProperty<>();
        this.hint = new SimpleStringProperty();

        this.fingerprintLogs = FXCollections.observableArrayList();
        this.messageLogs = FXCollections.observableArrayList();
        this.resources = FXCollections.observableArrayList();
    }

    public LocalDate getStart() {
        return start.get();
    }

    public ObjectProperty<LocalDate> startProperty() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start.set(start);
    }

    public LocalDate getEnd() {
        return end.get();
    }

    public ObjectProperty<LocalDate> endProperty() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end.set(end);
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

    public ObservableList<FingerprintLogModel> getFingerprintLogs() {
        return fingerprintLogs;
    }

    public void setFingerprintLogs(ObservableList<FingerprintLogModel> fingerprintLogs) {
        this.fingerprintLogs = fingerprintLogs;
    }

    public ObservableList<MessageLogModel> getMessageLogs() {
        return messageLogs;
    }

    public void setMessageLogs(ObservableList<MessageLogModel> messageLogs) {
        this.messageLogs = messageLogs;
    }

    public ObservableList<ResourceModel> getResources() {
        return resources;
    }

    public void setResources(ObservableList<ResourceModel> resources) {
        this.resources = resources;
    }

    public static class FingerprintLogModel {
        /**
         * ID
         */
        private IntegerProperty id;
        /**
         * 类型
         */
        private StringProperty type;
        /**
         * 内容
         */
        private StringProperty content;
        /**
         * 时间戳
         */
        private LongProperty timestamp;

        public FingerprintLogModel() {
            this.id = new SimpleIntegerProperty();
            this.type = new SimpleStringProperty();
            this.content = new SimpleStringProperty();
            this.timestamp = new SimpleLongProperty();
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

        public String getContent() {
            return content.get();
        }

        public StringProperty contentProperty() {
            return content;
        }

        public void setContent(String content) {
            this.content.set(content);
        }

        public Long getTimestamp() {
            return timestamp.get();
        }

        public LongProperty timestampProperty() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp.set(timestamp);
        }
    }

    public static class MessageLogModel {
        /**
         * ID
         */
        private IntegerProperty id;
        /**
         * 类型
         */
        private StringProperty type;
        /**
         * 消息体
         */
        private StringProperty body;
        /**
         * 时间戳
         */
        private LongProperty timestamp;

        public MessageLogModel() {
            this.id = new SimpleIntegerProperty();
            this.type = new SimpleStringProperty();
            this.body = new SimpleStringProperty();
            this.timestamp = new SimpleLongProperty();
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

        public String getBody() {
            return body.get();
        }

        public StringProperty bodyProperty() {
            return body;
        }

        public void setBody(String body) {
            this.body.set(body);
        }

        public Long getTimestamp() {
            return timestamp.get();
        }

        public LongProperty timestampProperty() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp.set(timestamp);
        }
    }

    public static class ResourceModel {
        /**
         * ID
         */
        private IntegerProperty id;
        /**
         * 类型
         */
        private StringProperty type;
        /**
         * 下载地址
         */
        private StringProperty url;
        /**
         * 保存路径
         */
        private StringProperty path;
        /**
         * MD5
         */
        private StringProperty md5;
        /**
         * 时间戳
         */
        private LongProperty timestamp;
        /**
         * 状态
         */
        private IntegerProperty status;

        public ResourceModel() {
            this.id = new SimpleIntegerProperty();
            this.type = new SimpleStringProperty();
            this.url = new SimpleStringProperty();
            this.path = new SimpleStringProperty();
            this.md5 = new SimpleStringProperty();
            this.timestamp = new SimpleLongProperty();
            this.status = new SimpleIntegerProperty();
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

        public String getMd5() {
            return md5.get();
        }

        public StringProperty md5Property() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5.set(md5);
        }

        public Long getTimestamp() {
            return timestamp.get();
        }

        public LongProperty timestampProperty() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp.set(timestamp);
        }

        public int getStatus() {
            return status.get();
        }

        public IntegerProperty statusProperty() {
            return status;
        }

        public void setStatus(int status) {
            this.status.set(status);
        }
    }
}