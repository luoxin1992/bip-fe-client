/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.presenter;

import cn.edu.xmu.bip.util.MeasurementUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 欢迎界面-合作商Logo
 *
 * @author luoxin
 * @version 2017-5-27
 */
public class PartnerPanePresenter implements Initializable {
    private static final double PANE_HEIGHT_FACTOR = (double) 1 / 8;
    private static final double IMAGE_HEIGHT_FACTOR = (double) 4 / 5;

    @FXML
    private HBox hbPartner;
    @FXML
    private ImageView ivCrossmatch;
    @FXML
    private ImageView ivDivider1;
    @FXML
    private ImageView ivIflytek;
    @FXML
    private ImageView ivDivider2;
    @FXML
    private ImageView ivMtyun;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        double paneHeight = MeasurementUtil.getNodeHeightByFactor(PANE_HEIGHT_FACTOR);
        hbPartner.setPrefHeight(paneHeight);

        double imageHeight = MeasurementUtil.getNodeSizeInParentByFactor(paneHeight, IMAGE_HEIGHT_FACTOR);
        ivCrossmatch.setFitHeight(imageHeight);
        ivDivider1.setFitHeight(imageHeight);
        ivIflytek.setFitHeight(imageHeight);
        ivDivider2.setFitHeight(imageHeight);
        ivMtyun.setFitHeight(imageHeight);
    }
}
