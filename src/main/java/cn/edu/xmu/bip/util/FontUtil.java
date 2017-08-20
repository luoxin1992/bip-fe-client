/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.util;

import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.Map;

/**
 * UI字体加载工具类
 *
 * @author luoxin
 * @version 2017-6-1
 */
public class FontUtil {
    //常规字体为冬青黑体W3，加粗字体为冬青黑体W6
    private static final String REGULAR_FONT_FAMILY = "Hiragino Sans GB W3";
    private static final String BOLD_FONT_FAMILY = "Hiragino Sans GB W6";
    private static final String REGULAR_FONT_FILENAME = "/font/Hiragino-Sans-GB-W3.ttf";
    private static final String BOLD_FONT_FILENAME = "/font/Hiragino-Sans-GB-W6.ttf";
    //最小字体大小为16点，其他字体大小亦为8的整数倍
    private static final int MIN_FONT_SIZE = 16;
    private static final int BASE_FONT_SIZE = 8;

    private static Map<Integer, Font> regularFonts;
    private static Map<Integer, Font> boldFonts;
    private static boolean installLocally;

    static {
        regularFonts = new HashMap<>();
        boldFonts = new HashMap<>();
        //尝试从本地加载所需字体，检查本地已安装字体中是否包括所需的2种字体
        installLocally = Font.getFamilies().stream()
                .filter(family -> family.equals(REGULAR_FONT_FAMILY) || family.equals(BOLD_FONT_FAMILY))
                .count() == 2;
    }

    private FontUtil() {
    }

    /**
     * 加载可容纳在父布局高度内的最大字体
     *
     * @param parentHeight 父布局高度
     * @param boldFont     使用加粗字体
     * @return 字体
     */
    public static synchronized Font loadFont(double parentHeight, boolean boldFont) {
        int size = getMaxSize(parentHeight);
        if (boldFont) {
            if (!boldFonts.containsKey(size)) {
                Font font;
                if (installLocally) {
                    font = Font.font(BOLD_FONT_FAMILY, size);
                } else {
                    font = Font.loadFont(FontUtil.class.getResourceAsStream(BOLD_FONT_FILENAME), size);
                }
                boldFonts.put(size, font);
            }
            return boldFonts.get(size);
        } else {
            if (!regularFonts.containsKey(size)) {
                Font font;
                if (installLocally) {
                    font = Font.font(REGULAR_FONT_FAMILY, size);
                } else {
                    font = Font.loadFont(FontUtil.class.getResourceAsStream(REGULAR_FONT_FILENAME), size);
                }
                regularFonts.put(size, font);
            }
            return regularFonts.get(size);
        }
    }

    /**
     * 根据父布局高度，计算可容纳的最大字体大小
     *
     * @param parentHeight 父布局高度
     * @return 最大字体大小
     */
    private static int getMaxSize(double parentHeight) {
        if (parentHeight <= MIN_FONT_SIZE) {
            return MIN_FONT_SIZE;
        } else {
            int multiple = (int) (parentHeight / BASE_FONT_SIZE);
            //如果可容纳的字体大小正好和父布局相等，字体主动缩小一号，避免顶格显示
            if (parentHeight % BASE_FONT_SIZE == 0) {
                multiple -= 1;
            }
            return multiple * BASE_FONT_SIZE;
        }
    }
}
