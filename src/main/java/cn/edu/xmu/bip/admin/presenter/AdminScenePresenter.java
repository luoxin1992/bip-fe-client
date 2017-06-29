/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.admin.presenter;

import cn.com.lx1992.lib.client.constant.RegExpConstant;
import cn.com.lx1992.lib.client.dto.NICAddressDTO;
import cn.com.lx1992.lib.client.util.NativeUtil;
import cn.edu.xmu.bip.admin.constant.AdminConstant;
import cn.edu.xmu.bip.common.constant.CommonConstant;
import cn.edu.xmu.bip.admin.dto.BackendEnvDTO;
import cn.edu.xmu.bip.admin.service.AdminService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * 管理工具
 *
 * @author luoxin
 * @version 2017-6-26
 */
public class AdminScenePresenter implements Initializable {
    @FXML
    private TabPane tpParent;
    /**
     * 参数设置Tab-后端环境
     */
    @FXML
    private ToggleGroup tgBackendEnv;
    @FXML
    private RadioButton rbBackendRd;
    @FXML
    private RadioButton rbBackendQa;
    @FXML
    private RadioButton rbBackendProd;
    @FXML
    private RadioButton rbBackendCustom;
    @FXML
    private TextField tfBackendApi;
    @FXML
    private TextField tfBackendWs;
    @FXML
    private Button btnBackendSave;
    @FXML
    private Label lblBackendHint;
    /**
     * 参数设置Tab-窗口信息
     */
    @FXML
    private TextField tfCounterNumber;
    @FXML
    private TextField tfCounterName;
    @FXML
    private ChoiceBox<String> cbCounterNic;
    @FXML
    private Button btnCounterRefresh;
    @FXML
    private TextField tfCounterMac;
    @FXML
    private TextField tfCounterIp;
    @FXML
    private Button btnCounterSubmit;
    @FXML
    private Label lblCounterHint;
    //数据浏览Tab
    /**
     * 反馈问题Tab
     */
    @FXML
    private Button btnFeedbackScreenshot;
    @FXML
    private ImageView ivFeedbackScreenshot;
    @FXML
    private Button btnFeedbackExtractLog;
    @FXML
    private TextArea taFeedbackExtractLog;
    @FXML
    private Label lblFeedbackSuggestionLength;
    @FXML
    private TextArea taFeedbackSuggestion;
    @FXML
    private Button btnFeedbackSubmit;

    @Inject
    private AdminService adminService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //参数设置Tab-后端环境
        //获取当前后端环境
        updateBackendEnv(null);
        //单选按钮选择事件
        tgBackendEnv.selectedToggleProperty().addListener((property, oldValue, newValue) ->
                toggleBackendEnv(((RadioButton) newValue).getText()));
        //保存按钮点击事件
        btnBackendSave.setOnAction(event -> clickBackendSave());

        //参数设置Tab-窗口信息
        //加载本机网卡列表
        updateCounterNicChoiceBox();
        //绑定网卡下拉列表选择事件
        cbCounterNic.valueProperty().addListener((property, oldValue, newValue) -> {
            updateCounterMacAndIp(newValue);
            updateCounterNumberAndName();
        });
        //刷新按钮点击事件
        btnCounterRefresh.setOnAction(event -> updateCounterNicChoiceBox());
        //提交按钮点击事件
        btnCounterSubmit.setOnAction(event -> clickCounterSubmit());

        //数据浏览Tab


