/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.service.impl;

import cn.com.lx1992.lib.client.constant.DateTimeConstant;
import cn.com.lx1992.lib.client.util.DateTimeUtil;
import cn.com.lx1992.lib.client.util.NativeUtil;
import cn.edu.xmu.bip.dao.IFingerprintDAO;
import cn.edu.xmu.bip.dao.factory.DAOFactory;
import cn.edu.xmu.bip.meta.FingerprintEventEnum;
import cn.edu.xmu.bip.service.IFingerprintService;
import cn.edu.xmu.bip.service.IMessageService;
import cn.edu.xmu.bip.service.factory.ServiceFactory;
import com4j.Com4jObject;
import com4j.EventCookie;
import fpscanner.ClassFactory;
import fpscanner.IFPScannerEvents;
import fpscanner.IFPScannerObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

public class FingerprintServiceImpl implements IFingerprintService {
    //SDK工作模式：0=空闲/1=登记/3=辨识(1:N)
    private static final int STATE_MARK_IDLE = 0;
    private static final int STATE_MARK_ENROLL = 1;
    private static final int STATE_MARK_IDENTIFY = 3;
    //SDK中间数据本地保存
    private static final String DATA_SUBDIRECTORY = "fingerprint";
    private static final String DATA_TYPE_IMAGE = "image";
    private static final String DATA_TYPE_TEMPLATE = "template";
    private static final String FILE_EXTENSION_IMAGE = ".bmp";
    private static final String FILE_EXTENSION_TEMPLATE = ".dat";

    private final Logger logger = LoggerFactory.getLogger(FingerprintServiceImpl.class);

    @Inject
    private ServiceFactory serviceFactory;
    @Inject
    private DAOFactory daoFactory;

    private IFPScannerObject fpScannerObject;
    private EventCookie fpScannerEvent;

    @PostConstruct
    private void initialize() {
        fpScannerObject = ClassFactory.createIFPScannerObject();
        fpScannerObject.connectFpScanner();
        fpScannerEvent = fpScannerObject.advise(IFPScannerEvents.class, new FPScannerEvents());
        logger.info("initialize fingerprint scanner sdk");
    }

    @PreDestroy
    private void destroy() {
        fpScannerEvent.close();
        fpScannerObject.disconnectFpScanner();
        fpScannerObject.dispose();
        logger.info("destroy fingerprint scanner sdk");
    }

    @Override
    public void enroll(int count) {
        if (fpScannerObject.getStateMark() == STATE_MARK_ENROLL) {
            logger.error("fingerprint sdk state is already ENROLL");
            return;
        }

        checkScanner();

        fpScannerObject.setEnrollCount(count);
        fpScannerObject.beginEnroll();
        logger.info("set fingerprint sdk state to ENROLL");
    }

    @Override
    public void identify() {
        if (fpScannerObject.getStateMark() == STATE_MARK_IDENTIFY) {
            logger.error("fingerprint sdk state is already IDENTIFY");
            return;
        }

        checkScanner();

        fpScannerObject.setStateMark(STATE_MARK_IDENTIFY);
        logger.info("set fingerprint sdk state to IDENTIFY");
    }

    @Override
    public void cancel() {
        if (fpScannerObject.getStateMark() == STATE_MARK_IDLE) {
            logger.warn("fingerprint sdk state is already IDLE");
            return;
        }

        fpScannerObject.setStateMark(STATE_MARK_IDLE);
        logger.info("set fingerprint sdk state to IDLE");
    }

    /**
     * 检查指纹仪连接情况
     */
    private void checkScanner() {
        if (fpScannerObject.getFPScannerCount() <= 0) {
            //回调消息Service的指纹仪错误方法
            IMessageService messageService = (IMessageService) serviceFactory.getInstance(ServiceFactory.MESSAGE);
            messageService.onFingerprintScannerError();
        }
    }

    /**
     * 获取文件保存路径
     *
     * @param type 数据类型
     * @return 保存路径
     */
    private String getDataSavePath(String type) {
        String path = NativeUtil.getAppDataDirectoryPath(DATA_SUBDIRECTORY);
        String filename = DateTimeUtil.getNowStr(DateTimeConstant.MILLIS_DATETIME_PATTERN);
        switch (type) {
            case DATA_TYPE_IMAGE:
                filename += FILE_EXTENSION_IMAGE;
                break;
            case DATA_TYPE_TEMPLATE:
                filename += FILE_EXTENSION_TEMPLATE;
                break;
        }
        return path + File.separatorChar + filename;
    }

