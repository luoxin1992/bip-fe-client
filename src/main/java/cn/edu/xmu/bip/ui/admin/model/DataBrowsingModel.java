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
 * 数据浏览ViewModel
 *
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
    private StringProperty message;
    /**
     * 指纹(仪)(日志)表查询结果
     */
    private ObjectProperty<ObservableList<FingerprintModel>> fingerprints;
    /**
     * 消息(日志)表查询结果
     */
    private ObjectProperty<ObservableList<MessageModel>> messages;
    /**
     * 资源表查询结果
     */
    private ObjectProperty<ObservableList<ResourceModel>> resources;

    public DataBrowsingModel() {
        this.start = new SimpleObjectProperty<>();
        this.end = new SimpleObjectProperty<>();
        this.message = new SimpleStringProperty();

        this.fingerprints = new SimpleObjectProperty<>(FXCollections.observableArrayList());
        this.messages = new SimpleObjectProperty<>(FXCollections.observableArrayList());
        this.resources = new SimpleObjectProperty<>(FXCollections.observableArrayList());
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

    public String getMessage() {
        return message.get();
    }

    public StringProperty messageProperty() {
        return message;
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    public ObservableList<FingerprintModel> getFingerprints() {
        return fingerprints.get();
    }

    public ObjectProperty<ObservableList<FingerprintModel>> fingerprintsProperty() {
        return fingerprints;
    }

    public void setFingerprints(ObservableList<FingerprintModel> fingerprints) {
        this.fingerprints.set(fingerprints);
    }

    public ObservableList<MessageModel> getMessages() {
        return messages.get();
    }

    public ObjectProperty<ObservableList<MessageModel>> messagesProperty() {
        return messages;
    }

    public void setMessages(ObservableList<MessageModel> messages) {
        this.messages.set(messages);
    }

    public ObservableList<ResourceModel> getResources() {
        return resources.get();
    }

    public ObjectProperty<ObservableList<ResourceModel>> resourcesProperty() {
        return resources;
    }

    public void setResources(ObservableList<ResourceModel> resources) {
        this.resources.set(resources);
    }

    /**
     * 指纹(仪)(日志)表查询结果ViewModel
     */
    public static class FingerprintModel {
        /**
         * ID
         */
        private IntegerProperty id;
        /**
         * 类型
         */
        private StringProperty event;
        /**
         * 附加信息
         */
        private StringProperty extra;
        /**
         * 时间戳
         */
        private LongProperty timestamp;

        public FingerprintModel() {
            this.id = new SimpleIntegerProperty();
            this.event = new SimpleStringProperty();
            this.extra = new SimpleStringProperty();
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

        public String getEvent() {
            return event.get();
        }

        public StringProperty eventProperty() {
            return event;
        }

        public void setEvent(String event) {
            this.event.set(event);
        }

        public String getExtra() {
            return extra.get();
        }

        public StringProperty extraProperty() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra.set(extra);
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

    /**
     * 消息(日志)表查询结果ViewModel
     */
    public static class MessageModel {
        /**
         * ID
         */
        private IntegerProperty id;
        /**
         * UID
         */
        private LongProperty uid;
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

        public MessageModel() {
            this.id = new SimpleIntegerProperty();
            this.uid = new SimpleLongProperty();
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

        public long getUid() {
            return uid.get();
        }

        public LongProperty uidProperty() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid.set(uid);
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

    /**
     * 资源表查询结果ViewModel
     */
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
         * 文件大小
         */
        private LongProperty length;
        /**
         * 修改时间
         */
        private StringProperty modify;
        /**
         * 时间戳
         */
        private LongProperty timestamp;

        public ResourceModel() {
            this.id = new SimpleIntegerProperty();
            this.type = new SimpleStringProperty();
            this.url = new SimpleStringProperty();
            this.path = new SimpleStringProperty();
            this.length = new SimpleLongProperty();
            this.modify = new SimpleStringProperty();
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

        public long getLength() {
            return length.get();
        }

        public LongProperty lengthProperty() {
            return length;
        }

        public void setLength(Long length) {
            this.length.set(length);
        }

        public String getModify() {
            return modify.get();
        }

        public StringProperty modifyProperty() {
            return modify;
        }

        public void setModify(String modify) {
            this.modify.set(modify);
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
}