        //反馈问题Tab
        //截图按钮点击事件
        btnFeedbackScreenshot.setOnAction(event -> clickFeedbackScreenshot());
        //提取按钮点击事件
        btnFeedbackExtractLog.setOnAction(event -> clickFeedbackExtractLog());
        //意见建议键入事件
        taFeedbackSuggestion.textProperty().addListener((property, oldValue, newValue) ->
                typeInFeedbackSuggestion(newValue));
        //提交按钮点击事件
        btnFeedbackSubmit.setOnAction(event -> clickFeedbackSubmit());
    }

    private void toggleBackendEnv(String env) {
        BackendEnvDTO model = adminService.getBackend(env.toLowerCase());
        updateBackendEnv(model);
    }

    private void clickBackendSave() {
        String env = ((RadioButton) tgBackendEnv.getSelectedToggle()).getText().toLowerCase();
        String api = tfBackendApi.getText().trim();
        String ws = tfBackendWs.getText().trim();
        if (!api.matches(AdminConstant.CUSTOM_BACKEND_API_REGEXP)) {
            lblBackendHint.setText(AdminConstant.BACKEND_API_INVALIDATE);
            return;
        }
        if (!ws.matches(AdminConstant.CUSTOM_BACKEND_WS_REGEXP)) {
            lblBackendHint.setText(AdminConstant.BACKEND_WS_INVALIDATE);
            return;
        }
        BackendEnvDTO model = new BackendEnvDTO();
        model.setEnv(env);
        model.setApi(api);
        model.setWs(ws);
        adminService.saveBackend(model);
        lblBackendHint.setText(AdminConstant.BACKEND_SAVE_SUCCESSFUL);
    }

    private void updateBackendEnv(BackendEnvDTO model) {
        if (model == null) {
            model = adminService.getBackend(null);
        }
        switch (model.getEnv()) {
            case AdminConstant.BACKEND_ENV_RD:
                rbBackendRd.setSelected(true);
                tfBackendApi.setEditable(false);
                tfBackendWs.setEditable(false);
                break;
            case AdminConstant.BACKEND_ENV_QA:
                rbBackendQa.setSelected(true);
                tfBackendApi.setEditable(false);
                tfBackendWs.setEditable(false);
                break;
            case AdminConstant.BACKEND_ENV_PROD:
                rbBackendProd.setSelected(true);
                tfBackendApi.setEditable(false);
                tfBackendWs.setEditable(false);
                break;
            default:
                rbBackendCustom.setSelected(true);
                tfBackendApi.setEditable(true);
                tfBackendWs.setEditable(true);
                break;
        }
        tfBackendApi.setText(model.getApi());
        tfBackendWs.setText(model.getWs());
    }

    private void updateCounterNicChoiceBox() {
        tfCounterNumber.setText(CommonConstant.EMPTY_STRING);
        tfCounterName.setText(CommonConstant.EMPTY_STRING);
        tfCounterMac.setText(CommonConstant.EMPTY_STRING);
        tfCounterIp.setText(CommonConstant.EMPTY_STRING);
        lblCounterHint.setText(CommonConstant.EMPTY_STRING);

        List<String> nicIndexAndNames = NativeUtil.getNICBasicInfo().stream()
                .filter(nic -> nic.isUp() && !nic.isLoopback())
                .map(nic -> nic.getIndex() + "|" + nic.getName())
                .collect(Collectors.toList());
        cbCounterNic.setItems(FXCollections.observableArrayList(nicIndexAndNames));
    }

    private void updateCounterMacAndIp(String selectNic) {
        if (!StringUtils.isEmpty(selectNic)) {
            NICAddressDTO nicAddress = NativeUtil.getNICAddress(Integer.parseInt(selectNic.substring(0, 1)));
            tfCounterMac.setText(nicAddress.getMac());
            tfCounterIp.setText(nicAddress.getIpv4());
        }
    }

    private void updateCounterNumberAndName() {
        String mac = tfCounterMac.getText().trim();
        String ip = tfCounterIp.getText().trim();
        if (mac.matches(RegExpConstant.MAC_ADDRESS) && ip.matches(RegExpConstant.IP_ADDRESS)) {
            adminService.queryCounter(mac, ip,
                    result -> Platform.runLater(() -> {
                        tfCounterNumber.setText(result.getNumber());
                        tfCounterName.setText(result.getName());
                        lblCounterHint.setText(CommonConstant.EMPTY_STRING);
                    }), error -> Platform.runLater(() -> {
                        tfCounterNumber.setText(CommonConstant.EMPTY_STRING);
                        tfCounterName.setText(CommonConstant.EMPTY_STRING);
                        lblCounterHint.setText(error);
                    }));
        }
    }

    private void clickCounterSubmit() {
        String number = tfCounterNumber.getText().trim();
        String name = tfCounterName.getText().trim();
        String mac = tfCounterMac.getText().trim();
        String ip = tfCounterIp.getText().trim();
        if (number.length() < 1 || number.length() > 16) {
            lblCounterHint.setText(AdminConstant.COUNTER_NUMBER_INVALIDATE);
            return;
        }
        if (name.length() < 1 || name.length() > 16) {
            lblCounterHint.setText(AdminConstant.COUNTER_NAME_INVALIDATE);
            return;
        }
        adminService.submitCounter(number, name, mac, ip,
                success -> Platform.runLater(() -> lblCounterHint.setText(AdminConstant.COUNTER_SUBMIT_SUCCESS)),
                error -> Platform.runLater(() -> lblCounterHint.setText(error)));
    }

    private void clickFeedbackScreenshot() {
        tpParent.snapshot(result -> {
            ivFeedbackScreenshot.setImage(result.getImage());
            return null;
        }, null, null);
    }

    private void clickFeedbackExtractLog() {
        //TODO
    }

    private void typeInFeedbackSuggestion(String text) {
        lblFeedbackSuggestionLength.setText(String.valueOf(text.length()));
        if (text.length() >= 500) {
            taFeedbackSuggestion.setText(text.substring(0, 500));
        }
    }

    private void clickFeedbackSubmit() {
        //TODO
    }
}