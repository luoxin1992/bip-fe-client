/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui;

import cn.edu.xmu.bip.exception.ClientException;
import cn.edu.xmu.bip.exception.ClientExceptionEnum;
import cn.edu.xmu.bip.ui.admin.view.AdminSceneView;
import cn.edu.xmu.bip.ui.main.view.MainSceneView;
import cn.edu.xmu.bip.ui.splash.view.SplashSceneView;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Stage工厂
 *
 * @author luoxin
 * @version 2017-8-15
 */
public class StageFactory {
    public static final String MAIN = "main";
    public static final String ADMIN = "admin";
    public static final String SPLASH = "splash";

    private static final Logger logger = LoggerFactory.getLogger(StageFactory.class);
    private static final HashMap<String, Stage> stages;

    static {
        //主界面
        Stage mainStage = new Stage();
        mainStage.setScene(new Scene(new MainSceneView().getView()));
        mainStage.setFullScreen(true);
        mainStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        //管理工具
        Stage adminStage = new Stage();
        adminStage.setScene(new Scene(new AdminSceneView().getView()));
        //启动界面
        Stage splashStage = new Stage();
        splashStage.setScene(new Scene(new SplashSceneView().getView()));
        splashStage.setFullScreen(true);
        splashStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        //放入内部容器
        stages = new HashMap<>();
        stages.putIfAbsent(MAIN, mainStage);
        stages.putIfAbsent(ADMIN, adminStage);
        stages.putIfAbsent(SPLASH, splashStage);
    }

    /**
     * 展示舞台(Stage)
     *
     * @param name 舞台名称
     */
    public static void showStage(String name) {
        checkStage(name);

        Stage stage = stages.get(name);
        if (Platform.isFxApplicationThread()) {
            stage.show();
        } else {
            Platform.runLater(stage::show);
        }
    }

    /**
     * 隐藏舞台(Stage)
     *
     * @param name 舞台名称
     */
    public static void hideStage(String name) {
        checkStage(name);

        Stage stage = stages.get(name);
        if (Platform.isFxApplicationThread()) {
            stage.hide();
        } else {
            Platform.runLater(stage::hide);
        }
    }

    /**
     * 检查舞台名称
     *
     * @param name 舞台名称
     */
    private static void checkStage(String name) {
        if (!stages.containsKey(name)) {
            logger.error("invalid stage name {}", name);
            throw new ClientException(ClientExceptionEnum.INVALID_STAGE_NAME);
        }
    }
}
