/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.splash.presenter;

import cn.edu.xmu.bip.util.FontUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

/**
 * 启动界面-加载进度
 *
 * @author luoxin
 * @version 2017-6-4
 */
public class LoadingPanePresenter implements Initializable {
    //间距为父布局高的1/15
    private static final double PARENT_SPACING_FACTOR = (double) 1 / 15;
    //指示器半径为父布局高的1/3
    private static final double LOADING_RADIUS_FACTOR = (double) 1 / 3;
    //状态文本字体为父布局高的1/3
    private static final double STATUS_HEIGHT_FACTOR = (double) 1 / 3;

    @FXML
    private HBox hbParent;
    @FXML
    private ProgressIndicator piLoading;
    @FXML
    private Label lblStatus;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DoubleBinding parentSpacingBinding = Bindings.multiply(hbParent.heightProperty(), PARENT_SPACING_FACTOR);
        hbParent.spacingProperty().bind(parentSpacingBinding);

        DoubleBinding indicatorRadiusBinding = Bindings.multiply(hbParent.heightProperty(), LOADING_RADIUS_FACTOR);
        piLoading.prefWidthProperty().bind(indicatorRadiusBinding);
        piLoading.prefHeightProperty().bind(indicatorRadiusBinding);

        Callable<Font> statusFont = () -> FontUtil.loadFont(hbParent.getHeight() * STATUS_HEIGHT_FACTOR, false);
        ObjectBinding<Font> statusFontBinding = Bindings.createObjectBinding(statusFont, hbParent.heightProperty());
        lblStatus.fontProperty().bind(statusFontBinding);
    }
}
