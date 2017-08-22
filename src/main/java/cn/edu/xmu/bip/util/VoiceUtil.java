/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 音频播放工具类
 *
 * @author luoxin
 * @version 2017-7-4
 */
public class VoiceUtil {
    public static final int LOW_PRIORITY = 0;
    public static final int HIGH_PRIORITY = 1;

    private static final Logger logger = LoggerFactory.getLogger(VoiceUtil.class);

    //索引：指向播放列表中当前正在播放的文件，-1表示未在播放
    private static int index;
    //枢轴：指向播放列表中最后一个高优先级文件，-1表示当前所有文件均为低优先级
    private static int pivot;
    private static MediaPlayer mediaPlayer;
    private static List<String> playlist;

    static {
        index = -1;
        pivot = -1;
        playlist = new ArrayList<>();
    }

    private VoiceUtil() {
    }

    /**
     * 插入新文件到播放列表
     * <p>
     * 1 播放列表为空：插入新文件，立即开始播放
     * 2 播放列表非空
     * 2.1 插入高优先级文件：清空当前播放列表，插入新文件，重新开始播放
     * 2.2 插入低优先级文件
     * 2.2.1 当前正在播放高优先级文件：删除余下所有低优先级文件，将新文件插入至余下高优先级文件末尾
     * 2.2.2 当前正在播放低优先级文件：跳过余下所有低优先级文件，插入新文件，重新开始播放
     *
     * @param paths    新文件路径
     * @param priority 播放优先级
     */
    public static synchronized void add(List<String> paths, int priority) {
        if (playlist.isEmpty()) {
            //1
            index = -1;
            pivot = priority == HIGH_PRIORITY ? paths.size() - 1 : -1;
            playlist.addAll(paths);
            next();
        } else {
            //2
            if (priority == HIGH_PRIORITY) {
                //2.1
                index = -1;
                pivot = paths.size() - 1;
                playlist.clear();
                playlist.addAll(paths);
                stop();
                next();
            } else {
                //2.2
                if (index <= pivot) {
                    //2.2.1
                    playlist = playlist.subList(0, pivot + 1);
                    playlist.addAll(paths);
                } else {
                    //2.2.2
                    index = playlist.size() - 1;
                    playlist.addAll(paths);
                    stop();
                    next();
                }
            }
        }
    }

    /**
     * 停止播放
     */
    private static void stop() {
        if (isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
    }

    /**
     * 开始播放index指向的文件
     */
    private static void play() {
        mediaPlayer = new MediaPlayer(new Media(Paths.get(playlist.get(index)).toUri().toString()));
        mediaPlayer.setAutoPlay(true);
        //发生错误或播放完成后跳到下一个文件
        mediaPlayer.setOnError(() -> {
            logger.error("media file {} playback failed", playlist.get(index), mediaPlayer.getError());
            next();
        });
        mediaPlayer.setOnEndOfMedia(() -> {
            logger.info("media file {} playback end", playlist.get(index));
            next();
        });
    }

    /**
     * 播放下一个文件(如果有的话)
     */
    private static void next() {
        if (index == playlist.size() - 1) {
            //已经播放完最后一个文件：清空播放列表、销毁播放器
            index = -1;
            pivot = -1;
            playlist.clear();
            stop();
        } else {
            //还有文件待播放：播放下一个文件
            index++;
            if (checkAvailable()) {
                play();
            } else {
                logger.error("media file {} unavailable", playlist.get(index));
                next();
            }
        }
    }

    /**
     * 检查播放器是否正在播放
     *
     * @return true-正在播放，false-其他
     */
    private static boolean isPlaying() {
        return mediaPlayer != null && MediaPlayer.Status.PLAYING == mediaPlayer.getStatus();
    }

    /**
     * 检查待播放的文件是否可用
     *
     * @return true-文件可用，false-其他
     */
    private static boolean checkAvailable() {
        File file = Paths.get(playlist.get(index)).toFile();
        return file.exists() && file.isFile() && file.canRead();
    }
}
