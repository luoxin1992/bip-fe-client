/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.admin.presenter;

import cn.com.lx1992.lib.client.util.DateTimeUtil;
import cn.edu.xmu.bip.service.IAdminService;
import cn.edu.xmu.bip.service.factory.ServiceFactory;
import cn.edu.xmu.bip.ui.admin.model.DataBrowsingModel;
import javafx.beans.binding.Bindings;
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
import java.util.ResourceBundle;

/**
 * 管理工具-数据浏览
 *
 * @author luoxin
 * @version 2017-6-26
 */
public class DataBrowsingPresenter implements Initializable {
    private static final String TABLE_FINGERPRINT = "fingerprint";
    private static final String TABLE_MESSAGE = "message";
    private static final String TABLE_RESOURCE = "resource";

    @FXML
    private ToggleGroup tgTable;
    @FXML
    private Label lblMessage;
    @FXML
    private DatePicker dpStart;
    @FXML
    private DatePicker dpEnd;
    @FXML
    private Button btnQuery;
    @FXML
    private TableView<DataBrowsingModel.FingerprintModel> tvFingerprint;
    @FXML
    private TableColumn<DataBrowsingModel.FingerprintModel, Integer> tcFingerprintId;
    @FXML
    private TableColumn<DataBrowsingModel.FingerprintModel, String> tcFingerprintEvent;
    @FXML
    private TableColumn<DataBrowsingModel.FingerprintModel, String> tcFingerprintExtra;
    @FXML
    private TableColumn<DataBrowsingModel.FingerprintModel, Long> tcFingerprintTimestamp;
    @FXML
    private TableView<DataBrowsingModel.MessageModel> tvMessage;
    @FXML
    private TableColumn<DataBrowsingModel.MessageModel, Integer> tcMessageId;
    @FXML
    private TableColumn<DataBrowsingModel.MessageModel, String> tcMessageType;
    @FXML
    private TableColumn<DataBrowsingModel.MessageModel, Long> tcMessageUid;
    @FXML
    private TableColumn<DataBrowsingModel.MessageModel, String> tcMessageBody;
    @FXML
    private TableColumn<DataBrowsingModel.MessageModel, Long> tcMessageTimestamp;
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
    private TableColumn<DataBrowsingModel.ResourceModel, Long> tcResourceLength;
    @FXML
    private TableColumn<DataBrowsingModel.ResourceModel, String> tcResourceModify;
    @FXML
    private TableColumn<DataBrowsingModel.ResourceModel, Long> tcResourceTimestamp;

