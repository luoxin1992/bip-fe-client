/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.ui.splash;

import cn.edu.xmu.bip.ui.splash.view.SplashSceneView;
import com.airhacks.afterburner.injection.Injector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

/**
 * 启动界面入口
 *
 * @author luoxin
 * @version 2017-5-28
 */
public class AppEntry extends Application {
    private static final int MIN_STAGE_WIDTH = 1024;
    private static final int MIN_STAGE_HEIGHT = 600;

    private Stage stage;

    public void start(Stage stage) throws Exception {
        setStage(stage);

        SplashSceneView view = new SplashSceneView();
        Scene scene = new Scene(view.getView(), MIN_STAGE_WIDTH, MIN_STAGE_HEIGHT);

        view.prefWidthProperty().bind(scene.widthProperty());
        view.prefHeightProperty().bind(scene.heightProperty());

        scene.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                this.switchFullScreen();
            }
        });
        stage.setMinWidth(MIN_STAGE_WIDTH);
        stage.setMinHeight(MIN_STAGE_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        Injector.forgetAll();
    }

    private void setStage(Stage stage) {
        this.stage = stage;
    }

    private void switchFullScreen() {
        if (stage.isFullScreen()) {
            stage.setFullScreen(false);
        } else {
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.setFullScreen(true);
        }
    }
}
