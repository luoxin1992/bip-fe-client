/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.main.presenter;

import cn.com.lx1992.lib.client.util.PreferencesUtil;
import cn.edu.xmu.bip.constant.PreferenceKeyConstant;
import cn.edu.xmu.bip.service.IMiscService;
import cn.edu.xmu.bip.service.IWSService;
import cn.edu.xmu.bip.service.factory.ServiceFactory;
import cn.edu.xmu.bip.ui.main.model.BannerModel;
import cn.edu.xmu.bip.ui.main.model.CounterModel;
import cn.edu.xmu.bip.ui.main.model.PaneVisibleModel;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

import javax.inject.Inject;
import javax.websocket.CloseReason;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * 主界面
 *
 * @author luoxin
 * @version 2017-6-6
 */
public class MainScenePresenter implements Initializable {
    //Banner窗格为父布局高的1/8
    private static final double BANNER_HEIGHT_FACTOR = (double) 1 / 8;
    //时钟/客户窗格(左侧上)为父布局宽的1/3、高的19/24
    private static final double CLOCK_WIDTH_FACTOR = (double) 1 / 3;
    private static final double CLOCK_HEIGHT_FACTOR = (double) 19 / 24;
    private static final double USER_WIDTH_FACTOR = (double) 1 / 3;
    private static final double USER_HEIGHT_FACTOR = (double) 19 / 24;
    //开发者窗格(左侧下)为父布局宽的1/3、高的1/12
    private static final double DEVELOPER_WIDTH_FACTOR = (double) 1 / 3;
    private static final double DEVELOPER_HEIGHT_FACTOR = (double) 1 / 12;
    //窗口/消息窗格(右侧)为父布局宽的2/3、高的7/8
    private static final double COUNTER_WIDTH_FACTOR = (double) 2 / 3;
    private static final double COUNTER_HEIGHT_FACTOR = (double) 7 / 8;
    private static final double MESSAGE_WIDTH_FACTOR = (double) 2 / 3;
    private static final double MESSAGE_HEIGHT_FACTOR = (double) 7 / 8;

    @FXML
    private BorderPane bpRoot;
    @FXML
    private AnchorPane apBanner;
    @FXML
    private VBox vbClock;
    @FXML
    private VBox vbUser;
    @FXML
    private StackPane spDeveloper;
    @FXML
    private StackPane spCounter;
    @FXML
    private VBox vbMessage;

    @Inject
    private ServiceFactory serviceFactory;

    @Inject
    private PaneVisibleModel paneVisibleModel;
    @Inject
    private CounterModel counterModel;
    @Inject
    private BannerModel bannerModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindView();
        bindEvent();
    }

    private void bindView() {
        apBanner.prefWidthProperty().bind(bpRoot.widthProperty());
        apBanner.prefHeightProperty().bind(Bindings.multiply(bpRoot.heightProperty(), BANNER_HEIGHT_FACTOR));
        vbClock.prefWidthProperty().bind(Bindings.multiply(bpRoot.widthProperty(), CLOCK_WIDTH_FACTOR));
        vbClock.prefHeightProperty().bind(Bindings.multiply(bpRoot.heightProperty(), CLOCK_HEIGHT_FACTOR));
        vbUser.prefWidthProperty().bind(Bindings.multiply(bpRoot.widthProperty(), USER_WIDTH_FACTOR));
        vbUser.prefHeightProperty().bind(Bindings.multiply(bpRoot.heightProperty(), USER_HEIGHT_FACTOR));
        spDeveloper.prefWidthProperty().bind(Bindings.multiply(bpRoot.widthProperty(), DEVELOPER_WIDTH_FACTOR));
        spDeveloper.prefHeightProperty().bind(Bindings.multiply(bpRoot.heightProperty(), DEVELOPER_HEIGHT_FACTOR));
        spCounter.prefWidthProperty().bind(Bindings.multiply(bpRoot.widthProperty(), COUNTER_WIDTH_FACTOR));
        spCounter.prefHeightProperty().bind(Bindings.multiply(bpRoot.heightProperty(), COUNTER_HEIGHT_FACTOR));
        vbMessage.prefWidthProperty().bind(Bindings.multiply(bpRoot.widthProperty(), MESSAGE_WIDTH_FACTOR));
        vbMessage.prefHeightProperty().bind(Bindings.multiply(bpRoot.heightProperty(), MESSAGE_HEIGHT_FACTOR));
    }

    private void bindEvent() {
        bpRoot.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue == null && newValue != null) {
                newValue.windowProperty().addListener((observable1, oldValue1, newValue1) -> {
                    if (oldValue1 == null && newValue1 != null) {
                        bindWindowEvent(newValue1);
                    }
                });
            }
        });
    }

    /**
     * 窗口显示(隐藏)时
     * <p>
     * 建立(关闭)WebSocket连接
     * 启动(停止)时钟
     *
     * @param window 根部局绑定的窗口
     */
    private void bindWindowEvent(Window window) {
        window.setOnShown(event -> {
            IWSService wsService = (IWSService) serviceFactory.getInstance(ServiceFactory.WS);
            wsService.openSession(PreferencesUtil.get(PreferenceKeyConstant.SESSION_TOKEN));

            IMiscService miscService = (IMiscService) serviceFactory.getInstance(ServiceFactory.MISC);
            miscService.startClock();
        });
        window.setOnHiding(event -> {
            IWSService wsService = (IWSService) serviceFactory.getInstance(ServiceFactory.WS);
            wsService.closeSession(CloseReason.CloseCodes.NORMAL_CLOSURE);

            IMiscService miscService = (IMiscService) serviceFactory.getInstance(ServiceFactory.MISC);
            miscService.stopClock();
        });
    }
}
