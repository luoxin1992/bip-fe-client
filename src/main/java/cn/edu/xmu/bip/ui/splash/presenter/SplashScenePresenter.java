/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.splash.presenter;

import cn.edu.xmu.bip.service.IMiscService;
import cn.edu.xmu.bip.service.IResourceService;
import cn.edu.xmu.bip.service.factory.ServiceFactory;
import cn.edu.xmu.bip.ui.StageFactory;
import cn.edu.xmu.bip.util.CrashUtil;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

/**
 * 启动界面
 *
 * @author luoxin
 * @version 2017-6-5
 */
public class SplashScenePresenter implements Initializable {
    //产品信息为父布局高的7/8
    private static final double PRODUCT_HEIGHT_FACTOR = (double) 7 / 8;
    //合作商和加载进度为父布局高的1/8
    private static final double PARTNER_HEIGHT_FACTOR = (double) 1 / 8;
    private static final double LOADING_HEIGHT_FACTOR = (double) 1 / 8;

    @FXML
    private BorderPane bpRoot;
    @FXML
    private StackPane spProduct;
    @FXML
    private HBox hbPartner;
    @FXML
    private HBox hbLoading;

    @Inject
    private ServiceFactory serviceFactory;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindView();
        bindEvent();
    }

    private void bindView() {
        spProduct.prefWidthProperty().bind(bpRoot.widthProperty());
        spProduct.prefHeightProperty().bind(Bindings.multiply(bpRoot.heightProperty(), PRODUCT_HEIGHT_FACTOR));
        hbPartner.prefHeightProperty().bind(Bindings.multiply(bpRoot.heightProperty(), PARTNER_HEIGHT_FACTOR));
        hbLoading.prefHeightProperty().bind(Bindings.multiply(bpRoot.heightProperty(), LOADING_HEIGHT_FACTOR));
    }

    private void bindEvent() {
        //View展示时进行程序初始化工作
        bpRoot.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue == null && newValue != null) {
                newValue.windowProperty().addListener((observable1, oldValue1, newValue1) -> {
                    if (oldValue1 == null && newValue1 != null) {
                        newValue1.setOnShown(event -> bindWindowEvent());
                    }
                });
            }
        });
    }

    private void bindWindowEvent() {
        CompletableFuture
                //检查运行环境
                .runAsync(((IMiscService) serviceFactory.getInstance(ServiceFactory.MISC)).checkRuntime())
                //检查资源更新
                .thenRun(((IResourceService) serviceFactory.getInstance(ServiceFactory.RESOURCE)).checkUpdate())
                //下载资源更新
                .thenRun(((IResourceService) serviceFactory.getInstance(ServiceFactory.RESOURCE)).downloadUpdate())
                //获取基础数据
                .thenRun(((IMiscService) serviceFactory.getInstance(ServiceFactory.MISC)).getBasicData())
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        CrashUtil.logCrashAndExit(exception);
                    } else {
                        //打开Main界面，关闭Splash界面
                        StageFactory.showStage(StageFactory.MAIN);
                        StageFactory.hideStage(StageFactory.SPLASH);
                    }
                });
    }
}
