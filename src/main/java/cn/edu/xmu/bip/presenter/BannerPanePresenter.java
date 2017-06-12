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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

/**
 * 主界面-Banner
 *
 * @author luoxin
 * @version 2017-5-29
 */
public class BannerPanePresenter implements Initializable {
    //公司LOGO、名称为父布局高的2/3
    private static final double COMPANY_HEIGHT_FACTOR = (double) 2 / 3;
    //产品名称为父布局高的1/3
    private static final double PRODUCT_HEIGHT_FACTOR = (double) 1 / 3;
    //装饰矩形为父布局高的1/15
    private static final double DECORATOR_HEIGHT_FACTOR = (double) 1 / 15;

    @FXML
    private AnchorPane apParent;
    @FXML
    private HBox hbCompany;
    @FXML
    private ImageView ivCompany;
    @FXML
    private Label lblCompany;
    @FXML
    private Rectangle rectDecorator;
    @FXML
    private Polygon polyShelter;
    @FXML
    private Label lblProduct;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //公司信息
        hbCompany.prefHeightProperty().bind(apParent.heightProperty());

        //内间距
        Callable<Double> companySpacing = () ->
                (apParent.getHeight() - ivCompany.getFitHeight() - rectDecorator.getHeight()) / 2;
        DoubleBinding companySpacingBinding = Bindings.createDoubleBinding(companySpacing,
                apParent.heightProperty(), ivCompany.fitHeightProperty(), rectDecorator.heightProperty());
        hbCompany.spacingProperty().bind(companySpacingBinding);

        //外(左)边距
        Callable<Insets> companyPadding = () ->
                new Insets(0, 0, 0, (apParent.getHeight() - ivCompany.getFitHeight() - rectDecorator.getHeight()) / 2);
        ObjectBinding<Insets> companyPaddingBinding = Bindings.createObjectBinding(companyPadding,
                apParent.heightProperty(), ivCompany.fitHeightProperty(), rectDecorator.heightProperty());
        hbCompany.paddingProperty().bind(companyPaddingBinding);

        //公司LOGO
        DoubleBinding logoHeightBinding = Bindings.multiply(apParent.heightProperty(), COMPANY_HEIGHT_FACTOR);
        ivCompany.fitHeightProperty().bind(logoHeightBinding);

        //公司名称
        Callable<Font> companyFont = () -> FontUtil.loadFont(apParent.getHeight() * COMPANY_HEIGHT_FACTOR, true);
        ObjectBinding<Font> companyFontBinding = Bindings.createObjectBinding(companyFont, apParent.heightProperty());
        lblCompany.fontProperty().bind(companyFontBinding);

        //底部用于装饰的矩形
        Callable<Double> rectDecoratorHeight = () -> Math.rint(apParent.getHeight() * DECORATOR_HEIGHT_FACTOR);
        DoubleBinding rectDecoratorHeightBinding =
                Bindings.createDoubleBinding(rectDecoratorHeight, apParent.heightProperty());
        rectDecorator.widthProperty().bind(apParent.widthProperty());
        rectDecorator.heightProperty().bind(rectDecoratorHeightBinding);

        //产品名称
        Callable<Font> productNameFont = () -> FontUtil.loadFont(apParent.getHeight() * PRODUCT_HEIGHT_FACTOR, false);
        ObjectBinding<Font> productNameFontBinding =
                Bindings.createObjectBinding(productNameFont, apParent.heightProperty());
        lblProduct.fontProperty().bind(productNameFontBinding);
        lblProduct.fontProperty().addListener((property, oldValue, newValue) -> {
            //重绘产品名称左上角用于遮挡的三角形
            polyShelter.getPoints().set(2, lblProduct.getFont().getSize() - rectDecorator.getHeight());
            polyShelter.getPoints().set(5, lblProduct.getFont().getSize() - rectDecorator.getHeight());
        });
    }
}
