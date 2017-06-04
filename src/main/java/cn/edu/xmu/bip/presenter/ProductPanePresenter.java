/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.presenter;

import cn.edu.xmu.bip.util.FontUtil;
import cn.edu.xmu.bip.util.MeasurementUtil;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 启动界面-产品信息
 *
 * @author luoxin
 * @version 2017-6-4
 */
public class ProductPanePresenter implements Initializable {
    private static final double PANE_WIDTH_FACTOR = (double) 1;
    private static final double PANE_HEIGHT_FACTOR = (double) 7 / 8;
    private static final double IMAGE_HEIGHT_FACTOR = (double) 2 / 5;
    private static final double LABEL_HEIGHT_FACTOR = (double) 1 / 7;

    @FXML
    private StackPane spProduct;
    @FXML
    private Pane paneAnimation;
    @FXML
    private ImageView ivLogo;
    @FXML
    private Label lblName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        double paneWidth = MeasurementUtil.getNodeWidthByFactor(PANE_WIDTH_FACTOR);
        double paneHeight = MeasurementUtil.getNodeHeightByFactor(PANE_HEIGHT_FACTOR);
        spProduct.setPrefWidth(paneWidth);
        spProduct.setPrefHeight(paneHeight);

        paneAnimation.getChildren().add(bubbleAnimation(paneWidth, paneHeight));

        double imageHeight = MeasurementUtil.getNodeSizeInParentByFactor(paneHeight, IMAGE_HEIGHT_FACTOR);
        ivLogo.setFitHeight(imageHeight);

        double labelHeight = MeasurementUtil.getNodeSizeInParentByFactor(paneHeight, LABEL_HEIGHT_FACTOR);
        lblName.setFont(FontUtil.loadFont(labelHeight, true));
    }

    private Group bubbleAnimation(double width, double height) {
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

        Group circles = new Group();
        circles.setEffect(new BoxBlur(10, 10, 3));
        for (int i = 0; i < 50; i++) {
            Circle circle = new Circle(50, Color.web("#ffffff", 0.05));
            circle.setStroke(Color.web("#ffffff", 0.16));
            circle.setStrokeType(StrokeType.OUTSIDE);
            circle.setStrokeWidth(4);
            circles.getChildren().add(circle);
        }

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

        return new Group(new Group(new Rectangle(width, height, Color.BLACK), circles), colors);
    }
}