    /**
     * 保存指纹图片到文件
     *
     * @param path 保存路径
     */
    private void saveImageToFile(String path) {
        fpScannerObject.saveFingerprintToImage(path);
        logger.info("save fingerprint image to file {}", path);
    }

    /**
     * 保存指纹模板到文件
     *
     * @param template 指纹模板
     * @param path     保存路径
     */
    private void saveTemplateToFile(String template, String path) {
        try {
            FileUtils.writeByteArrayToFile(new File(path), Base64.decodeBase64(template));
            logger.info("save fingerprint template to file {}", path);
        } catch (IOException e) {
            logger.error("save fingerprint template to file {} failed", path, e);
        }
    }

    /**
     * 创建事件日志
     *
     * @param event 事件
     * @param extra 附加信息
     */
    private void createLog(FingerprintEventEnum event, String extra) {
        IFingerprintDAO fingerprintDAO = (IFingerprintDAO) daoFactory.getInstance(DAOFactory.FINGERPRINT);
        fingerprintDAO.insert(event.getEvent(), extra);
    }

    class FPScannerEvents implements IFPScannerEvents {
        @Override
        public void onImageReceived(Com4jObject pict) {
            if (fpScannerObject.getStateMark() == STATE_MARK_IDLE) {
                logger.warn("ignore captured fingerprint image when sdk state is idle");
                return;
            }

            String path = getDataSavePath(DATA_TYPE_IMAGE);
            saveImageToFile(path);
            createLog(FingerprintEventEnum.IMAGE_CAPTURE, path);
        }

        @Override
        public void onFpScannerConnect(boolean connect, String readerSerNum) {
            createLog(FingerprintEventEnum.SCANNER_CONNECT, readerSerNum);
            logger.info("connect fingerprint scanner {}", readerSerNum);
        }

        @Override
        public void onFpScannerDisConnect(boolean disConnect, String readerSerNum) {
            createLog(FingerprintEventEnum.SCANNER_DISCONNECT, readerSerNum);
            logger.info("disconnect fingerprint scanner {}", readerSerNum);
        }

        @Override
        public void onFingerTouch(boolean onTouch, String readerSerNum) {
        }

        @Override
        public void onFingerGone(boolean onGone, String readerSerNum) {
        }

        @Override
        public void onFingerQuality(short actionResult, String readerSerNum) {
        }

        @Override
        public void enrollIndex(short index) {
            if (index > 0) {
                //index是剩余按压次数，count-index+1即下次按压次数
                IMessageService messageService = (IMessageService) serviceFactory.getInstance(ServiceFactory.MESSAGE);
                messageService.onFingerprintEnrollCount(fpScannerObject.getEnrollCount() - index + 1);
            }
        }

        @Override
        public void onFpEnroll(boolean actionResult, Object atemplate, String readerSerNum) {
            if (!actionResult) {
                logger.warn("get false action result in fingerprint enroll callback");
                return;
            }

            String path = getDataSavePath(DATA_TYPE_TEMPLATE);
            saveTemplateToFile(fpScannerObject.getRegTemplateAsStr(), path);
            createLog(FingerprintEventEnum.ENROLL_TEMPLATE_EXTRACT, path);

            //退出指纹登记
            cancel();

            IMessageService messageService = (IMessageService) serviceFactory.getInstance(ServiceFactory.MESSAGE);
            messageService.onFingerprintEnrollTemplate(fpScannerObject.getRegTemplateAsStr());
        }

        @Override
        public void onFpCapture(boolean actionResult, Object atemplate, String readerSerNum) {
            if (!actionResult) {
                logger.warn("get false result in fingerprint identify callback");
                return;
            }

            String path = getDataSavePath(DATA_TYPE_TEMPLATE);
            saveTemplateToFile(fpScannerObject.getVerTemplateAsStr(), path);
            createLog(FingerprintEventEnum.IDENTIFY_TEMPLATE_EXTRACT, path);

            cancel();

            IMessageService messageService = (IMessageService) serviceFactory.getInstance(ServiceFactory.MESSAGE);
            messageService.onFingerprintIdentifyTemplate(fpScannerObject.getVerTemplateAsStr());
        }
    }
}
