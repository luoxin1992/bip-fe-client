/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.admin.presenter;

import cn.com.lx1992.lib.client.util.DateTimeUtil;
import cn.edu.xmu.bip.constant.AdminConstant;
import cn.edu.xmu.bip.service.AdminService;
import cn.edu.xmu.bip.ui.admin.model.DataBrowsingModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

import javax.inject.Inject;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * 管理工具-数据浏览
 *
 * @author luoxin
 * @version 2017-6-26
 */
public class DataBrowsingPresenter implements Initializable {
    @FXML
    private ToggleGroup tgTable;
    @FXML
    private Label lblHint;
    @FXML
    private DatePicker dpStart;
    @FXML
    private DatePicker dpEnd;
    @FXML
    private Button btnQuery;
    @FXML
    private TableView<DataBrowsingModel.FingerprintLogModel> tvFingerprintLog;
    @FXML
    private TableColumn<DataBrowsingModel.FingerprintLogModel, Integer> tcFingerprintLogId;
    @FXML
    private TableColumn<DataBrowsingModel.FingerprintLogModel, String> tcFingerprintLogType;
    @FXML
    private TableColumn<DataBrowsingModel.FingerprintLogModel, String> tcFingerprintLogContent;
    @FXML
    private TableColumn<DataBrowsingModel.FingerprintLogModel, Long> tcFingerprintLogTimestamp;
    @FXML
    private TableView<DataBrowsingModel.MessageLogModel> tvMessageLog;
    @FXML
    private TableColumn<DataBrowsingModel.MessageLogModel, Integer> tcMessageLogId;
    @FXML
    private TableColumn<DataBrowsingModel.MessageLogModel, String> tcMessageLogType;
    @FXML
    private TableColumn<DataBrowsingModel.MessageLogModel, String> tcMessageLogBody;
    @FXML
    private TableColumn<DataBrowsingModel.MessageLogModel, Long> tcMessageLogTimestamp;
    @FXML
    private TableView<DataBrowsingModel.ResourceModel> tvResource;
    @FXML
    private TableColumn<DataBrowsingModel.ResourceModel, Integer> tcResourceId;
    @FXML
    private TableColumn<DataBrowsingModel.ResourceModel, String> tcResourceType;
    @FXML
    private TableColumn<DataBrowsingModel.ResourceModel, String> tcResourceUrl;
    @FXML
    private TableColumn<DataBrowsingModel.ResourceModel, String> tcResourcePath;
    @FXML
    private TableColumn<DataBrowsingModel.ResourceModel, String> tcResourceMd5;
    @FXML
    private TableColumn<DataBrowsingModel.ResourceModel, Long> tcResourceTimestamp;
    @FXML
    private TableColumn<DataBrowsingModel.ResourceModel, Integer> tcResourceStatus;

