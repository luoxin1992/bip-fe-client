/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.admin.presenter;

import cn.edu.xmu.bip.service.AdminService;
import cn.edu.xmu.bip.ui.admin.model.ConfigBackendModel;
import cn.edu.xmu.bip.ui.admin.model.ConfigCounterModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

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
    private ToggleGroup tgPreset;
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
    private AdminService adminService;
    @Inject
    private ConfigBackendModel configBackendModel;
    @Inject
    private ConfigCounterModel configCounterModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindViewModel();
        bindEvent();
        //获取当前后端环境
        getCurrentBackend();
        //加载本机网卡列表
        updateNicList();
        //获取已绑定的网卡
        getBindNic();
    }

    private void bindViewModel() {
        //后端环境
        tfApi.textProperty().bindBidirectional(configBackendModel.apiProperty());
        tfWs.textProperty().bindBidirectional(configBackendModel.wsProperty());
        lblHint1.textProperty().bindBidirectional(configBackendModel.hintProperty());
        //窗口信息
        tfNumber.textProperty().bindBidirectional(configCounterModel.numberProperty());
        tfName.textProperty().bindBidirectional(configCounterModel.nameProperty());
        cbNic.setItems(configCounterModel.getNicList());
        cbNic.valueProperty().bindBidirectional(configCounterModel.nicProperty());
        tfMac.textProperty().bindBidirectional(configCounterModel.macProperty());
        tfIp.textProperty().bindBidirectional(configCounterModel.ipProperty());
        lblHint2.textProperty().bindBidirectional(configCounterModel.hintProperty());
    }

    private void bindEvent() {
        //选择预置后端环境
        tgPreset.selectedToggleProperty().addListener(observable -> selectPresetBackend());
        //保存后端环境
        btnSave.setOnAction(event -> saveBackend());
        //选择绑定网卡
        cbNic.valueProperty().addListener((property, oldValue, newValue) -> selectNic());
        //刷新网卡列表
        btnRefresh.setOnAction(event -> updateNicList());
        //提交窗口信息
        btnSubmit.setOnAction(event -> submitCounter());
    }

    private void getCurrentBackend() {
        adminService.getCurrentBackend();
    }

    private void selectPresetBackend() {
        Toggle toggle = tgPreset.getSelectedToggle();
        if (toggle != null) {
            adminService.getPresetBackend(((RadioButton) toggle).getId());
        }
    }

    private void saveBackend() {
        adminService.saveBackend();
    }

    private void getBindNic() {
        adminService.getBindNic();
    }

    private void selectNic() {
        adminService.selectNic();
    }

    private void updateNicList() {
        adminService.updateNicList();
    }

    private void submitCounter() {
        adminService.submitCounter();
    }
}