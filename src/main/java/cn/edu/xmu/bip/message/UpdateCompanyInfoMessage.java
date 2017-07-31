/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.message;

/**
 * 更新公司信息Message
 *
 * @author luoxin
 * @version 2017-4-29
 */
public class UpdateCompanyInfoMessage extends BaseReceiveMessage {
    /**
     * 名称
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
