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
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 主界面-用户信息
 *
 * @author luoxin
 * @version 2017-5-29
 */
public class UserInfoPanePresenter implements Initializable {
    //整个窗格占屏幕1/3宽、3/4高，间距为屏幕1/24高
    private static final double PANE_WIDTH_FACTOR = (double) 1 / 3;
    private static final double PANE_HEIGHT_FACTOR = (double) 3 / 4;
    private static final double PANE_MARGIN_FACTOR = (double) 1 / 24;
    //照片占整个窗格的2/3宽、2/3高
    private static final double IMAGE_WIDTH_FACTOR = (double) 2 / 3;
    private static final double IMAGE_HEIGHT_FACTOR = (double) 2 / 3;
    //编号、姓名正文占整个窗格的1/8，提示占整个窗格的1/16高
    private static final double LABEL_HEIGHT_FACTOR = (double) 1 / 8;
    private static final double LABEL_PROMPT_HEIGHT_FACTOR = (double) 1 / 16;

    @FXML
    private VBox vbUser;
    @FXML
    private ImageView ivPhoto;
    @FXML
    private Label lblNumberPrompt;
    @FXML
    private Label lblNumber;
    @FXML
    private Label lblNamePrompt;
    @FXML
    private Label lblName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        double paneWidth = MeasurementUtil.getNodeWidthByFactor(PANE_WIDTH_FACTOR);
        double paneHeight = MeasurementUtil.getNodeHeightByFactor(PANE_HEIGHT_FACTOR);
        double paneMargin = MeasurementUtil.getNodeSizeInParentByFactor(paneHeight, PANE_MARGIN_FACTOR);
        vbUser.setPrefWidth(paneWidth);
        vbUser.setPrefHeight(paneHeight);
        vbUser.setSpacing(paneMargin);
        vbUser.setPadding(new Insets(paneMargin));

        double photoWidth = MeasurementUtil.getNodeSizeInParentByFactor(paneWidth, IMAGE_WIDTH_FACTOR);
        double photoHeight = MeasurementUtil.getNodeSizeInParentByFactor(paneHeight, IMAGE_HEIGHT_FACTOR);
        ivPhoto.setFitWidth(photoWidth);
        ivPhoto.setFitHeight(photoHeight);

        double labelHeight = MeasurementUtil.getNodeSizeInParentByFactor(paneHeight, LABEL_HEIGHT_FACTOR);
        double labelPromptHeight = MeasurementUtil.getNodeSizeInParentByFactor(paneHeight, LABEL_PROMPT_HEIGHT_FACTOR);
        lblNumberPrompt.setFont(FontUtil.loadFont(labelPromptHeight, false));
        lblNumber.setFont(FontUtil.loadFont(labelHeight, true));
        lblNamePrompt.setFont(FontUtil.loadFont(labelPromptHeight, false));
        lblName.setFont(FontUtil.loadFont(labelHeight, true));
    }
}
