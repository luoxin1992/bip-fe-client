/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.main.presenter;

import cn.edu.xmu.bip.ui.main.model.CounterModel;
import cn.edu.xmu.bip.ui.main.model.PaneVisibleModel;
import cn.edu.xmu.bip.util.FontUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import javax.inject.Inject;
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
    //窗口开放间隔、边距为父布局高的1/8
    private static final double OPEN_SPACING_FACTOR = (double) 1 / 8;
    private static final double OPEN_PADDING_FACTOR = (double) 1 / 8;
    //编号为父布局高的3/8
    private static final double NUMBER_HEIGHT_FACTOR = (double) 3 / 8;
    //名称为父布局高的1/4
    private static final double NAME_HEIGHT_FACTOR = (double) 1 / 4;
    //窗口关闭为父布局高的3/16
    private static final double CLOSE_HEIGHT_FACTOR = (double) 3 / 16;

    @FXML
    private StackPane spParent;
    @FXML
    private VBox vbOpen;
    @FXML
    private Label lblNumber;
    @FXML
    private Label lblName;
    @FXML
    private Label lblClose;

    @Inject
    private CounterModel counterModel;
    @Inject
    private PaneVisibleModel paneVisibleModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindView();
        bindViewModel();
    }

    private void bindView() {
        DoubleBinding openSpacingBinding = Bindings.multiply(vbOpen.heightProperty(), OPEN_SPACING_FACTOR);
        vbOpen.spacingProperty().bind(openSpacingBinding);

        Callable<Insets> openPadding = () -> new Insets(vbOpen.getHeight() * OPEN_PADDING_FACTOR);
        ObjectBinding<Insets> openPaddingBinding = Bindings.createObjectBinding(openPadding, vbOpen.heightProperty());
        vbOpen.paddingProperty().bind(openPaddingBinding);

        Callable<Font> numberFont = () -> FontUtil.loadFont(spParent.getHeight() * NUMBER_HEIGHT_FACTOR, true);
        ObjectBinding<Font> numberFontBinding = Bindings.createObjectBinding(numberFont, spParent.heightProperty());
        lblNumber.fontProperty().bind(numberFontBinding);

        Callable<Font> nameFont = () -> FontUtil.loadFont(spParent.getHeight() * NAME_HEIGHT_FACTOR, true);
        ObjectBinding<Font> nameFontBinding = Bindings.createObjectBinding(nameFont, spParent.heightProperty());
        lblName.fontProperty().bind(nameFontBinding);

        Callable<Font> closeFont = () -> FontUtil.loadFont(spParent.getHeight() * CLOSE_HEIGHT_FACTOR, true);
        ObjectBinding<Font> closeFontBinding = Bindings.createObjectBinding(closeFont, spParent.heightProperty());
        lblClose.fontProperty().bind(closeFontBinding);
    }

    private void bindViewModel() {
        lblNumber.textProperty().bind(counterModel.numberProperty());
        lblName.textProperty().bind(counterModel.nameProperty());
        vbOpen.visibleProperty().bind(counterModel.closeProperty().not());
        lblClose.visibleProperty().bind(counterModel.closeProperty());
        spParent.visibleProperty().bind(paneVisibleModel.counterVisibleProperty());
    }
}
