/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.main.presenter;

import cn.edu.xmu.bip.ui.main.view.BannerPaneView;
import cn.edu.xmu.bip.ui.main.view.CounterPaneView;
import cn.edu.xmu.bip.ui.main.view.ClockPaneView;
import cn.edu.xmu.bip.ui.main.view.DeveloperPaneView;
import cn.edu.xmu.bip.ui.main.view.MessagePaneView;
import cn.edu.xmu.bip.ui.main.view.UserPaneView;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 主界面
 *
 * @author luoxin
 * @version 2017-6-6
 */
public class MainScenePresenter implements Initializable {
    //Banner窗格为父布局高的1/8
    private static final double BANNER_HEIGHT_FACTOR = (double) 1 / 8;
    //日期时间/客户窗格(左侧上)为父布局宽的1/3、高的19/24
    private static final double DATETIME_WIDTH_FACTOR = (double) 1 / 3;
    private static final double DATETIME_HEIGHT_FACTOR = (double) 19 / 24;
    private static final double USER_WIDTH_FACTOR = (double) 1 / 3;
    private static final double USER_HEIGHT_FACTOR = (double) 19 / 24;
    //开发者窗格(左侧下)为父布局宽的1/3、高的1/12
    private static final double DEVELOPER_WIDTH_FACTOR = (double) 1 / 3;
    private static final double DEVELOPER_HEIGHT_FACTOR = (double) 1 / 12;
    //窗口/消息窗格(右侧)为父布局宽的2/3、高的7/8
    private static final double COUNTER_WIDTH_FACTOR = (double) 2 / 3;
    private static final double COUNTER_HEIGHT_FACTOR = (double) 7 / 8;
    private static final double MESSAGE_WIDTH_FACTOR = (double) 2 / 3;
    private static final double MESSAGE_HEIGHT_FACTOR = (double) 7 / 8;

    @FXML
    private BorderPane bpMain;
    @FXML
    private StackPane spLeft;
    @FXML
    private VBox vbLeft;
    @FXML
    private StackPane spCenter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AnchorPane bannerPaneView = (AnchorPane) new BannerPaneView().getView();
        VBox datetimePaneView = (VBox) new ClockPaneView().getView();
        VBox userPaneView = (VBox) new UserPaneView().getView();
        StackPane developerPaneView = (StackPane) new DeveloperPaneView().getView();
        VBox counterPaneView = (VBox) new CounterPaneView().getView();
        VBox messagePaneView = (VBox) new MessagePaneView().getView();

        //datetimePaneView.setVisible(false);
        userPaneView.setVisible(false);
        //developerPaneView.setVisible(false);
        //counterPaneView.setVisible(false);
        messagePaneView.setVisible(false);

        DoubleBinding bannerHeightBinding = Bindings.multiply(bpMain.heightProperty(), BANNER_HEIGHT_FACTOR);
        DoubleBinding datetimeWidthBinding = Bindings.multiply(bpMain.widthProperty(), DATETIME_WIDTH_FACTOR);
        DoubleBinding datetimeHeightBinding = Bindings.multiply(bpMain.heightProperty(), DATETIME_HEIGHT_FACTOR);
        DoubleBinding userWidthBinding = Bindings.multiply(bpMain.widthProperty(), USER_WIDTH_FACTOR);
        DoubleBinding userHeightBinding = Bindings.multiply(bpMain.heightProperty(), USER_HEIGHT_FACTOR);
        DoubleBinding developerWidthBinding = Bindings.multiply(bpMain.widthProperty(), DEVELOPER_WIDTH_FACTOR);
        DoubleBinding developerHeightBinding = Bindings.multiply(bpMain.heightProperty(), DEVELOPER_HEIGHT_FACTOR);
        DoubleBinding counterWidthBinding = Bindings.multiply(bpMain.widthProperty(), COUNTER_WIDTH_FACTOR);
        DoubleBinding counterHeightBinding = Bindings.multiply(bpMain.heightProperty(), COUNTER_HEIGHT_FACTOR);
        DoubleBinding messageWidthBinding = Bindings.multiply(bpMain.widthProperty(), MESSAGE_WIDTH_FACTOR);
        DoubleBinding messageHeightBinding = Bindings.multiply(bpMain.heightProperty(), MESSAGE_HEIGHT_FACTOR);

        bannerPaneView.prefWidthProperty().bind(bpMain.widthProperty());
        bannerPaneView.prefHeightProperty().bind(bannerHeightBinding);
        datetimePaneView.prefWidthProperty().bind(datetimeWidthBinding);
        datetimePaneView.prefHeightProperty().bind(datetimeHeightBinding);
        userPaneView.prefWidthProperty().bind(userWidthBinding);
        userPaneView.prefHeightProperty().bind(userHeightBinding);
        developerPaneView.prefWidthProperty().bind(developerWidthBinding);
        developerPaneView.prefHeightProperty().bind(developerHeightBinding);
        counterPaneView.prefWidthProperty().bind(counterWidthBinding);
        counterPaneView.prefHeightProperty().bind(counterHeightBinding);
        messagePaneView.prefWidthProperty().bind(messageWidthBinding);
        messagePaneView.prefHeightProperty().bind(messageHeightBinding);

        spLeft.getChildren().addAll(datetimePaneView, userPaneView);
        vbLeft.getChildren().addAll(developerPaneView);
        spCenter.getChildren().addAll(counterPaneView, messagePaneView);

        bpMain.setTop(bannerPaneView);
        bpMain.setLeft(vbLeft);
        bpMain.setCenter(spCenter);
    }
}
