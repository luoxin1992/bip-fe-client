/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.admin.presenter;

import cn.com.lx1992.lib.client.dto.NICAddressDTO;
import cn.com.lx1992.lib.client.util.NativeUtil;
import cn.edu.xmu.bip.admin.constant.AdminConstant;
import cn.edu.xmu.bip.admin.service.ConfigService;
import cn.edu.xmu.bip.common.constant.CommonConstant;
import cn.edu.xmu.bip.ui.admin.model.BackendModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * 管理工具-参数设置
 *
 * @author luoxin
 * @version 2017-6-26
 */
public class ConfigPresenter implements Initializable {
    /**
     * 后端环境
     */
    @FXML
    private ToggleGroup tgEnv;
    @FXML
    private RadioButton rbRd;
    @FXML
    private RadioButton rbQa;
    @FXML
    private RadioButton rbProd;
    @FXML
    private RadioButton rbCustom;
    @FXML
    private TextField tfApi;
    @FXML
    private TextField tfWs;
    @FXML
    private Button btnSave;
    @FXML
    private Label lblHint1;
    /**
     * 窗口信息
     */
    @FXML
    private TextField tfNumber;
    @FXML
    private TextField tfName;
    @FXML
    private ChoiceBox<String> cbNic;
    @FXML
    private Button btnRefresh;
    @FXML
    private TextField tfMac;
    @FXML
    private TextField tfIp;
    @FXML
    private Button btnSubmit;
    @FXML
    private Label lblHint2;

    @Inject
    private ConfigService configService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //后端环境
        //更新当前后端环境
        updateBackendControl(null);
        //单选按钮选择事件
        tgEnv.selectedToggleProperty().addListener((property, oldValue, newValue) ->
                updateBackendControl(((RadioButton) newValue).getId()));
        //保存按钮点击事件
        btnSave.setOnAction(event -> clickSaveButton());

        //窗口信息
        //加载本机网卡列表
        updateNicChoiceBox();
        //绑定网卡下拉列表选择事件
        cbNic.valueProperty().addListener((property, oldValue, newValue) -> {
            if (!StringUtils.isEmpty(newValue)) {
                updateMacAndIpTextField(newValue);
                updateNumberAndNameTextField();
            }
        });
        //刷新按钮点击事件
        btnRefresh.setOnAction(event -> updateNicChoiceBox());
        //提交按钮点击事件
        btnSubmit.setOnAction(event -> clickSubmitButton());
    }

    private void updateBackendControl(String env) {
        BackendModel model;
        if (StringUtils.isEmpty(env)) {
            model = configService.getBackend(null);
        } else {
            model = configService.getBackend(env);
        }
        switch (model.getEnv()) {
            case AdminConstant.BACKEND_ENV_RD:
                rbRd.setSelected(true);
                tfApi.setEditable(false);
                tfWs.setEditable(false);
                break;
            case AdminConstant.BACKEND_ENV_QA:
                rbQa.setSelected(true);
                tfApi.setEditable(false);
                tfWs.setEditable(false);
                break;
            case AdminConstant.BACKEND_ENV_PROD:
                rbProd.setSelected(true);
                tfApi.setEditable(false);
                tfWs.setEditable(false);
                break;
            default:
                rbCustom.setSelected(true);
                tfApi.setEditable(true);
                tfWs.setEditable(true);
                break;
        }
        tfApi.setText(model.getApi());
        tfWs.setText(model.getWs());
    }

    private void clickSaveButton() {
        String env = ((RadioButton) tgEnv.getSelectedToggle()).getId();
        String api = tfApi.getText().trim();
        String ws = tfWs.getText().trim();

        String result = configService.saveBackend(env, api, ws);
        lblHint1.setText(result);
    }

    private void updateNicChoiceBox() {
        tfNumber.setText(CommonConstant.EMPTY_STRING);
        tfName.setText(CommonConstant.EMPTY_STRING);
        tfMac.setText(CommonConstant.EMPTY_STRING);
        tfIp.setText(CommonConstant.EMPTY_STRING);
        lblHint2.setText(CommonConstant.EMPTY_STRING);

        List<String> nicIndexAndNames = NativeUtil.getNICBasicInfo().stream()
                .filter(nic -> nic.isUp() && !nic.isLoopback())
                //下拉列表展示格式：index|name
                .map(nic -> nic.getIndex() + "|" + nic.getName())
                .collect(Collectors.toList());
        cbNic.setItems(FXCollections.observableArrayList(nicIndexAndNames));
    }

    private void updateMacAndIpTextField(String selectNic) {
        NICAddressDTO nicAddress = NativeUtil.getNICAddress(Integer.parseInt(selectNic.substring(0, 1)));
        tfMac.setText(nicAddress.getMac());
        tfIp.setText(nicAddress.getIpv4());
    }

    private void updateNumberAndNameTextField() {
        String mac = tfMac.getText().trim();
        String ip = tfIp.getText().trim();

        configService.queryCounter(mac, ip,
                result -> Platform.runLater(() -> {
                    tfNumber.setText(result.getNumber());
                    tfName.setText(result.getName());
                    lblHint2.setText(CommonConstant.EMPTY_STRING);
                }), error -> Platform.runLater(() -> {
                    tfNumber.setText(CommonConstant.EMPTY_STRING);
                    tfName.setText(CommonConstant.EMPTY_STRING);
                    lblHint2.setText(error);
                }));
    }

    private void clickSubmitButton() {
        String number = tfNumber.getText().trim();
        String name = tfName.getText().trim();
        String mac = tfMac.getText().trim();
        String ip = tfIp.getText().trim();

        configService.submitCounter(number, name, mac, ip,
                success -> Platform.runLater(() -> lblHint2.setText(AdminConstant.COUNTER_SUBMIT_SUCCESS)),
                error -> Platform.runLater(() -> lblHint2.setText(error)));
    }
}