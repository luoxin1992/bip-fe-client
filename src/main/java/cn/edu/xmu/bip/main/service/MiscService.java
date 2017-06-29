/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.main.service;

import cn.edu.xmu.bip.main.model.ClockModel;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 其他杂项服务
 *
 * @author luoxin
 * @version 2017-6-21
 */
public class MiscService {
    private final Logger logger = LoggerFactory.getLogger(MiscService.class);

    private ScheduledExecutorService clockExecutor;

    @Inject
    private ClockModel clockModel;

    @PostConstruct
    public void initial() {
        clockExecutor = Executors.newSingleThreadScheduledExecutor();
        clockExecutor.scheduleAtFixedRate(clockValueUpdateTask(), 0, 1, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void destroy() {
        clockExecutor.shutdown();
    }

    private Runnable clockValueUpdateTask() {
        return () -> {
            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.now();
            Platform.runLater(() -> {
                clockModel.setDate(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(date));
                clockModel.setTime(DateTimeFormatter.ofPattern("HH:mm:ss").format(time));
                clockModel.setHour(time.getHour() % 12);
                clockModel.setMinute(time.getMinute());
                clockModel.setSecond(time.getSecond());
            });
        };
    }
}
