/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.main.message;

import cn.edu.xmu.bip.main.meta.MessageTypeEnum;

/**
 * 更新用户信息Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class UserInfoMessage extends BaseMessage {
    /**
     * 用户编号
     */
    private String number;
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 用户照片(URL)
     */
    private String photo;
    /**
     * 资源
     */
    private BaseMessageResource resource;

    public UserInfoMessage() {
        super(MessageTypeEnum.USER_INFO);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public BaseMessageResource getResource() {
        return resource;
    }

    public void setResource(BaseMessageResource resource) {
        this.resource = resource;
    }
}
