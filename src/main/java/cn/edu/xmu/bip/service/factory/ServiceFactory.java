/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.service.factory;

import cn.com.lx1992.lib.client.inject.Implement;
import cn.edu.xmu.bip.exception.ClientException;
import cn.edu.xmu.bip.exception.ClientExceptionEnum;
import cn.edu.xmu.bip.service.IAPIService;
import cn.edu.xmu.bip.service.IAdminService;
import cn.edu.xmu.bip.service.IBaseService;
import cn.edu.xmu.bip.service.IFingerprintService;
import cn.edu.xmu.bip.service.IMessageService;
import cn.edu.xmu.bip.service.IMiscService;
import cn.edu.xmu.bip.service.IResourceService;
import cn.edu.xmu.bip.service.IWSService;
import cn.edu.xmu.bip.service.impl.APIServiceImpl;
import cn.edu.xmu.bip.service.impl.AdminServiceImpl;
import cn.edu.xmu.bip.service.impl.FingerprintServiceImpl;
import cn.edu.xmu.bip.service.impl.MessageServiceImpl;
import cn.edu.xmu.bip.service.impl.MiscServiceImpl;
import cn.edu.xmu.bip.service.impl.ResourceServiceImpl;
import cn.edu.xmu.bip.service.impl.WSServiceImpl;

import javax.inject.Inject;

/**
 * @author luoxin
 * @version 2017-8-2
 */
public class ServiceFactory {
    public static final String WS = "ws";
    public static final String API = "api";
    public static final String MISC = "misc";
    public static final String ADMIN = "admin";
    public static final String MESSAGE = "message";
    public static final String RESOURCE = "resource";
    public static final String FINGERPRINT = "fingerprint";

    @Inject
    @Implement(name = WSServiceImpl.class)
    private IWSService wsService;
    @Inject
    @Implement(name = APIServiceImpl.class)
    private IAPIService apiService;
    @Inject
    @Implement(name = MiscServiceImpl.class)
    private IMiscService miscService;
    @Inject
    @Implement(name = AdminServiceImpl.class)
    private IAdminService adminService;
    @Inject
    @Implement(name = MessageServiceImpl.class)
    private IMessageService messageService;
    @Inject
    @Implement(name = ResourceServiceImpl.class)
    private IResourceService resourceService;
    @Inject
    @Implement(name = FingerprintServiceImpl.class)
    private IFingerprintService fingerprintService;

    public IBaseService getInstance(String name) {
        switch (name) {
            case WS:
                return wsService;
            case API:
                return apiService;
            case MISC:
                return miscService;
            case ADMIN:
                return adminService;
            case MESSAGE:
                return messageService;
            case RESOURCE:
                return resourceService;
            case FINGERPRINT:
                return fingerprintService;
            default:
                throw new ClientException(ClientExceptionEnum.INVALID_SERVICE_NAME);
        }
    }
}
