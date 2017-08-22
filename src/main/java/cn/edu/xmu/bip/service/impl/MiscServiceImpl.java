/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.service.impl;

import cn.com.lx1992.lib.client.dto.NicAddressDTO;
import cn.com.lx1992.lib.client.util.CrashUtil;
import cn.com.lx1992.lib.client.util.NativeUtil;
import cn.com.lx1992.lib.client.util.PreferencesUtil;
import cn.edu.xmu.bip.constant.PreferenceKeyConstant;
import cn.edu.xmu.bip.constant.ResourceConstant;
import cn.edu.xmu.bip.exception.ClientException;
import cn.edu.xmu.bip.exception.ClientExceptionEnum;
import cn.edu.xmu.bip.job.ClockUpdateJob;
import cn.edu.xmu.bip.meta.ResourceTypeEnum;
import cn.edu.xmu.bip.param.CounterQueryBindParam;
import cn.edu.xmu.bip.param.SessionApplyParam;
import cn.edu.xmu.bip.param.SettingListParam;
import cn.edu.xmu.bip.result.CounterQueryBindResult;
import cn.edu.xmu.bip.result.SessionApplyResult;
import cn.edu.xmu.bip.result.SettingQueryResult;
import cn.edu.xmu.bip.service.IAPIService;
import cn.edu.xmu.bip.service.IMiscService;
import cn.edu.xmu.bip.service.IResourceService;
import cn.edu.xmu.bip.service.factory.ServiceFactory;
import cn.edu.xmu.bip.ui.main.model.BannerModel;
import cn.edu.xmu.bip.ui.main.model.CounterModel;
import cn.edu.xmu.bip.ui.splash.model.LoadingModel;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class MiscServiceImpl implements IMiscService {
    private static final String CHECK_RUNTIME_SUPPORT_OS = "Windows";
    private static final String CHECK_RUNTIME_SUPPORT_JVM = "32";
    private static final String BASIC_INFO_COMPANY = "company";
    private static final String BASIC_INFO_COMPANY_LOGO = "company-logo";
    private static final String BASIC_INFO_COMPANY_NAME = "company-name";
    private static final String BASIC_INFO_COUNTER = "counter";
    private static final String BASIC_INFO_COUNTER_NUMBER = "counter-number";
    private static final String BASIC_INFO_COUNTER_NAME = "counter-name";

    private final Logger logger = LoggerFactory.getLogger(MiscServiceImpl.class);

    @Inject
    private ServiceFactory serviceFactory;
    @Inject
    private ClockUpdateJob clockUpdateJob;

    @Inject
    private BannerModel bannerModel;
    @Inject
    private CounterModel counterModel;
    @Inject
    private LoadingModel loadingModel;

    @PostConstruct
    private void destroy() {
        PreferencesUtil.remove(PreferenceKeyConstant.SESSION_TOKEN);
        PreferencesUtil.remove(PreferenceKeyConstant.SESSION_COUNTER_ID);
    }

    @Override
    public Task<Void> checkRuntime() {
        return new Task<Void>() {
            @Override
            protected void scheduled() {
                String message = "检查运行环境";
                loadingModel.setMessage(message);
            }

            @Override
            protected Void call() throws Exception {
                checkOs();
                checkJvm();
                checkBackend();
                return null;
            }

            @Override
            protected void failed() {
                CrashUtil.logCrashAndExit(getException());
            }
        };
    }

    /**
     * 检查操作系统
     */
    private void checkOs() {
        String osName = NativeUtil.getOsName();
        if (StringUtils.isEmpty(osName) || !osName.contains(CHECK_RUNTIME_SUPPORT_OS)) {
            logger.error("unsupported operating system: {}", osName);
            throw new ClientException(ClientExceptionEnum.MISC_UNSUPPORTED_OS, osName);
        }
    }

    /**
     * 检查Java虚拟机
     */
    private void checkJvm() {
        String jvmArch = NativeUtil.getJvmArch();
        if (StringUtils.isEmpty(jvmArch) || !jvmArch.contains(CHECK_RUNTIME_SUPPORT_JVM)) {
            logger.error("unsupported java virtual machine architecture: {}", jvmArch);
            throw new ClientException(ClientExceptionEnum.MISC_UNSUPPORTED_JVM, jvmArch);
        }
    }

    /**
     * 检查后端服务可用性
     */
    private void checkBackend() {
        IAPIService apiService = (IAPIService) serviceFactory.getInstance(ServiceFactory.API);
        apiService.invokeAlive();
    }

    @Override
    public Task<Map<String, Map<String, String>>> getBasicData() {
        return new Task<Map<String, Map<String, String>>>() {
            @Override
            protected void scheduled() {
                String message = "获取基础数据";
                loadingModel.setMessage(message);
            }

            @Override
            protected Map<String, Map<String, String>> call() throws Exception {
                Map<String, String> company = getCompanyInfo();
                Map<String, String> counter = getCounterInfo();
                applySession();

                Map<String, Map<String, String>> retMap = new HashMap<>();
                retMap.put(BASIC_INFO_COMPANY, company);
                retMap.put(BASIC_INFO_COUNTER, counter);
                return retMap;
            }

            @Override
            protected void succeeded() {
                Map<String, String> company = getValue().get(BASIC_INFO_COMPANY);
                bannerModel.setLogo(new Image(company.get(BASIC_INFO_COMPANY_LOGO), true));
                bannerModel.setName(company.get(BASIC_INFO_COMPANY_NAME));

                Map<String, String> counter = getValue().get(BASIC_INFO_COUNTER);
                counterModel.setNumber(counter.get(BASIC_INFO_COUNTER_NUMBER));
                counterModel.setName(counter.get(BASIC_INFO_COUNTER_NAME));
            }

            @Override
            protected void failed() {
                CrashUtil.logCrashAndExit(getException());
            }
        };
    }

    /**
     * 获取公司信息
     *
     * @return 公司信息
     */
    private Map<String, String> getCompanyInfo() {
        SettingListParam param = new SettingListParam();
        param.setParent(BASIC_INFO_COMPANY);

        IAPIService apiService = (IAPIService) serviceFactory.getInstance(ServiceFactory.API);
        SettingQueryResult result = apiService.invokeSettingList(param);
        logger.info("get company settings, length = {}", result.getList().size());

        //获取所需公司信息(亦或取默认值)
        String logo = result.getList().stream()
                .filter(item -> BASIC_INFO_COMPANY_LOGO.equals(item.getKey()) && !StringUtils.isEmpty(item.getValue()))
                .map(SettingQueryResult.Item::getValue)
                .findFirst()
                .orElse(null);
        String name = result.getList().stream()
                .filter(item -> BASIC_INFO_COMPANY_NAME.equals(item.getKey()) && !StringUtils.isEmpty(item.getValue()))
                .map(SettingQueryResult.Item::getValue)
                .findFirst()
                .orElse(null);
        //LOGO转换成对应资源
        IResourceService resourceService = (IResourceService) serviceFactory.getInstance(ServiceFactory.RESOURCE);
        logo = resourceService.get(ResourceTypeEnum.IMAGE, logo, true);

        //取公司LOGO和公司名称 包装成Map
        Map<String, String> retMap = new HashMap<>();
        retMap.put(BASIC_INFO_COMPANY_LOGO, !StringUtils.isEmpty(logo) ? logo : ResourceConstant.DEFAULT_COMPANY_LOGO);
        retMap.put(BASIC_INFO_COMPANY_NAME, !StringUtils.isEmpty(name) ? name : "请登录管理后台配置公司信息");
        return retMap;
    }

    /**
     * 获取窗口信息
     *
     * @return 窗口信息
     */
    private Map<String, String> getCounterInfo() {
        String nicIndex = PreferencesUtil.get(PreferenceKeyConstant.CONFIG_BIND_NIC_INDEX);
        if (StringUtils.isEmpty(nicIndex)) {
            logger.error("bind nic index not exist in preference");
            throw new ClientException(ClientExceptionEnum.MISC_BIND_NIC_NOT_EXIST);
        }

        NicAddressDTO nicAddress = NativeUtil.getNicAddress(Integer.parseInt(nicIndex));
        logger.info("get bind nic address, mac = {}, ip = {}", nicAddress.getMac(), nicAddress.getIpv4());

        CounterQueryBindParam param = new CounterQueryBindParam();
        param.setMac(nicAddress.getMac());
        param.setIp(nicAddress.getIpv4());

        IAPIService apiService = (IAPIService) serviceFactory.getInstance(ServiceFactory.API);
        CounterQueryBindResult result = apiService.invokeCounterQueryBind(param);
        logger.info("get bind counter info, id = {}", result.getId());

        //返回结果中的窗口ID存入偏好，其他信息包装成Map返回
        PreferencesUtil.put(PreferenceKeyConstant.SESSION_COUNTER_ID, result.getId());

        Map<String, String> retMap = new HashMap<>();
        retMap.put(BASIC_INFO_COUNTER_NUMBER, result.getNumber());
        retMap.put(BASIC_INFO_COUNTER_NAME, result.getName());
        return retMap;
    }

    /**
     * 申请会话Token
     */
    private void applySession() {
        String counterId = PreferencesUtil.get(PreferenceKeyConstant.SESSION_COUNTER_ID);
        if (StringUtils.isEmpty(counterId)) {
            logger.error("counter id not exist in preference");
            throw new ClientException(ClientExceptionEnum.MISC_COUNTER_ID_NOT_EXIST);
        }

        //调用申请会话Token API
        SessionApplyParam param = new SessionApplyParam();
        param.setCounterId(Long.parseLong(counterId));

        IAPIService apiService = (IAPIService) serviceFactory.getInstance(ServiceFactory.API);
        SessionApplyResult result = apiService.invokeSessionApply(param);
        logger.info("get session apply result, token = {}", result.getToken());

        //存入偏好
        PreferencesUtil.put(PreferenceKeyConstant.SESSION_TOKEN, result.getToken());
    }

    @Override
    public void startClock() {
        clockUpdateJob.setPeriod(Duration.seconds(1));
        clockUpdateJob.start();
        logger.info("start clock update job");
    }

    @Override
    public void stopClock() {
        clockUpdateJob.cancel();
        logger.info("stop clock update job");
    }
}
