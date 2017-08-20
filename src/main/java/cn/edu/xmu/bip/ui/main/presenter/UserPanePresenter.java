/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.main.presenter;

import cn.edu.xmu.bip.ui.main.model.PaneVisibleModel;
import cn.edu.xmu.bip.ui.main.model.UserModel;
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

import javax.inject.Inject;
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
    //照片为父布局高的13/24，宽的7/8
    //照片外侧的容器用于固定照片显示区域
    private static final double PHOTO_WIDTH_FACTOR = (double) 7 / 8;
    private static final double PHOTO_HEIGHT_FACTOR = (double) 13 / 24;
    //编号、姓名为父布局高的1/8，左侧提示为父布局高的1/16
    private static final double USER_HEIGHT_FACTOR = (double) 1 / 8;
    private static final double PROMPT_HEIGHT_FACTOR = (double) 1 / 16;

    @FXML
    private VBox vbParent;
    @FXML
    private VBox vbPhoto;
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

    @Inject
    private UserModel userModel;
    @Inject
    private PaneVisibleModel paneVisibleModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindView();
        bindViewModel();
    }

    private void bindView() {
        DoubleBinding parentSpacingBinding = Bindings.multiply(vbParent.heightProperty(), PARENT_SPACING_FACTOR);
        vbParent.spacingProperty().bind(parentSpacingBinding);

        Callable<Insets> parentPadding = () -> new Insets(vbParent.getHeight() * PARENT_PADDING_FACTOR);
        ObjectBinding<Insets> parentPaddingBinding = Bindings.createObjectBinding(parentPadding, vbParent.heightProperty());
        vbParent.paddingProperty().bind(parentPaddingBinding);

        DoubleBinding photoWidthBinding = Bindings.multiply(vbParent.widthProperty(), PHOTO_WIDTH_FACTOR);
        DoubleBinding photoHeightBinding = Bindings.multiply(vbParent.heightProperty(), PHOTO_HEIGHT_FACTOR);
        vbPhoto.prefWidthProperty().bind(photoWidthBinding);
        vbPhoto.prefHeightProperty().bind(photoHeightBinding);
        ivPhoto.fitWidthProperty().bind(photoWidthBinding);
        ivPhoto.fitHeightProperty().bind(photoHeightBinding);

        Callable<Font> promptLabelFont = () -> FontUtil.loadFont(vbParent.getHeight() * PROMPT_HEIGHT_FACTOR, false);
        ObjectBinding<Font> promptLabelFontBinding = Bindings.createObjectBinding(promptLabelFont, vbParent.heightProperty());
        lblNumberPrompt.fontProperty().bind(promptLabelFontBinding);
        lblNamePrompt.fontProperty().bind(promptLabelFontBinding);

        Callable<Font> labelFont = () -> FontUtil.loadFont(vbParent.getHeight() * USER_HEIGHT_FACTOR, true);
        ObjectBinding<Font> labelFontBinding = Bindings.createObjectBinding(labelFont, vbParent.heightProperty());
        lblNumber.fontProperty().bind(labelFontBinding);
        lblName.fontProperty().bind(labelFontBinding);
    }

    private void bindViewModel() {
        lblNumber.textProperty().bind(userModel.numberProperty());
        lblName.textProperty().bind(userModel.nameProperty());
        ivPhoto.imageProperty().bind(userModel.photoProperty());
        vbParent.visibleProperty().bind(paneVisibleModel.userVisibleProperty());
    }
}
