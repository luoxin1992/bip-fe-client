/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.message;

/**
 * 更新客户信息Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class UpdateUserInfoMessage extends BaseReceiveMessage {
    /**
     * 编号
     */
    private String number;
    /**
     * 姓名
     */
    private String name;

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
}
