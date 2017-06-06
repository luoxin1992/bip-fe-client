/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.presenter;

import cn.edu.xmu.bip.util.FontUtil;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

/**
 * 启动界面-产品信息
 *
 * @author luoxin
 * @version 2017-6-4
 */
public class ProductPanePresenter implements Initializable {
    private static final double PANE_WIDTH_FACTOR = (double) 1;
    private static final double PANE_HEIGHT_FACTOR = (double) 7 / 8;
    //产品LOGO为父布局高的2/5
    private static final double LOGO_HEIGHT_FACTOR = (double) 2 / 5;
    //产品名称为父布局高的1/7
    private static final double NAME_HEIGHT_FACTOR = (double) 1 / 7;

    @FXML
    private StackPane spParent;
    @FXML
    private Pane paneAnimation;
    @FXML
    private ImageView ivLogo;
    @FXML
    private Label lblName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Group animation = createBubbleAnimation(spParent.prefWidthProperty(), spParent.prefHeightProperty());
        paneAnimation.getChildren().add(animation);
        paneAnimation.prefWidthProperty().bind(spParent.widthProperty());
        paneAnimation.prefHeightProperty().bind(spParent.heightProperty());

        DoubleBinding logoHeightBinding = Bindings.multiply(spParent.heightProperty(), LOGO_HEIGHT_FACTOR);
        ivLogo.fitHeightProperty().bind(logoHeightBinding);

        Callable<Font> nameFont = () -> FontUtil.loadFont(spParent.getHeight() * NAME_HEIGHT_FACTOR, true);
        ObjectBinding<Font> nameFontBinding = Bindings.createObjectBinding(nameFont, spParent.heightProperty());
        lblName.fontProperty().bind(nameFontBinding);
    }

    /**
     * 创建(覆盖在产品信息之下的)气泡动画
     *
     * @param widthProperty  动画区域宽属性
     * @param heightProperty 动画区域高属性
     * @return 动画区域
     */
    private Group createBubbleAnimation(DoubleProperty widthProperty, DoubleProperty heightProperty) {
        double width = widthProperty.doubleValue();
        double height = heightProperty.doubleValue();

        //彩色渐变
        Rectangle colors = new Rectangle(width, height,
                new LinearGradient(0.0, 1.0, 1.0, 0.0, true, CycleMethod.NO_CYCLE,
                        new Stop(0.00, Color.web("#f8bd55")),
                        new Stop(0.14, Color.web("#c0fe56")),
                        new Stop(0.29, Color.web("#5dfbc1")),
                        new Stop(0.43, Color.web("#64c2f8")),
                        new Stop(0.57, Color.web("#be4af7")),
                        new Stop(0.71, Color.web("#ed5fc2")),
                        new Stop(0.86, Color.web("#ef504c")),
                        new Stop(1.00, Color.web("#f2660f"))));
        colors.setBlendMode(BlendMode.OVERLAY);
        colors.widthProperty().bind(widthProperty);
        colors.heightProperty().bind(heightProperty);

        //模糊圆圈
        Group circles = new Group();
        circles.setEffect(new BoxBlur(10, 10, 3));
        for (int i = 0; i < 50; i++) {
            Circle circle = new Circle(50, Color.web("#ffffff", 0.05));
            circle.setStroke(Color.web("#ffffff", 0.16));
            circle.setStrokeType(StrokeType.OUTSIDE);
            circle.setStrokeWidth(4);
            circles.getChildren().add(circle);
        }

        //黑色蒙版
        Rectangle black = new Rectangle(width, height, Color.BLACK);
        black.widthProperty().bind(widthProperty);
        black.heightProperty().bind(heightProperty);

        Timeline timeline = new Timeline();
        timeline.setAutoReverse(true);
        timeline.setCycleCount(Timeline.INDEFINITE);
        for (Node circle : circles.getChildren()) {
            timeline.getKeyFrames().addAll(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(circle.translateXProperty(), Math.random() * width),
                            new KeyValue(circle.translateYProperty(), Math.random() * height)
                    ),
                    new KeyFrame(new Duration(30000),
                            new KeyValue(circle.translateXProperty(), Math.random() * width),
                            new KeyValue(circle.translateYProperty(), Math.random() * height)
                    )
            );
        }
        timeline.play();

        return new Group(new Group(black, circles), colors);
    }
}
