/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.splash.presenter;

import cn.edu.xmu.bip.ui.splash.view.LoadingPaneView;
import cn.edu.xmu.bip.ui.splash.view.PartnerPaneView;
import cn.edu.xmu.bip.ui.splash.view.ProductPaneView;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 启动界面
 *
 * @author luoxin
 * @version 2017-6-5
 */
public class SplashScenePresenter implements Initializable {
    //产品信息为父布局高的7/8
    private static final double PRODUCT_HEIGHT_FACTOR = (double) 7 / 8;
    //合作商和加载进度为父布局高的1/8
    private static final double PARTNER_HEIGHT_FACTOR = (double) 1 / 8;
    private static final double LOADING_HEIGHT_FACTOR = (double) 1 / 8;

    @FXML
    private BorderPane bpSplash;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        StackPane productPaneView = (StackPane) new ProductPaneView().getView();
        HBox partnerPaneView = (HBox) new PartnerPaneView().getView();
        HBox loadingPaneView = (HBox) new LoadingPaneView().getView();

        DoubleBinding productHeightBinding = Bindings.multiply(bpSplash.heightProperty(), PRODUCT_HEIGHT_FACTOR);
        DoubleBinding partnerHeightBinding = Bindings.multiply(bpSplash.heightProperty(), PARTNER_HEIGHT_FACTOR);
        DoubleBinding loadingHeightBinding = Bindings.multiply(bpSplash.heightProperty(), LOADING_HEIGHT_FACTOR);

        productPaneView.prefWidthProperty().bind(bpSplash.widthProperty());
        productPaneView.prefHeightProperty().bind(productHeightBinding);
        partnerPaneView.prefHeightProperty().bind(partnerHeightBinding);
        loadingPaneView.prefHeightProperty().bind(loadingHeightBinding);

        bpSplash.setTop(productPaneView);
        bpSplash.setLeft(partnerPaneView);
        bpSplash.setRight(loadingPaneView);
    }
}
