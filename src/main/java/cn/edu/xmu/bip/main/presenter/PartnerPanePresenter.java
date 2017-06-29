/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.main.presenter;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 启动界面-合作商Logo
 *
 * @author luoxin
 * @version 2017-5-27
 */
public class PartnerPanePresenter implements Initializable {
    //合作商图片为父布局高的2/3
    private static final double PARTNER_HEIGHT_FACTOR = (double) 2 / 3;

    @FXML
    private HBox hbParent;
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
        DoubleBinding partnerHeightBinding = Bindings.multiply(hbParent.heightProperty(), PARTNER_HEIGHT_FACTOR);
        ivCrossmatch.fitHeightProperty().bind(partnerHeightBinding);
        ivDivider1.fitHeightProperty().bind(partnerHeightBinding);
        ivIflytek.fitHeightProperty().bind(partnerHeightBinding);
        ivDivider2.fitHeightProperty().bind(partnerHeightBinding);
        ivMtyun.fitHeightProperty().bind(partnerHeightBinding);
    }
}
