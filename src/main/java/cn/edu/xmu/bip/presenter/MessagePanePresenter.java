/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.presenter;

import cn.edu.xmu.bip.util.FontUtil;
import cn.edu.xmu.bip.util.MeasurementUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 主界面-消息
 *
 * @author luoxin
 * @version 2017-6-3
 */
public class MessagePanePresenter implements Initializable {
    //整个窗格占屏幕2/3宽、7/8高，间距为屏幕1/10高
    private static final double PANE_WIDTH_FACTOR = (double) 2 / 3;
    private static final double PANE_HEIGHT_FACTOR = (double) 7 / 8;
    private static final double PANE_SPACING_FACTOR = (double) 1 / 10;
    //消息图片占整个窗格的3/4宽、3/4高
    private static final double IMAGE_WIDTH_FACTOR = (double) 3 / 4;
    private static final double IMAGE_HEIGHT_FACTOR = (double) 3 / 4;
    //消息内容占整个窗格的1/8高
    private static final double LABEL_HEIGHT_FACTOR = (double) 1 / 7;

    @FXML
    private VBox vbMessage;
    @FXML
    private ImageView ivMessage;
    @FXML
    private Label lblMessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        double paneWidth = MeasurementUtil.getNodeWidthByFactor(PANE_WIDTH_FACTOR);
        double paneHeight = MeasurementUtil.getNodeHeightByFactor(PANE_HEIGHT_FACTOR);
        double paneSpacing = MeasurementUtil.getNodeHeightByFactor(PANE_SPACING_FACTOR);
        vbMessage.setPrefWidth(paneWidth);
        vbMessage.setPrefHeight(paneHeight);
        vbMessage.setSpacing(paneSpacing);

        double imageWidth = MeasurementUtil.getNodeWidthByFactor(paneWidth, IMAGE_WIDTH_FACTOR);
        double imageHeight = MeasurementUtil.getNodeHeightByFactor(paneHeight, IMAGE_HEIGHT_FACTOR);
        ivMessage.setFitWidth(imageWidth);
        ivMessage.setFitHeight(imageHeight);

        double labelHeight = MeasurementUtil.getNodeHeightByFactor(paneHeight, LABEL_HEIGHT_FACTOR);
        lblMessage.setFont(FontUtil.loadFont(labelHeight, true));
        lblMessage.setTextOverrun(OverrunStyle.ELLIPSIS);
    }
}
