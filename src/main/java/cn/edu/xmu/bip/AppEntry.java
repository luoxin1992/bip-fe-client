/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip;

import cn.com.lx1992.lib.client.inject.Injector;
import cn.com.lx1992.lib.client.util.CrashUtil;
import cn.edu.xmu.bip.exception.ClientException;
import cn.edu.xmu.bip.exception.ClientExceptionEnum;
import cn.edu.xmu.bip.ui.StageFactory;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * 启动界面入口
 *
 * @author luoxin
 * @version 2017-5-28
 */
public class AppEntry extends Application {
    private static final String APP_PROPS_FILE = "/META-INF/app.properties";

    private static final int ARGS_COUNT = 1;
    private static final String ARGS_ADMIN = "--admin";

    @Override
    public void init() throws Exception {
        CrashUtil.setUncaughtExceptionHandler();
    }

    @Override
    public void start(Stage stage) throws Exception {
        loadAppProps();
        processCliArgs();
    }

    @Override
    public void stop() throws Exception {
        Injector.forgetAll();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void loadAppProps() {
        try (InputStream appPropsFile = AppEntry.class.getResourceAsStream(APP_PROPS_FILE)) {
            Properties properties = new Properties();
            properties.load(appPropsFile);
            properties.forEach((key, value) -> {
                if (StringUtils.isEmpty(System.getProperty(String.valueOf(key)))) {
                    System.out.println("load app property: key=" + key + ", value=" + value);
                    System.setProperty(String.valueOf(key), String.valueOf(value));
                }
            });
        } catch (Exception e) {
            System.err.println("load app properties failed");
            throw new ClientException(ClientExceptionEnum.STARTUP_LOAD_PROP_ERROR);
        }
    }

    private void processCliArgs() {
        List<String> args = getParameters().getRaw();
        if (CollectionUtils.isEmpty(args)) {
            //不带命令行参数时正常启动
            StageFactory.showStage(StageFactory.SPLASH);
        } else {
            if (args.size() != ARGS_COUNT || !ARGS_ADMIN.equals(args.get(0))) {
                System.err.println("invalid command line argument");
                throw new ClientException(ClientExceptionEnum.STARTUP_CLI_ARGS_INVALID);
            } else {
                StageFactory.showStage(StageFactory.ADMIN);
            }
        }
    }
}
