/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.job;

import cn.com.lx1992.lib.client.util.DateTimeUtil;
import cn.edu.xmu.bip.ui.main.model.ClockModel;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 主界面时钟更新任务
 *
 * @author luoxin
 * @version 2017-8-4
 */
public class ClockUpdateJob extends ScheduledService<List<Integer>> {
    private static final int YEAR_INDEX = 0;
    private static final int MONTH_INDEX = 1;
    private static final int DAY_INDEX = 2;
    private static final int HOUR_INDEX = 3;
    private static final int MINUTE_INDEX = 4;
    private static final int SECOND_INDEX = 5;

    @Inject
    private ClockModel clockModel;

    @Override
    protected Task<List<Integer>> createTask() {
        return new Task<List<Integer>>() {
            @Override
            protected List<Integer> call() throws Exception {
                LocalDateTime now = DateTimeUtil.getNow();

                List<Integer> result = new ArrayList<>();
                result.add(YEAR_INDEX, now.getYear());
                result.add(MONTH_INDEX, now.getMonthValue());
                result.add(DAY_INDEX, now.getDayOfMonth());
                result.add(HOUR_INDEX, now.getHour());
                result.add(MINUTE_INDEX, now.getMinute());
                result.add(SECOND_INDEX, now.getSecond());
                return result;
            }

            @Override
            protected void succeeded() {
                List<Integer> result = getValue();
                String date = String.format("%04d-%02d-%02d",
                        result.get(YEAR_INDEX), result.get(MONTH_INDEX), result.get(DAY_INDEX));
                String time = String.format("%02d:%02d:%02d",
                        result.get(HOUR_INDEX), result.get(MINUTE_INDEX), result.get(SECOND_INDEX));

                clockModel.setDate(date);
                clockModel.setTime(time);
                clockModel.setHour(result.get(HOUR_INDEX));
                clockModel.setMinute(result.get(MINUTE_INDEX));
                clockModel.setSecond(result.get(SECOND_INDEX));
            }
        };
    }
}
