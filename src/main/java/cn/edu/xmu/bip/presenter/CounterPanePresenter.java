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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

/**
 * 主界面-窗口
 *
 * @author luoxin
 * @version 2017-6-3
 */
public class CounterPanePresenter implements Initializable {
    //间隔、边距为父布局高的1/8
    private static final double PARENT_SPACING_FACTOR = (double) 1 / 8;
    private static final double PARENT_PADDING_FACTOR = (double) 1 / 8;
    //编号为父布局高的3/8
    private static final double NUMBER_HEIGHT_FACTOR = (double) 3 / 8;
    //名称为父布局高的1/4
    private static final double NAME_HEIGHT_FACTOR = (double) 1 / 4;

    @FXML
    private VBox vbParent;
    @FXML
    private Label lblNumber;
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

        Callable<Font> numberFont = () -> FontUtil.loadFont(vbParent.getHeight() * NUMBER_HEIGHT_FACTOR, true);
        ObjectBinding<Font> numberFontBinding = Bindings.createObjectBinding(numberFont, vbParent.heightProperty());
        lblNumber.fontProperty().bind(numberFontBinding);

        Callable<Font> nameFont = () -> FontUtil.loadFont(vbParent.getHeight() * NAME_HEIGHT_FACTOR, true);
        ObjectBinding<Font> nameFontBinding = Bindings.createObjectBinding(nameFont, vbParent.heightProperty());
        lblName.fontProperty().bind(nameFontBinding);
    }
}
