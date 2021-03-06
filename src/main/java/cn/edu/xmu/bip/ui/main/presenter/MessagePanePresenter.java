/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.main.presenter;

import cn.edu.xmu.bip.ui.main.model.MessageModel;
import cn.edu.xmu.bip.ui.main.model.PaneVisibleModel;
import cn.edu.xmu.bip.util.FontUtil;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

/**
 * 主界面-消息
 *
 * @author luoxin
 * @version 2017-6-3
 */
public class MessagePanePresenter implements Initializable {
    //间隔、边距为父布局的1/16
    private static final double PARENT_SPACING_FACTOR = (double) 1 / 16;
    private static final double PARENT_PADDING_FACTOR = (double) 1 / 16;
    //图片宽、高为父布局的11/16
    private static final double IMAGE_WIDTH_FACTOR = (double) 11 / 16;
    private static final double IMAGE_HEIGHT_FACTOR = (double) 11 / 16;
    //内容高为父布局的1/8
    private static final double CONTENT_HEIGHT_FACTOR = (double) 1 / 8;

    @FXML
    private VBox vbParent;
    @FXML
    private VBox vbImage;
    @FXML
    private ImageView ivImage;
    @FXML
    private ScrollPane spContent;
    @FXML
    private Label lblContent;

    @Inject
    private MessageModel messageModel;
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

        DoubleBinding imageWidthBinding = Bindings.multiply(vbParent.widthProperty(), IMAGE_WIDTH_FACTOR);
        DoubleBinding imageHeightBinding = Bindings.multiply(vbParent.heightProperty(), IMAGE_HEIGHT_FACTOR);
        vbImage.prefWidthProperty().bind(imageWidthBinding);
        vbImage.prefHeightProperty().bind(imageHeightBinding);
        ivImage.fitWidthProperty().bind(imageWidthBinding);
        ivImage.fitHeightProperty().bind(imageHeightBinding);

        Callable<Font> contentFont = () -> FontUtil.loadFont(vbParent.getHeight() * CONTENT_HEIGHT_FACTOR, false);
        ObjectBinding<Font> contentFontBinding = Bindings.createObjectBinding(contentFont, vbParent.heightProperty());
        lblContent.fontProperty().bind(contentFontBinding);

        //监听label宽度变化，当宽度超过父布局scrollPane时，自动移动滚动条，形成跑马灯效果
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        lblContent.widthProperty().addListener(observable -> rerunAnimation(timeline));
    }

    private void bindViewModel() {
        ivImage.imageProperty().bind(messageModel.imageProperty());
        lblContent.textProperty().bind(messageModel.contentProperty());
        vbParent.visibleProperty().bind(paneVisibleModel.messageVisibleProperty());
    }

    /**
     * 消息内容Label跑马灯动画
     *
     * @param timeline 时间线
     */
    private void rerunAnimation(Timeline timeline) {
        //停止正在播放的动画
        if (timeline.getStatus() == Timeline.Status.RUNNING) {
            timeline.stop();
        }
        //计算播放时间(ms)
        double scrollWidth = lblContent.getWidth() - spContent.getViewportBounds().getWidth();
        if (scrollWidth <= 0) {
            //无滚动条，不需要滚动
            return;
        }
        //动画开始后，在X秒内正向滚动到尾，等待X/2秒，再在X秒内反向滚动到头，再等待X/2秒，总耗时3X秒，以此循环
        //X的算法为等待滚动距离(px)×3
        int playTime = (int) (scrollWidth * 3);
        int waitTime = playTime / 2;
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(0), new KeyValue(spContent.hvalueProperty(), 0)),
                new KeyFrame(Duration.millis(playTime), new KeyValue(spContent.hvalueProperty(), 1)),
                new KeyFrame(Duration.millis(playTime + waitTime), new KeyValue(spContent.hvalueProperty(), 1)),
                new KeyFrame(Duration.millis(playTime + waitTime + playTime), new KeyValue(spContent.hvalueProperty(), 0)),
                new KeyFrame(Duration.millis(playTime + waitTime + playTime + waitTime), new KeyValue(spContent.hvalueProperty(), 0))
        );
        timeline.play();
    }
}
