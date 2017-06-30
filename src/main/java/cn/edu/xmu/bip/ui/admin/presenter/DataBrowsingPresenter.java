/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.admin.presenter;

import cn.com.lx1992.lib.client.util.DateTimeUtil;
import cn.edu.xmu.bip.admin.constant.AdminConstant;
import cn.edu.xmu.bip.admin.service.DataBrowsingService;
import cn.edu.xmu.bip.ui.admin.model.FingerprintLogModel;
import cn.edu.xmu.bip.ui.admin.model.MessageLogModel;
import cn.edu.xmu.bip.ui.admin.model.ResourceModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    private Label lblTotalPage;
    @FXML
    private Label lblCurrentPage;
    @FXML
    private Button btnPrevPage;
    @FXML
    private Button btnNextPage;
    @FXML
    private DatePicker dpStart;
    @FXML
    private DatePicker dpEnd;
    @FXML
    private Button btnQuery;
    @FXML
    private TableView<FingerprintLogModel> tvFingerprintLog;
    @FXML
    private TableColumn<FingerprintLogModel, Integer> tcFingerprintLogId;
    @FXML
    private TableColumn<FingerprintLogModel, String> tcFingerprintLogType;
    @FXML
    private TableColumn<FingerprintLogModel, String> tcFingerprintLogContent;
    @FXML
    private TableColumn<FingerprintLogModel, String> tcFingerprintLogTimestamp;
    @FXML
    private TableView<MessageLogModel> tvMessageLog;
    @FXML
    private TableColumn<MessageLogModel, Integer> tcMessageLogId;
    @FXML
    private TableColumn<MessageLogModel, String> tcMessageLogType;
    @FXML
    private TableColumn<MessageLogModel, String> tcMessageLogBody;
    @FXML
    private TableColumn<MessageLogModel, String> tcMessageLogTimestamp;
    @FXML
    private TableView<ResourceModel> tvResource;
    @FXML
    private TableColumn<ResourceModel, Integer> tcResourceId;
    @FXML
    private TableColumn<ResourceModel, String> tcResourceType;
    @FXML
    private TableColumn<ResourceModel, String> tcResourceUrl;
    @FXML
    private TableColumn<ResourceModel, String> tcResourcePath;
    @FXML
    private TableColumn<ResourceModel, String> tcResourceFilename;
    @FXML
    private TableColumn<ResourceModel, String> tcResourceMd5;
    @FXML
    private TableColumn<ResourceModel, String> tcResourceTimestamp;

    @Inject
    private DataBrowsingService dataBrowsingService;

    private ObservableList<FingerprintLogModel> fingerprintLogModels = FXCollections.observableArrayList();
    private ObservableList<MessageLogModel> messageLogModels = FXCollections.observableArrayList();
    private ObservableList<ResourceModel> resourceModels = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //绑定表格和表格列相关属性
        bindTableVisibleWithManaged();
        bindTableViewAndColumns();
        //数据库表单选按钮选择事件
        tgTable.selectedToggleProperty().addListener((property, oldValue, newValue) ->
                selectTableToggleGroup(((RadioButton) newValue).getId()));
        //翻页按钮点击事件
        btnPrevPage.setOnAction(event -> clickPrevPageButton());
        btnNextPage.setOnAction(event -> clickNextPageButton());
        //查询按钮点击事件
        btnQuery.setOnAction(event -> clickQueryButton());
    }

    private void bindTableVisibleWithManaged() {
        //visible和managed属性双向绑定，使控件隐藏后不占用空间
        tvFingerprintLog.visibleProperty().bindBidirectional(tvFingerprintLog.managedProperty());
        tvMessageLog.visibleProperty().bindBidirectional(tvMessageLog.managedProperty());
        tvResource.visibleProperty().bindBidirectional(tvResource.managedProperty());
    }

    private void bindTableViewAndColumns() {
        //指纹(扫描仪)日志
        tvFingerprintLog.setItems(fingerprintLogModels);
        tcFingerprintLogId.setCellValueFactory(cellValue -> cellValue.getValue().idProperty().asObject());
        tcFingerprintLogType.setCellValueFactory(cellValue -> cellValue.getValue().typeProperty());
        tcFingerprintLogContent.setCellValueFactory(cellValue -> cellValue.getValue().contentProperty());
        tcFingerprintLogTimestamp.setCellValueFactory(cellValue -> cellValue.getValue().timestampProperty());
        //消息日志
        tvMessageLog.setItems(messageLogModels);
        tcMessageLogId.setCellValueFactory(cellValue -> cellValue.getValue().idProperty().asObject());
        tcMessageLogType.setCellValueFactory(cellValue -> cellValue.getValue().typeProperty());
        tcMessageLogBody.setCellValueFactory(cellValue -> cellValue.getValue().bodyProperty());
        tcMessageLogTimestamp.setCellValueFactory(cellValue -> cellValue.getValue().timestampProperty());
        //资源
        tvResource.setItems(resourceModels);
        tcResourceId.setCellValueFactory(cellValue -> cellValue.getValue().idProperty().asObject());
        tcResourceType.setCellValueFactory(cellValue -> cellValue.getValue().typeProperty());
        tcResourceUrl.setCellValueFactory(cellValue -> cellValue.getValue().urlProperty());
        tcResourcePath.setCellValueFactory(cellValue -> cellValue.getValue().pathProperty());
        tcResourceFilename.setCellValueFactory(cellValue -> cellValue.getValue().filenameProperty());
        tcResourceMd5.setCellValueFactory(cellValue -> cellValue.getValue().md5Property());
        tcResourceTimestamp.setCellValueFactory(cellValue -> cellValue.getValue().timestampProperty());
    }

    private void selectTableToggleGroup(String selectId) {
        //初始时未选择表，查询按钮不可用，选择后激活按钮
        if (btnQuery.isDisable()) {
            btnQuery.setDisable(false);
        }
        //资源表默认查询周期(t-7)~t，其他表(t-1)~t
        switch (selectId) {
            case AdminConstant.TABLE_FINGERPRINT_LOG:
                dpStart.setValue(DateTimeUtil.getYesterday());
                dpEnd.setValue(DateTimeUtil.getToday());
                tvFingerprintLog.setVisible(true);
                tvMessageLog.setVisible(false);
                tvResource.setVisible(false);
                break;
            case AdminConstant.TABLE_MESSAGE_LOG:
                dpStart.setValue(DateTimeUtil.getYesterday());
                dpEnd.setValue(DateTimeUtil.getToday());
                tvFingerprintLog.setVisible(false);
                tvMessageLog.setVisible(true);
                tvResource.setVisible(false);
                break;
            case AdminConstant.TABLE_RESOURCE:
                dpStart.setValue(DateTimeUtil.getLastWeekToday());
                dpEnd.setValue(DateTimeUtil.getToday());
                tvFingerprintLog.setVisible(false);
                tvMessageLog.setVisible(false);
                tvResource.setVisible(true);
                break;
        }
    }

    private void clickPrevPageButton() {
        String table = ((RadioButton) tgTable.getSelectedToggle()).getId();
        int page = Integer.parseInt(lblCurrentPage.getText()) - 1;
        LocalDate start = dpStart.getValue();
        LocalDate end = dpEnd.getValue();

        queryData(table, page, start, end);
    }

    private void clickNextPageButton() {
        String table = ((RadioButton) tgTable.getSelectedToggle()).getId();
        int page = Integer.parseInt(lblCurrentPage.getText()) + 1;
        LocalDate start = dpStart.getValue();
        LocalDate end = dpEnd.getValue();

        queryData(table, page, start, end);
    }

    private void clickQueryButton() {
        String table = ((RadioButton) tgTable.getSelectedToggle()).getId();
        LocalDate start = dpStart.getValue();
        LocalDate end = dpEnd.getValue();

        queryData(table, 1, start, end);
    }

    private void queryData(String table, int page, LocalDate start, LocalDate end) {
        switch (table) {
            case AdminConstant.TABLE_FINGERPRINT_LOG:
                //分页
                updatePagingControl(dataBrowsingService.pagingFingerprintLog(start, end), page);
                //数据
                fingerprintLogModels.clear();
                fingerprintLogModels.addAll(dataBrowsingService.queryFingerprintLogs(page, start, end));
                break;
            case AdminConstant.TABLE_MESSAGE_LOG:
                updatePagingControl(dataBrowsingService.pagingMessageLog(start, end), page);
                messageLogModels.clear();
                messageLogModels.addAll(dataBrowsingService.queryMessageLogs(page, start, end));
                break;
            case AdminConstant.TABLE_RESOURCE:
                updatePagingControl(dataBrowsingService.pagingResource(start, end), page);
                resourceModels.clear();
                resourceModels.addAll(dataBrowsingService.queryResources(page, start, end));
                break;
        }
    }

    private void updatePagingControl(int total, int current) {
        if (total > 1) {
            if (current <= 1) {
                btnPrevPage.setDisable(true);
                btnNextPage.setDisable(false);
            } else if (current >= total) {
                btnPrevPage.setDisable(false);
                btnNextPage.setDisable(true);
            } else {
                btnPrevPage.setDisable(false);
                btnNextPage.setDisable(false);
            }
        } else {
            btnPrevPage.setDisable(true);
            btnNextPage.setDisable(true);
        }
        lblTotalPage.setText(String.valueOf(total));
        lblCurrentPage.setText(String.valueOf(current));
    }
}