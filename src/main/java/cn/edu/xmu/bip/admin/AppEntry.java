/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.admin;

import cn.edu.xmu.bip.admin.view.AdminSceneView;
import com.airhacks.afterburner.injection.Injector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 管理工具入口
 *
 * @author luoxin
 * @version 2017-5-28
 */
public class AppEntry extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        AdminSceneView view = new AdminSceneView();
        Scene scene = new Scene(view.getView());
        stage.setResizable(false);
        stage.setTitle("管理工具");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        Injector.forgetAll();
    }
}
