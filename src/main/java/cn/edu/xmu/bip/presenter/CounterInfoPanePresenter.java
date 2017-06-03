/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.presenter;

import cn.edu.xmu.bip.util.FontUtil;
import cn.edu.xmu.bip.util.MeasurementUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 主界面-窗口信息
 *
 * @author luoxin
 * @version 2017-6-3
 */
public class CounterInfoPanePresenter implements Initializable {
    //整个窗格占屏幕2/3宽、7/8高，间距为屏幕1/24高
    private static final double PANE_WIDTH_FACTOR = (double) 2 / 3;
    private static final double PANE_HEIGHT_FACTOR = (double) 7 / 8;
    private static final double PANE_MARGIN_FACTOR = (double) 1 / 16;
    //窗口编号占整个窗格的3/5，窗口名称占整个窗格的2/5高
    private static final double LABEL_NUMBER_HEIGHT_FACTOR = (double) 1 / 2;
    private static final double LABEL_NAME_HEIGHT_FACTOR = (double) 3 / 10;

    @FXML
    private VBox vbCounter;
    @FXML
    private Label lblNumber;
    @FXML
    private Label lblName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        double paneWidth = MeasurementUtil.getNodeWidthByFactor(PANE_WIDTH_FACTOR);
        double paneHeight = MeasurementUtil.getNodeHeightByFactor(PANE_HEIGHT_FACTOR);
        double paneSpacing = MeasurementUtil.getNodeSizeInParentByFactor(paneHeight, PANE_MARGIN_FACTOR);
        vbCounter.setPrefWidth(paneWidth);
        vbCounter.setPrefHeight(paneHeight);
        vbCounter.setSpacing(paneSpacing);
        vbCounter.setPadding(new Insets(paneSpacing));

        double labelNumberHeight = MeasurementUtil.getNodeSizeInParentByFactor(paneHeight, LABEL_NUMBER_HEIGHT_FACTOR);
        double labelNameHeight = MeasurementUtil.getNodeSizeInParentByFactor(paneHeight, LABEL_NAME_HEIGHT_FACTOR);
        lblNumber.setFont(FontUtil.loadFont(labelNumberHeight, true));
        lblName.setFont(FontUtil.loadFont(labelNameHeight, true));
    }
}
