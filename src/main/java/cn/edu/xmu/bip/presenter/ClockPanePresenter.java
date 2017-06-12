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
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

/**
 * 主界面-时钟
 *
 * @author luoxin
 * @version 2017-5-28
 */
public class ClockPanePresenter implements Initializable {
    //间隔、边距为父布局高的1/27
    private static final double PARENT_SPACING_FACTOR = (double) 1 / 30;
    private static final double PARENT_PADDING_FACTOR = (double) 1 / 15;
    //钟面半径为父布局高的3/5，边框宽度为钟面半径的1/30
    private static final double FACE_RADIUS_FACTOR = (double) 3 / 5;
    private static final double FACE_STROKE_WIDTH_FACTOR = (double) 1 / 30;
    //主轴半径为钟面半径的1/20
    private static final double SPINDLE_RADIUS_FACTOR = (double) 1 / 20;
    //长、短刻度长度为钟面半径的1/10、1/20、宽度为钟面半径的1/30
    private static final double LONG_SCALE_LENGTH_FACTOR = (double) 1 / 10;
    private static final double SHORT_SCALE_LENGTH_FACTOR = (double) 1 / 20;
    private static final double SCALE_STROKE_WIDTH_FACTOR = (double) 1 / 30;
    //时针、分针、秒针长度为钟面半径的4/10、5/10、8/10，宽度为钟面半径的3/50、2/50、3/50
    private static final double HOUR_HAND_LENGTH_FACTOR = (double) 4 / 10;
    private static final double MINUTE_HAND_LENGTH_FACTOR = (double) 6 / 10;
    private static final double SECOND_HAND_LENGTH_FACTOR = (double) 8 / 10;
    private static final double HOUR_HAND_STROKE_WIDTH_FACTOR = (double) 3 / 50;
    private static final double MINUTE_HAND_STROKE_WIDTH_FACTOR = (double) 2 / 50;
    private static final double SECOND_HAND_STROKE_WIDTH_FACTOR = (double) 1 / 50;
    //日期、时间为父布局高的1/10
    private static final double DATE_TIME_HEIGHT_FACTOR = (double) 1 / 10;

    @FXML
    private VBox vbParent;
    @FXML
    private Circle cirFace;
    @FXML
    private Group grpScale;
    @FXML
    private Circle cirSpindle;
    @FXML
    private Line lnSecond;
    @FXML
    private Line lnMinute;
    @FXML
    private Line lnHour;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblTime;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DoubleBinding parentSpacingBinding = Bindings.multiply(vbParent.heightProperty(), PARENT_SPACING_FACTOR);
        vbParent.spacingProperty().bind(parentSpacingBinding);

        Callable<Insets> parentPadding = () -> new Insets(vbParent.getHeight() * PARENT_PADDING_FACTOR);
        ObjectBinding<Insets> parentPaddingBinding =
                Bindings.createObjectBinding(parentPadding, vbParent.heightProperty());
        vbParent.paddingProperty().bind(parentPaddingBinding);

        DoubleBinding faceRadiusBinding = Bindings.multiply(vbParent.heightProperty(), FACE_RADIUS_FACTOR).divide(2);
        cirFace.radiusProperty().bind(faceRadiusBinding);

        DoubleBinding faceStrokeWidthBinding = Bindings.multiply(cirFace.radiusProperty(), FACE_STROKE_WIDTH_FACTOR);
        cirFace.strokeWidthProperty().bind(faceStrokeWidthBinding);

        //钟面半径变化时重绘刻度
        cirFace.radiusProperty().addListener((property, oldValue, newValue) -> repaintScale());

        //主轴半径
        DoubleBinding spindleRadiusBinding = Bindings.multiply(cirFace.radiusProperty(), SPINDLE_RADIUS_FACTOR);
        cirSpindle.radiusProperty().bind(spindleRadiusBinding);

        //时针长度和宽度
        DoubleBinding hourHandLengthBinding =
                Bindings.multiply(cirFace.radiusProperty(), HOUR_HAND_LENGTH_FACTOR).multiply(-1);
        lnHour.endYProperty().bind(hourHandLengthBinding);

        DoubleBinding hourHandStrokeWidthBinding =
                Bindings.multiply(cirFace.radiusProperty(), HOUR_HAND_STROKE_WIDTH_FACTOR);
        lnHour.strokeWidthProperty().bind(hourHandStrokeWidthBinding);

        //分针长度和宽度
        DoubleBinding minuteHandLengthBinding =
                Bindings.multiply(cirFace.radiusProperty(), MINUTE_HAND_LENGTH_FACTOR).multiply(-1);
        lnMinute.endYProperty().bind(minuteHandLengthBinding);

        DoubleBinding minuteHandStrokeWidthBinding =
                Bindings.multiply(cirFace.radiusProperty(), MINUTE_HAND_STROKE_WIDTH_FACTOR);
        lnMinute.strokeWidthProperty().bind(minuteHandStrokeWidthBinding);

        //秒针长度和宽度
        DoubleBinding secondHandLengthBinding =
                Bindings.multiply(cirFace.radiusProperty(), SECOND_HAND_LENGTH_FACTOR).multiply(-1);
        lnSecond.endYProperty().bind(secondHandLengthBinding);

        DoubleBinding secondHandStrokeWidthBinding =
                Bindings.multiply(cirFace.radiusProperty(), SECOND_HAND_STROKE_WIDTH_FACTOR);
        lnSecond.strokeWidthProperty().bind(secondHandStrokeWidthBinding);

        Callable<Font> datetimeFont = () ->
                FontUtil.loadFont(vbParent.getHeight() * DATE_TIME_HEIGHT_FACTOR, false);
        ObjectBinding<Font> datetimeFontBinding = Bindings.createObjectBinding(datetimeFont, vbParent.heightProperty());
        lblDate.fontProperty().bind(datetimeFontBinding);
        lblTime.fontProperty().bind(datetimeFontBinding);
    }

    /**
     * 重绘钟面刻度
     */
    private void repaintScale() {
        List<Node> nodes = grpScale.getChildren();
        for (int i = 0; i < 60; i++) {
            Line line = (Line) nodes.get(i);
            if (i % 5 == 0) {
                line.setStartY(-cirFace.getRadius() + cirFace.getRadius() * LONG_SCALE_LENGTH_FACTOR);
            } else {
                line.setStartY(-cirFace.getRadius() + cirFace.getRadius() * SHORT_SCALE_LENGTH_FACTOR);
            }
            line.setEndY(-cirFace.getRadius());
            line.setStrokeWidth(cirFace.getRadius() * SCALE_STROKE_WIDTH_FACTOR);
        }
    }

    /**
     * 根据给定时间，重绘(旋转)时分秒针
     *
     * @param hour   时
     * @param minute 分
     * @param second 秒
     */
    private void repaintHand(int hour, int minute, int second) {
        double hourDegree = (hour % 12 + minute / (double) 60 + second / (double) 3600) * 30;
        ((Rotate) lnHour.getTransforms().get(0)).setAngle(hourDegree);

        double minuteDegree = (minute + second / (double) 60) * 6;
        ((Rotate) lnMinute.getTransforms().get(0)).setAngle(minuteDegree);

        double secondDegree = second * 6;
        ((Rotate) lnSecond.getTransforms().get(0)).setAngle(secondDegree);
    }
}