    @Inject
    private AdminService adminService;
    @Inject
    private DataBrowsingModel dataBrowsingModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindViewModel();
        bindEvent();
        bindTableVisibleWithManaged();
        bindTableViewAndColumns();
    }

    private void bindViewModel() {
        dpStart.valueProperty().bindBidirectional(dataBrowsingModel.startProperty());
        dpEnd.valueProperty().bindBidirectional(dataBrowsingModel.endProperty());
        lblHint.textProperty().bindBidirectional(dataBrowsingModel.hintProperty());
    }

    private void bindEvent() {
        //选择数据库表
        tgTable.selectedToggleProperty().addListener(observable -> toggleDbTable());
        //点击查询
        btnQuery.setOnAction(event -> queryData());
    }

    private void bindTableVisibleWithManaged() {
        //visible和managed属性双向绑定，使控件隐藏后不占用空间
        tvFingerprintLog.visibleProperty().bindBidirectional(tvFingerprintLog.managedProperty());
        tvMessageLog.visibleProperty().bindBidirectional(tvMessageLog.managedProperty());
        tvResource.visibleProperty().bindBidirectional(tvResource.managedProperty());
    }

    private void bindTableViewAndColumns() {
        //指纹(扫描仪)日志
        tvFingerprintLog.setItems(dataBrowsingModel.getFingerprintLogs());
        tcFingerprintLogId.setCellValueFactory(cellValue -> cellValue.getValue().idProperty().asObject());
        tcFingerprintLogType.setCellValueFactory(cellValue -> cellValue.getValue().typeProperty());
        tcFingerprintLogContent.setCellValueFactory(cellValue -> cellValue.getValue().contentProperty());
        tcFingerprintLogTimestamp.setCellValueFactory(cellValue -> cellValue.getValue().timestampProperty().asObject());
        //消息日志
        tvMessageLog.setItems(dataBrowsingModel.getMessageLogs());
        tcMessageLogId.setCellValueFactory(cellValue -> cellValue.getValue().idProperty().asObject());
        tcMessageLogType.setCellValueFactory(cellValue -> cellValue.getValue().typeProperty());
        tcMessageLogBody.setCellValueFactory(cellValue -> cellValue.getValue().bodyProperty());
        tcMessageLogTimestamp.setCellValueFactory(cellValue -> cellValue.getValue().timestampProperty().asObject());
        //资源
        tvResource.setItems(dataBrowsingModel.getResources());
        tcResourceId.setCellValueFactory(cellValue -> cellValue.getValue().idProperty().asObject());
        tcResourceType.setCellValueFactory(cellValue -> cellValue.getValue().typeProperty());
        tcResourceUrl.setCellValueFactory(cellValue -> cellValue.getValue().urlProperty());
        tcResourcePath.setCellValueFactory(cellValue -> cellValue.getValue().pathProperty());
        tcResourceMd5.setCellValueFactory(cellValue -> cellValue.getValue().md5Property());
        tcResourceTimestamp.setCellValueFactory(cellValue -> cellValue.getValue().timestampProperty().asObject());
        tcResourceStatus.setCellValueFactory(cellValue -> cellValue.getValue().statusProperty().asObject());
    }

    private void toggleDbTable() {
        //切换数据库表时清空已有查询结果
        dataBrowsingModel.setHint(null);
        dataBrowsingModel.getFingerprintLogs().clear();
        dataBrowsingModel.getMessageLogs().clear();
        dataBrowsingModel.getResources().clear();
        //资源表默认查询周期(t-7)~t，其他表(t-1)~t
        Toggle toggle = tgTable.getSelectedToggle();
        switch (((RadioButton) toggle).getId()) {
            case AdminConstant.TABLE_FINGERPRINT_LOG:
                dataBrowsingModel.setStart(DateTimeUtil.getYesterday());
                dataBrowsingModel.setEnd(DateTimeUtil.getToday());
                tvFingerprintLog.setVisible(true);
                tvMessageLog.setVisible(false);
                tvResource.setVisible(false);
                break;
            case AdminConstant.TABLE_MESSAGE_LOG:
                dataBrowsingModel.setStart(DateTimeUtil.getYesterday());
                dataBrowsingModel.setEnd(DateTimeUtil.getToday());
                tvFingerprintLog.setVisible(false);
                tvMessageLog.setVisible(true);
                tvResource.setVisible(false);
                break;
            case AdminConstant.TABLE_RESOURCE:
                dataBrowsingModel.setStart(DateTimeUtil.getLastWeekToday());
                dataBrowsingModel.setEnd(DateTimeUtil.getToday());
                tvFingerprintLog.setVisible(false);
                tvMessageLog.setVisible(false);
                tvResource.setVisible(true);
                break;
        }
    }

    private void queryData() {
        RadioButton table = (RadioButton) tgTable.getSelectedToggle();
        LocalDate start = dpStart.getValue();
        LocalDate end = dpEnd.getValue();

        if (validateParameter(table, start, end)) {
            switch (table.getId()) {
                case AdminConstant.TABLE_FINGERPRINT_LOG:
                    adminService.queryFingerprintLog(start, end);
                    break;
                case AdminConstant.TABLE_MESSAGE_LOG:
                    adminService.queryMessageLog(start, end);
                    break;
                case AdminConstant.TABLE_RESOURCE:
                    adminService.queryResource(start, end);
                    break;
            }
        }
    }

    private boolean validateParameter(Toggle table, LocalDate start, LocalDate end) {
        if (table == null) {
            dataBrowsingModel.setHint(AdminConstant.DATA_BROWSING_TABLE_NOT_SELECT);
            return false;
        }
        if (start == null || end == null) {
            dataBrowsingModel.setHint(AdminConstant.DATA_BROWSING_DATE_NOT_SET);
            return false;
        }
        if (start.isAfter(end)) {
            dataBrowsingModel.setHint(AdminConstant.DATA_BROWSING_DATE_INVALID);
            return false;
        }
        return true;
    }
}