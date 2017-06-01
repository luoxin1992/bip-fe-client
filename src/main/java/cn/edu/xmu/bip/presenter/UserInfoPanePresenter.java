/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.presenter;

import cn.edu.xmu.bip.util.FontUtil;
import cn.edu.xmu.bip.util.MeasurementUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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
    private static final double PANE_SPACING_FACTOR = (double) 1 / 24;
    //照片占整个窗格的2/3宽、2/3高
    private static final double PHOTO_WIDTH_FACTOR = (double) 2 / 3;
    private static final double PHOTO_HEIGHT_FACTOR = (double) 2 / 3;
    //正文文本占整个窗格的1/8，提示文本占整个窗格的1/16高
    private static final double TEXT_HEIGHT_FACTOR = (double) 1 / 8;
    private static final double TEXT_PROMPT_HEIGHT_FACTOR = (double) 1 / 16;

    @FXML
    private VBox vbUserInfo;
    @FXML
    private ImageView ivPhoto;
    @FXML
    private Text txtNumberPrompt;
    @FXML
    private Text txtNumber;
    @FXML
    private Text txtNamePrompt;
    @FXML
    private Text txtName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        double paneWidth = MeasurementUtil.getNodeWidthByFactor(PANE_WIDTH_FACTOR);
        double paneHeight = MeasurementUtil.getNodeHeightByFactor(PANE_HEIGHT_FACTOR);
        double paneSpacing = MeasurementUtil.getNodeHeightByFactor(PANE_SPACING_FACTOR);
        vbUserInfo.setPrefWidth(paneWidth);
        vbUserInfo.setPrefHeight(paneHeight);
        vbUserInfo.setSpacing(paneSpacing);

        double photoWidth = MeasurementUtil.getNodeWidthByFactor(paneWidth, PHOTO_WIDTH_FACTOR);
        double photoHeight = MeasurementUtil.getNodeHeightByFactor(paneHeight, PHOTO_HEIGHT_FACTOR);
        ivPhoto.setFitWidth(photoWidth);
        ivPhoto.setFitHeight(photoHeight);

        double textHeight = MeasurementUtil.getNodeHeightByFactor(paneHeight, TEXT_HEIGHT_FACTOR);
        double textPromptHeight = MeasurementUtil.getNodeHeightByFactor(paneHeight, TEXT_PROMPT_HEIGHT_FACTOR);
        txtNumberPrompt.setFont(FontUtil.loadFont(textPromptHeight, false));
        txtNumber.setFont(FontUtil.loadFont(textHeight, true));
        txtNamePrompt.setFont(FontUtil.loadFont(textPromptHeight, false));
        txtName.setFont(FontUtil.loadFont(textHeight, true));
    }
}
