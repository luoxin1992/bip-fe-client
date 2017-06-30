/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.message;

import cn.edu.xmu.bip.meta.MessageTypeEnum;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.lang.reflect.Field;

/**
 * 消息基类
 *
 * @author luoxin
 * @version 2017-5-19
 */
public class BaseMessage {
    /**
     * (回复)消息ID
     */
    private Long id;
    /**
     * 类型
     */
    private String type;

    public BaseMessage() {
    }

    public BaseMessage(MessageTypeEnum type) {
        this.type = type.getType();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        //自定义ToStringBuilder过滤null字段
        return new ReflectionToStringBuilder(this, ToStringStyle.JSON_STYLE) {
            @Override
            protected boolean accept(Field field) {
                boolean isNull = false;
                try {
                    isNull = field.get(BaseMessage.this) == null;
                } catch (IllegalAccessException ignored) {
                }
                return !isNull && super.accept(field);
            }
        }.toString();
    }
}
