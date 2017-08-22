/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.constant;

/**
 * 资源常量
 *
 * @author luoxin
 * @version 2017-7-28
 */
public class ResourceConstant {
    public static final String DEFAULT_USER_PHOTO = "/image/photo-unknown.png";
    public static final String DEFAULT_COMPANY_LOGO = "/image/company-unknown.png";
    public static final String DEFAULT_MESSAGE_IMAGE = "/image/message-unknown.png";

    public static final int INDEX_DEFAULT = 0;
    //一般业务(General Business)资源定义：0=等待结果/1=超时
    public static final int INDEX_GB_WAIT = 0;
    public static final int INDEX_GB_TIMEOUT = 1;
    //指纹登记(Fingerprint Enroll)消息资源定义：0=等待结果/1=出错/2=超时/3[~6]=等待操作(视采集次数)
    public static final int INDEX_FE_WAIT = 0;
    public static final int INDEX_FE_ERROR = 1;
    public static final int INDEX_FE_TIMEOUT = 2;
    public static final int INDEX_FE_1ST = 3;
    public static final int INDEX_FE_2ND = 4;
    public static final int INDEX_FE_3RD = 5;
    public static final int INDEX_FE_4TH = 6;
    //指纹辨识(Fingerprint Identify)消息资源定义：0=等待结果/1=出错/2=超时/3=等待操作
    public static final int INDEX_FI_WAIT = 0;
    public static final int INDEX_FI_ERROR = 1;
    public static final int INDEX_FI_TIMEOUT = 2;
    public static final int INDEX_FI = 3;

    private ResourceConstant() {
    }
}
