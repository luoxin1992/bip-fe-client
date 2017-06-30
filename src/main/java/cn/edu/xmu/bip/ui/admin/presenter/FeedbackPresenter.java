/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.admin.presenter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

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
    private Button btnExtractLog;
    @FXML
    private TextArea taExtractLog;
    @FXML
    private Label lblSuggestionLength;
    @FXML
    private TextArea taSuggestion;
    @FXML
    private Button btnSubmit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //截图按钮点击事件
        btnScreenshot.setOnAction(event -> clickScreenshotButton());
        //提取按钮点击事件
        btnExtractLog.setOnAction(event -> clickExtractLogButton());
        //意见建议键入事件
        taSuggestion.textProperty().addListener((property, oldValue, newValue) ->
                typeInSuggestion(newValue));
        //提交按钮点击事件
        btnSubmit.setOnAction(event -> clickSubmitButton());
    }

    private void clickScreenshotButton() {
        //TODO
    }

    private void clickExtractLogButton() {
        //TODO
    }

    private void typeInSuggestion(String text) {
        lblSuggestionLength.setText(String.valueOf(text.length()));
        if (text.length() >= SUGGESTION_MAX_LENGTH) {
            taSuggestion.setText(text.substring(0, SUGGESTION_MAX_LENGTH));
        }
    }

    private void clickSubmitButton() {
        //TODO
    }
}