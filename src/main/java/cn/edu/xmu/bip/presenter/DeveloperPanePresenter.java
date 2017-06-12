/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.presenter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 主界面-开发者
 *
 * @author luoxin
 * @version 2017-6-6
 */
public class DeveloperPanePresenter implements Initializable {
    @FXML
    private StackPane spParent;
    @FXML
    private ImageView ivDeveloper;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ivDeveloper.fitWidthProperty().bind(spParent.widthProperty());
        ivDeveloper.fitHeightProperty().bind(spParent.heightProperty());
    }
}
