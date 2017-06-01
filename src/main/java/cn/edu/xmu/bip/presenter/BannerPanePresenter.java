/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.presenter;

import cn.edu.xmu.bip.util.FontUtil;
import cn.edu.xmu.bip.util.MeasurementUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 主界面-Banner
 *
 * @author luoxin
 * @version 2017-5-29
 */
public class BannerPanePresenter implements Initializable {
    //整个窗格占屏幕全宽、1/8高
    private static final double PANE_WIDTH_FACTOR = (double) 1;
    private static final double PANE_HEIGHT_FACTOR = (double) 1 / 8;
    //公司信息和产品信息分别占整个窗格2/3高和1/3高
    private static final double COMPANY_HEIGHT_FACTOR = (double) 2 / 3;
    private static final double PRODUCT_HEIGHT_FACTOR = (double) 1 / 3;
    //窄装饰线条占整个窗格全宽、1/15高
    private static final double RECTANGLE_NARROW_WIDTH_FACTOR = (double) 1;
    private static final double RECTANGLE_NARROW_HEIGHT_FACTOR = (double) 1 / 15;

    @FXML
    private AnchorPane apBanner;
    @FXML
    private HBox hbCompany;
    @FXML
    private ImageView ivCompany;
    @FXML
    private Text txtCompany;
    @FXML
    private Text txtProduct;
    @FXML
    private Rectangle rectNarrow;
    @FXML
    private Rectangle rectWide;
    @FXML
    private Polygon polyShelter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //父布局
        double paneWidth = MeasurementUtil.getNodeWidthByFactor(PANE_WIDTH_FACTOR);
        double paneHeight = MeasurementUtil.getNodeHeightByFactor(PANE_HEIGHT_FACTOR);
        apBanner.setPrefWidth(paneWidth);
        apBanner.setPrefHeight(paneHeight);

        //公司LOGO、名称、产品名称
        double companyHeight = MeasurementUtil.getNodeHeightByFactor(paneHeight, COMPANY_HEIGHT_FACTOR);
        double productHeight = MeasurementUtil.getNodeHeightByFactor(paneHeight, PRODUCT_HEIGHT_FACTOR);
        ivCompany.setFitHeight(companyHeight);
        txtCompany.setFont(FontUtil.loadFont(companyHeight, true));
        txtProduct.setFont(FontUtil.loadFont(productHeight, false));

        //底部装饰几何图形
        double rectNarrowWidth = MeasurementUtil.getNodeWidthByFactor(paneWidth, RECTANGLE_NARROW_WIDTH_FACTOR);
        double rectNarrowHeight = MeasurementUtil.getNodeHeightByFactor(paneHeight, RECTANGLE_NARROW_HEIGHT_FACTOR);
        rectNarrow.setWidth(rectNarrowWidth);
        rectNarrow.setHeight(rectNarrowHeight);
        //宽矩形用于覆盖右下角产品名称，故宽高依据其计算
        double rectWideWidth = txtProduct.getLayoutBounds().getWidth() + productHeight;
        double rectWideHeight = productHeight;
        rectWide.setWidth(rectWideWidth);
        rectWide.setHeight(rectWideHeight);
        double polyPoint1X = paneWidth - rectWideWidth;
        double polyPoint1Y = paneHeight - rectWideHeight;
        double polyPoint2X = paneWidth - rectWideWidth;
        double polyPoint2Y = paneHeight - rectNarrowHeight;
        double polyPoint3X = paneWidth - rectWideWidth + (rectWideHeight - rectNarrowHeight);
        double polyPoint3Y = paneHeight - rectWideHeight;
        polyShelter.getPoints().clear();
        polyShelter.getPoints().addAll(polyPoint1X, polyPoint1Y, polyPoint2X, polyPoint2Y, polyPoint3X, polyPoint3Y);

        //公司信息边距
        double margin = (paneHeight - companyHeight - rectNarrowHeight) / 2;
        hbCompany.setSpacing(margin);
        AnchorPane.setTopAnchor(hbCompany, margin);
        AnchorPane.setLeftAnchor(hbCompany, margin);
    }
}
