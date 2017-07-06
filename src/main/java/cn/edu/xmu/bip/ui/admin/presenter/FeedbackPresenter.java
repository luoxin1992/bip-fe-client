/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.admin.presenter;

import cn.edu.xmu.bip.ui.admin.model.FeedbackModel;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * 管理工具-反馈问题
 *
 * @author luoxin
 * @version 2017-6-26
 */
public class FeedbackPresenter implements Initializable {
    private static final int SUGGESTION_MAX_LENGTH = 500;

    @FXML
    private Button btnScreenshot;
    @FXML
    private ImageView ivScreenshot;
    @FXML
    private Button btnRuntimeLog;
    @FXML
    private TextArea taRuntimeLog;
    @FXML
    private Label lblSuggestion;
    @FXML
    private TextArea taSuggestion;
    @FXML
    private Button btnSubmit;

    @Inject
    private FeedbackModel feedbackModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindViewModel();
        bindEvent();
    }

    private void bindViewModel() {
        ivScreenshot.imageProperty().bindBidirectional(feedbackModel.screenshotProperty());
        taRuntimeLog.textProperty().bindBidirectional(feedbackModel.logProperty());
        taSuggestion.textProperty().bindBidirectional(feedbackModel.suggestionProperty());
    }

    private void bindEvent() {
        //更新意见建议键入字数
        lblSuggestion.textProperty().bind(Bindings.createStringBinding(() ->
                String.valueOf(StringUtils.length(taSuggestion.textProperty().get())), taSuggestion.textProperty()));
        //限制意见建议键入字数
        taSuggestion.textProperty().addListener((property, oldValue, newValue) -> typeInSuggestion(newValue));
        //截图
        btnScreenshot.setOnAction(event -> takeScreenshot());
        //提取日志
        btnRuntimeLog.setOnAction(event -> extractRuntimeLog());
        //提交反馈
        btnSubmit.setOnAction(event -> submitFeedback());
    }

    private void typeInSuggestion(String text) {
        if (text.length() >= SUGGESTION_MAX_LENGTH) {
            taSuggestion.setText(text.substring(0, SUGGESTION_MAX_LENGTH));
        }
    }

    private void takeScreenshot() {
        //TODO
    }

    private void extractRuntimeLog() {
        //TODO
    }

    private void submitFeedback() {
        //TODO
    }
}