    @Inject
    private ServiceFactory serviceFactory;
    @Inject
    private DataBrowsingModel dataBrowsingModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindView();
        bindViewModel();
        bindEvent();
    }

    private void bindView() {
        //查询Button的disable属性绑定ToggleGroup未选择
        btnQuery.disableProperty().bind(Bindings.createBooleanBinding(
                () -> tgTable.getSelectedToggle() == null, tgTable.selectedToggleProperty()));
        //结果TableView的visible和managed属性双向绑定，使控件隐藏后不占用空间
        tvFingerprint.visibleProperty().bindBidirectional(tvFingerprint.managedProperty());
        tvMessage.visibleProperty().bindBidirectional(tvMessage.managedProperty());
        tvResource.visibleProperty().bindBidirectional(tvResource.managedProperty());
    }

    private void bindViewModel() {
        dataBrowsingModel.startProperty().bindBidirectional(dpStart.valueProperty());
        dataBrowsingModel.endProperty().bindBidirectional(dpEnd.valueProperty());
        dataBrowsingModel.messageProperty().bindBidirectional(lblMessage.textProperty());
        bindTableViewAndColumns();
    }

    private void bindTableViewAndColumns() {
        tvFingerprint.itemsProperty().bindBidirectional(dataBrowsingModel.fingerprintsProperty());
        tcFingerprintId.setCellValueFactory(cellValue -> cellValue.getValue().idProperty().asObject());
        tcFingerprintEvent.setCellValueFactory(cellValue -> cellValue.getValue().eventProperty());
        tcFingerprintExtra.setCellValueFactory(cellValue -> cellValue.getValue().extraProperty());
        tcFingerprintTimestamp.setCellValueFactory(cellValue -> cellValue.getValue().timestampProperty().asObject());

        tvMessage.itemsProperty().bindBidirectional(dataBrowsingModel.messagesProperty());
        tcMessageId.setCellValueFactory(cellValue -> cellValue.getValue().idProperty().asObject());
        tcMessageUid.setCellValueFactory(cellValue -> cellValue.getValue().uidProperty().asObject());
        tcMessageType.setCellValueFactory(cellValue -> cellValue.getValue().typeProperty());
        tcMessageBody.setCellValueFactory(cellValue -> cellValue.getValue().bodyProperty());
        tcMessageTimestamp.setCellValueFactory(cellValue -> cellValue.getValue().timestampProperty().asObject());

        tvResource.itemsProperty().bindBidirectional(dataBrowsingModel.resourcesProperty());
        tcResourceId.setCellValueFactory(cellValue -> cellValue.getValue().idProperty().asObject());
        tcResourceType.setCellValueFactory(cellValue -> cellValue.getValue().typeProperty());
        tcResourceUrl.setCellValueFactory(cellValue -> cellValue.getValue().urlProperty());
        tcResourcePath.setCellValueFactory(cellValue -> cellValue.getValue().pathProperty());
        tcResourceLength.setCellValueFactory(cellValue -> cellValue.getValue().lengthProperty().asObject());
        tcResourceModify.setCellValueFactory(cellValue -> cellValue.getValue().modifyProperty());
        tcResourceTimestamp.setCellValueFactory(cellValue -> cellValue.getValue().timestampProperty().asObject());
    }

    private void bindEvent() {
        //选择库表
        tgTable.selectedToggleProperty().addListener((observable, oldValue, newValue) -> selectTable(((RadioButton) newValue).getId()));
        //查询数据
        btnQuery.setOnAction(event -> queryData(((RadioButton) tgTable.getSelectedToggle()).getId()));
    }

    private void selectTable(String table) {
        //切换数据库表时清空已有查询结果
        dataBrowsingModel.setMessage(null);
        dataBrowsingModel.getFingerprints().clear();
        dataBrowsingModel.getMessages().clear();
        dataBrowsingModel.getResources().clear();

        //fingerprint和message默认查询周期(t-1)~t、resource默认查询周期(t-7)~t
        switch (table) {
            case TABLE_FINGERPRINT:
                dataBrowsingModel.setStart(DateTimeUtil.getYesterday());
                dataBrowsingModel.setEnd(DateTimeUtil.getToday());

                tvFingerprint.setVisible(true);
                tvMessage.setVisible(false);
                tvResource.setVisible(false);
                break;
            case TABLE_MESSAGE:
                dataBrowsingModel.setStart(DateTimeUtil.getYesterday());
                dataBrowsingModel.setEnd(DateTimeUtil.getToday());

                tvFingerprint.setVisible(false);
                tvMessage.setVisible(true);
                tvResource.setVisible(false);
                break;
            case TABLE_RESOURCE:
                dataBrowsingModel.setStart(DateTimeUtil.getLastWeekToday());
                dataBrowsingModel.setEnd(DateTimeUtil.getToday());

                tvFingerprint.setVisible(false);
                tvMessage.setVisible(false);
                tvResource.setVisible(true);
                break;
        }
    }

    private void queryData(String table) {
        IAdminService adminService = (IAdminService) serviceFactory.getInstance(ServiceFactory.ADMIN);

        switch (table) {
            case TABLE_FINGERPRINT:
                adminService.queryFingerprint();
                break;
            case TABLE_MESSAGE:
                adminService.queryMessage();
                break;
            case TABLE_RESOURCE:
                adminService.queryResource();
                break;
        }
    }
}