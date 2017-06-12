/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.presenter;

import cn.edu.xmu.bip.util.FontUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

/**
 * 主界面-用户
 *
 * @author luoxin
 * @version 2017-5-29
 */
public class UserPanePresenter implements Initializable {
    //间隔为父布局高的1/24，边距为父布局高的1/16
    private static final double PARENT_SPACING_FACTOR = (double) 1 / 24;
    private static final double PARENT_PADDING_FACTOR = (double) 1 / 16;
    //照片为父布局高的13/24
    private static final double PHOTO_HEIGHT_FACTOR = (double) 13 / 24;
    //编号、姓名为父布局高的1/8，左侧提示为父布局高的1/16
    private static final double USER_HEIGHT_FACTOR = (double) 1 / 8;
    private static final double PROMPT_HEIGHT_FACTOR = (double) 1 / 16;

    @FXML
    private VBox vbParent;
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
        DoubleBinding parentSpacingBinding = Bindings.multiply(vbParent.heightProperty(), PARENT_SPACING_FACTOR);
        vbParent.spacingProperty().bind(parentSpacingBinding);

        Callable<Insets> parentPadding = () -> new Insets(vbParent.getHeight() * PARENT_PADDING_FACTOR);
        ObjectBinding<Insets> parentPaddingBinding =
                Bindings.createObjectBinding(parentPadding, vbParent.heightProperty());
        vbParent.paddingProperty().bind(parentPaddingBinding);

        DoubleBinding photoHeightBinding = Bindings.multiply(vbParent.heightProperty(), PHOTO_HEIGHT_FACTOR);
        ivPhoto.fitWidthProperty().bind(vbParent.widthProperty());
        ivPhoto.fitHeightProperty().bind(photoHeightBinding);

        Callable<Font> promptFont = () -> FontUtil.loadFont(vbParent.getHeight() * PROMPT_HEIGHT_FACTOR, false);
        ObjectBinding<Font> promptFontBinding = Bindings.createObjectBinding(promptFont, vbParent.heightProperty());
        lblNumberPrompt.fontProperty().bind(promptFontBinding);
        lblNamePrompt.fontProperty().bind(promptFontBinding);

        Callable<Font> userFont = () -> FontUtil.loadFont(vbParent.getHeight() * USER_HEIGHT_FACTOR, true);
        ObjectBinding<Font> userFontBinding = Bindings.createObjectBinding(userFont, vbParent.heightProperty());
        lblNumber.fontProperty().bind(userFontBinding);
        lblName.fontProperty().bind(userFontBinding);
    }
}
