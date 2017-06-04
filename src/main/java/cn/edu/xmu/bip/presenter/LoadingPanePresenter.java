/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.presenter;

import cn.edu.xmu.bip.util.FontUtil;
import cn.edu.xmu.bip.util.MeasurementUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 欢迎界面-加载进度
 *
 * @author luoxin
 * @version 2017-6-4
 */
public class LoadingPanePresenter implements Initializable {
    private static final double PANE_HEIGHT_FACTOR = (double) 1 / 8;
    private static final double PROGRESS_RADIUS_FACTOR = (double) 1 / 3;
    private static final double LABEL_SIZE_FACTOR = (double) 1 / 3;

    @FXML
    private HBox hbLoading;
    @FXML
    private ProgressIndicator piLoading;
    @FXML
    private Label lblLoading;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        double paneHeight = MeasurementUtil.getNodeHeightByFactor(PANE_HEIGHT_FACTOR);
        hbLoading.setPrefHeight(paneHeight);

        double progressRadius = MeasurementUtil.getNodeSizeInParentByFactor(paneHeight, PROGRESS_RADIUS_FACTOR);
        piLoading.setPrefWidth(progressRadius);
        piLoading.setPrefHeight(progressRadius);

        double labelHeight = MeasurementUtil.getNodeSizeInParentByFactor(paneHeight, LABEL_SIZE_FACTOR);
        lblLoading.setFont(FontUtil.loadFont(labelHeight, false));
    }
}
