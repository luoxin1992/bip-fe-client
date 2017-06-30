/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.admin.service;

import cn.com.lx1992.lib.client.util.DateTimeUtil;
import cn.edu.xmu.bip.common.dao.FingerprintLogDAO;
import cn.edu.xmu.bip.common.dao.MessageLogDAO;
import cn.edu.xmu.bip.common.dao.ResourceDAO;
import cn.edu.xmu.bip.common.domain.FingerprintLogDO;
import cn.edu.xmu.bip.common.domain.MessageLogDO;
import cn.edu.xmu.bip.common.domain.ResourceDO;
import cn.edu.xmu.bip.common.meta.FingerprintLogTypeEnum;
import cn.edu.xmu.bip.common.meta.MessageLogTypeEnum;
import cn.edu.xmu.bip.common.meta.ResourceTypeEnum;
import cn.edu.xmu.bip.ui.admin.model.FingerprintLogModel;
import cn.edu.xmu.bip.ui.admin.model.MessageLogModel;
import cn.edu.xmu.bip.ui.admin.model.ResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据浏览Service
 *
 * @author luoxin
 * @version 2017-6-29
 */
public class DataBrowsingService {
    private static final int PAGE_SIZE = 30;

    private final Logger logger = LoggerFactory.getLogger(DataBrowsingService.class);

    @Inject
    private FingerprintLogDAO fingerprintLogDAO;
    @Inject
    private MessageLogDAO messageLogDAO;
    @Inject
    private ResourceDAO resourceDAO;

    public int pagingFingerprintLog(LocalDate start, LocalDate end) {
        long minTimestamp = DateTimeUtil.convert(DateTimeUtil.getDateTimeAtStartOfDay(start));
        long maxTimestamp = DateTimeUtil.convert(DateTimeUtil.getDateTimeAtEndOfDay(end));

        try {
            int count = fingerprintLogDAO.count(minTimestamp, maxTimestamp);
            logger.info("{} fingerprint log(s) in timestamp [{}, {}]", count, minTimestamp, maxTimestamp);
            return count % PAGE_SIZE == 0 ? count / PAGE_SIZE : count / PAGE_SIZE + 1;
        } catch (SQLException e) {
            logger.error("paging fingerprint log failed", e);
            return 0;
        }
    }

    public List<FingerprintLogModel> queryFingerprintLogs(int page, LocalDate start, LocalDate end) {
        int offset = page > 0 ? (page - 1) * PAGE_SIZE : 1;
        long minTimestamp = DateTimeUtil.convert(DateTimeUtil.getDateTimeAtStartOfDay(start));
        long maxTimestamp = DateTimeUtil.convert(DateTimeUtil.getDateTimeAtEndOfDay(end));

        try {
            List<FingerprintLogDO> domains = fingerprintLogDAO.select(minTimestamp, maxTimestamp, offset, PAGE_SIZE);
            logger.info("query {} fingerprint log(s)", domains.size());
            return domains.stream()
                    .map(domain -> {
                        FingerprintLogModel model = new FingerprintLogModel();
                        model.setId(domain.getId());
                        model.setType(FingerprintLogTypeEnum.getDescriptionByType(domain.getType()));
                        model.setContent(domain.getContent());
                        model.setTimestamp(DateTimeUtil.format(domain.getTimestamp()));
                        return model;
                    })
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            logger.error("query fingerprint log failed", e);
            return Collections.emptyList();
        }
    }

    public int pagingMessageLog(LocalDate start, LocalDate end) {
        long minTimestamp = DateTimeUtil.convert(DateTimeUtil.getDateTimeAtStartOfDay(start));
        long maxTimestamp = DateTimeUtil.convert(DateTimeUtil.getDateTimeAtEndOfDay(end));

        try {
            int count = messageLogDAO.count(minTimestamp, maxTimestamp);
            logger.info("{} message log(s) in timestamp [{}, {}]", count, minTimestamp, maxTimestamp);
            return count % PAGE_SIZE == 0 ? count / PAGE_SIZE : count / PAGE_SIZE + 1;
        } catch (SQLException e) {
            logger.error("paging message log failed", e);
            return 0;
        }
    }

    public List<MessageLogModel> queryMessageLogs(int page, LocalDate start, LocalDate end) {
        int offset = page > 0 ? (page - 1) * PAGE_SIZE : 1;
        long minTimestamp = DateTimeUtil.convert(DateTimeUtil.getDateTimeAtStartOfDay(start));
        long maxTimestamp = DateTimeUtil.convert(DateTimeUtil.getDateTimeAtEndOfDay(end));

        try {
            List<MessageLogDO> domains = messageLogDAO.select(minTimestamp, maxTimestamp, offset, PAGE_SIZE);
            logger.info("query {} message log(s)", domains.size());
            return domains.stream()
                    .map(domain -> {
                        MessageLogModel model = new MessageLogModel();
                        model.setId(domain.getId());
                        model.setType(MessageLogTypeEnum.getDescriptionByType(domain.getType()));
                        model.setBody(domain.getBody());
                        model.setTimestamp(DateTimeUtil.format(domain.getTimestamp()));
                        return model;
                    })
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            logger.error("query message log failed", e);
            return Collections.emptyList();
        }
    }

    public int pagingResource(LocalDate start, LocalDate end) {
        long minTimestamp = DateTimeUtil.convert(DateTimeUtil.getDateTimeAtStartOfDay(start));
        long maxTimestamp = DateTimeUtil.convert(DateTimeUtil.getDateTimeAtEndOfDay(end));

        try {
            int count = resourceDAO.count(minTimestamp, maxTimestamp);
            logger.info("{} resource(s) in timestamp [{}, {}]", count, minTimestamp, maxTimestamp);
            return count % PAGE_SIZE == 0 ? count / PAGE_SIZE : count / PAGE_SIZE + 1;
        } catch (SQLException e) {
            logger.error("paging resource failed", e);
            return 0;
        }
    }

    public List<ResourceModel> queryResources(int page, LocalDate start, LocalDate end) {
        int offset = page > 0 ? (page - 1) * PAGE_SIZE : 1;
        long minTimestamp = DateTimeUtil.convert(DateTimeUtil.getDateTimeAtStartOfDay(start));
        long maxTimestamp = DateTimeUtil.convert(DateTimeUtil.getDateTimeAtEndOfDay(end));

        try {
            List<ResourceDO> domains = resourceDAO.select(minTimestamp, maxTimestamp, offset, PAGE_SIZE);
            logger.info("query {} resource(s)", domains.size());
            return domains.stream()
                    .map(domain -> {
                        ResourceModel model = new ResourceModel();
                        model.setId(domain.getId());
                        model.setType(ResourceTypeEnum.getDescriptionByType(domain.getType()));
                        model.setUrl(domain.getUrl());
                        model.setPath(domain.getPath());
                        model.setFilename(domain.getFilename());
                        model.setMd5(domain.getMd5());
                        model.setTimestamp(DateTimeUtil.format(domain.getTimestamp()));
                        return model;
                    })
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            logger.error("query resource failed", e);
            return Collections.emptyList();
        }
    }
}
