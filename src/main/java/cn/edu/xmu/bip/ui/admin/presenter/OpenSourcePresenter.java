/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.admin.presenter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * 管理工具-开源许可
 *
 * @author luoxin
 * @version 2017-6-26
 */
public class OpenSourcePresenter implements Initializable {
    private static final String HTML_FILE_PATH = "/file/open-source.html";

    private final Logger logger = LoggerFactory.getLogger(OpenSourcePresenter.class);

    @FXML
    private WebView wvCopyright;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //疑似JDK BUG 该属性无法在FXML中配置
        wvCopyright.setContextMenuEnabled(false);
        try {
            URI uri = getClass().getResource(HTML_FILE_PATH).toURI();
            wvCopyright.getEngine().load(uri.toString());
        } catch (URISyntaxException e) {
            logger.error("load open source copyright html file failed", e);
        }
    }
}