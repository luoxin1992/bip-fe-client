/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.splash.presenter;

import cn.edu.xmu.bip.ui.splash.model.LoadingModel;
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

import javax.inject.Inject;
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
    //提示/进度字体为父布局高的1/3
    private static final double MESSAGE_PROGRESS_HEIGHT_FACTOR = (double) 1 / 3;

    @FXML
    private HBox hbParent;
    @FXML
    private ProgressIndicator piLoading;
    @FXML
    private Label lblMessage;
    @FXML
    private Label lblProgress;

    @Inject
    private LoadingModel loadingModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindView();
        bindViewModel();
    }

    private void bindView() {
        DoubleBinding parentSpacingBinding = Bindings.multiply(hbParent.heightProperty(), PARENT_SPACING_FACTOR);
        hbParent.spacingProperty().bind(parentSpacingBinding);

        DoubleBinding indicatorRadiusBinding = Bindings.multiply(hbParent.heightProperty(), LOADING_RADIUS_FACTOR);
        piLoading.prefWidthProperty().bind(indicatorRadiusBinding);
        piLoading.prefHeightProperty().bind(indicatorRadiusBinding);

        Callable<Font> labelFont = () -> FontUtil.loadFont(hbParent.getHeight() * MESSAGE_PROGRESS_HEIGHT_FACTOR, false);
        ObjectBinding<Font> labelFontBinding = Bindings.createObjectBinding(labelFont, hbParent.heightProperty());
        lblMessage.fontProperty().bind(labelFontBinding);
        lblProgress.fontProperty().bind(labelFontBinding);
    }

    private void bindViewModel() {
        lblMessage.textProperty().bind(loadingModel.messageProperty());
        lblProgress.textProperty().bind(loadingModel.progressProperty());
    }
}
