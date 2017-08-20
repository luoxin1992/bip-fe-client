/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.dao.factory;

import cn.edu.xmu.bip.dao.IBaseDAO;
import cn.edu.xmu.bip.dao.IFingerprintDAO;
import cn.edu.xmu.bip.dao.IMessageDAO;
import cn.edu.xmu.bip.dao.IResourceDAO;
import cn.edu.xmu.bip.dao.impl.FingerprintDAOImpl;
import cn.edu.xmu.bip.dao.impl.MessageDAOImpl;
import cn.edu.xmu.bip.dao.impl.ResourceDAOImpl;
import cn.edu.xmu.bip.exception.ClientException;
import cn.edu.xmu.bip.exception.ClientExceptionEnum;
import cn.edu.xmu.bip.inject.Implement;

import javax.inject.Inject;

/**
 * DAO工厂
 *
 * @author luoxin
 * @version 2017-6-13
 */
public class DAOFactory {
    public static final String MESSAGE = "message";
    public static final String RESOURCE = "resource";
    public static final String FINGERPRINT = "fingerprint";

    @Inject
    @Implement(name = MessageDAOImpl.class)
    private IMessageDAO messageDAO;
    @Inject
    @Implement(name = ResourceDAOImpl.class)
    private IResourceDAO resourceDAO;
    @Inject
    @Implement(name = FingerprintDAOImpl.class)
    private IFingerprintDAO fingerprintDAO;

    /**
     * 获取DAO单例
     *
     * @param name DAO名称
     * @return DAO单例
     */
    public IBaseDAO getInstance(String name) {
        switch (name) {
            case MESSAGE:
                return messageDAO;
            case RESOURCE:
                return resourceDAO;
            case FINGERPRINT:
                return fingerprintDAO;
            default:
                throw new ClientException(ClientExceptionEnum.INVALID_DAO_NAME);
        }
    }
}
