/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.admin.presenter;

import cn.edu.xmu.bip.service.IAdminService;
import cn.edu.xmu.bip.service.factory.ServiceFactory;
import cn.edu.xmu.bip.ui.admin.model.ConfigBackendModel;
import cn.edu.xmu.bip.ui.admin.model.ConfigCounterModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

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
    @FXML
    private VBox vbRoot;
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
    private Label lblMessage1;
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
    private Label lblMessage2;

    @Inject
    private ServiceFactory serviceFactory;
    @Inject
    private ConfigBackendModel configBackendModel;
    @Inject
    private ConfigCounterModel configCounterModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindViewModel();
        bindEvent();
    }

    private void bindViewModel() {
        //后端环境
        configBackendModel.apiProperty().bindBidirectional(tfApi.textProperty());
        configBackendModel.wsProperty().bindBidirectional(tfWs.textProperty());
        configBackendModel.messageProperty().bindBidirectional(lblMessage1.textProperty());
        //窗口信息
        configCounterModel.numberProperty().bindBidirectional(tfNumber.textProperty());
        configCounterModel.nameProperty().bindBidirectional(tfName.textProperty());
        configCounterModel.nicCurrentProperty().bindBidirectional(cbNic.valueProperty());
        configCounterModel.nicListProperty().bindBidirectional(cbNic.itemsProperty());
        configCounterModel.macProperty().bindBidirectional(tfMac.textProperty());
        configCounterModel.ipProperty().bindBidirectional(tfIp.textProperty());
        configCounterModel.messageProperty().bindBidirectional(lblMessage2.textProperty());
    }

    private void bindEvent() {
        //View展示时初始化
        vbRoot.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue == null && newValue != null) {
                newValue.getWindow().setOnShown(event -> bindWindowEvent());
            }
        });
        //选择预置后端环境
        tgPreset.selectedToggleProperty().addListener(
                (observable, oldValue, newValue) -> getPresetBackend(((RadioButton) newValue).getId()));
        //保存后端环境
        btnSave.setOnAction(event -> saveBackend());
        //选择绑定网卡
        cbNic.valueProperty().addListener((observable, oldValue, newValue) -> selectNic());
        //刷新网卡列表
        btnRefresh.setOnAction(event -> refreshNicList());
        //更新网卡信息
        tfMac.textProperty().addListener((observable, oldValue, newValue) -> queryCounter());
        tfIp.textProperty().addListener((observable, oldValue, newValue) -> queryCounter());
        //提交窗口信息
        btnSubmit.setOnAction(event -> submitCounter());
    }

    private void bindWindowEvent() {
        //获取当前后端环境
        getCurrentBackend();
        //加载本机网卡列表
        refreshNicList();
    }

    private void getCurrentBackend() {
        IAdminService adminService = (IAdminService) serviceFactory.getInstance(ServiceFactory.ADMIN);
        adminService.getCurrentBackend();
    }

    private void getPresetBackend(String preset) {
        IAdminService adminService = (IAdminService) serviceFactory.getInstance(ServiceFactory.ADMIN);
        adminService.getPresetBackend(preset);
    }

    private void saveBackend() {
        IAdminService adminService = (IAdminService) serviceFactory.getInstance(ServiceFactory.ADMIN);
        adminService.saveBackend();
    }

    private void selectNic() {
        IAdminService adminService = (IAdminService) serviceFactory.getInstance(ServiceFactory.ADMIN);
        adminService.selectNic();
    }

    private void refreshNicList() {
        IAdminService adminService = (IAdminService) serviceFactory.getInstance(ServiceFactory.ADMIN);
        adminService.refreshNicList();
    }

    private void queryCounter() {
        IAdminService adminService = (IAdminService) serviceFactory.getInstance(ServiceFactory.ADMIN);
        adminService.queryCounter();
    }

    private void submitCounter() {
        IAdminService adminService = (IAdminService) serviceFactory.getInstance(ServiceFactory.ADMIN);
        adminService.submitCounter();
    }
}