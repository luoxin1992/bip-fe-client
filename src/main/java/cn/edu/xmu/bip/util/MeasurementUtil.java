/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.util;

/**
 * UI控件尺寸计算工具类
 *
 * @author luoxin
 * @version 2017-5-30
 */
public class MeasurementUtil {
    private static final int SCREEN_WIDTH;
    private static final int SCREEN_HEIGHT;

    static {
        //SCREEN_WIDTH = (int) Screen.getPrimary().getBounds().getWidth();
        //SCREEN_HEIGHT = (int) Screen.getPrimary().getBounds().getHeight();
        SCREEN_WIDTH = 1024;
        SCREEN_HEIGHT = 600;
    }

    private MeasurementUtil() {
    }

    public static double getNodeWidthByFactor(double factor) {
        return SCREEN_WIDTH * factor;
    }

    public static double getNodeHeightByFactor(double factor) {
        return SCREEN_HEIGHT * factor;
    }

    public static double getNodeWidthByFactor(double parentWidth, double factor) {
        return parentWidth * factor;
    }

    public static double getNodeHeightByFactor(double parentHeight, double factor) {
        return parentHeight * factor;
    }
}
