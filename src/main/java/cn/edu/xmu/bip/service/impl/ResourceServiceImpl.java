/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.service.impl;

import cn.com.lx1992.lib.client.constant.CommonConstant;
import cn.com.lx1992.lib.client.util.CrashUtil;
import cn.com.lx1992.lib.client.util.HttpUtil;
import cn.com.lx1992.lib.client.util.NativeUtil;
import cn.edu.xmu.bip.dao.IResourceDAO;
import cn.edu.xmu.bip.dao.factory.DAOFactory;
import cn.edu.xmu.bip.domain.ResourceDO;
import cn.edu.xmu.bip.meta.ResourceTypeEnum;
import cn.edu.xmu.bip.result.ResourceListResult;
import cn.edu.xmu.bip.service.IAPIService;
import cn.edu.xmu.bip.service.IResourceService;
import cn.edu.xmu.bip.service.factory.ServiceFactory;
import cn.edu.xmu.bip.ui.splash.model.LoadingModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.concurrent.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 资源Service
 * <p>
 * 远程资源更新/下载/校验
 * 本地资源读取
 *
 * @author luoxin
 * @version 2017-7-2
 */
public class ResourceServiceImpl implements IResourceService {
    //查询全部已存在资源 返回URL类型
    private static final int WITH_ABSOLUTE_URL = 0;
    private static final int WITH_RELATIVE_URL = 1;
    //获取资源头部信息 HTTP头名称
    private static final int HEADER_COUNT = 2;
    private static final String HEADER_LAST_MODIFIED = "last-modified";
    private static final String HEADER_CONTENT_LENGTH = "content-length";

    private final Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);

    @Inject
    private ServiceFactory serviceFactory;
    @Inject
    private DAOFactory daoFactory;
    @Inject
    private LoadingModel loadingModel;

    @Override
    public Task<Void> checkUpdate() {
        return new Task<Void>() {
            @Override
            protected void scheduled() {
                String message = "检查资源文件";
                loadingModel.setMessage(message);

                Callable<String> progress =
                        () -> getProgress() >= 0 ? MessageFormat.format("({0}%)", (int) (getProgress() * 100)) : null;
                StringBinding progressBinding = Bindings.createStringBinding(progress, progressProperty());
                loadingModel.progressProperty().bind(progressBinding);
            }

            @Override
            protected Void call() throws Exception {
                //进度指示
                AtomicInteger total = new AtomicInteger(0);
                AtomicInteger current = new AtomicInteger(0);

                //获取已存在资源
                List<ResourceDO> domains = getAllExistence(WITH_ABSOLUTE_URL);
                total.set(domains.size());

                //逐一检查 更新进度
                domains.forEach(domain -> {
                    checkExistence(domain);
                    updateProgress(current.addAndGet(1), total.get());
                });

                logger.info("complete resource update CHECK stage");
                return null;
            }

            @Override
            protected void succeeded() {
                loadingModel.progressProperty().unbind();
                loadingModel.setProgress(null);
            }

            @Override
            protected void failed() {
                CrashUtil.logCrashAndExit(getException());
            }
        };
    }

    @Override
    public Task<Void> downloadUpdate() {
        return new Task<Void>() {
            @Override
            protected void scheduled() {
                String message = "下载资源文件";
                loadingModel.setMessage(message);

                Callable<String> progress =
                        () -> getProgress() >= 0 ? MessageFormat.format("({0}%)", (int) (getProgress() * 100)) : null;
                StringBinding progressBinding = Bindings.createStringBinding(progress, progressProperty());
                loadingModel.progressProperty().bind(progressBinding);
            }

            @Override
            protected Void call() throws Exception {
                AtomicInteger total = new AtomicInteger(0);
                AtomicInteger current = new AtomicInteger(0);

                //获取已存在资源的下载地址
                List<String> urls = getAllExistence(WITH_RELATIVE_URL).stream()
                        .map(ResourceDO::getUrl)
                        .collect(Collectors.toList());

                //获取待下载资源
                Map<String, String> downloads = getAllMissing(urls);
                total.set(downloads.size());

                //逐一下载 更新进度
                downloads.forEach((url, type) -> {
                    String urlPrefix = ((IAPIService) serviceFactory.getInstance(ServiceFactory.API)).getUrlPrefix();
                    String pathPrefix = NativeUtil.getAppDataDirectoryPath(null);

                    downloadMissing(url, urlPrefix + url, pathPrefix + url);
                    updateProgress(current.addAndGet(1), total.get());
                });

                logger.info("complete resource update DOWNLOAD stage");
                return null;
            }

            @Override
            protected void succeeded() {
                loadingModel.progressProperty().unbind();
                loadingModel.setProgress(null);
            }

            @Override
            protected void failed() {
                CrashUtil.logCrashAndExit(getException());
            }
        };
    }

    @Override
    public String get(ResourceTypeEnum type, String url, boolean asUri) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }

        IAPIService apiService = (IAPIService) serviceFactory.getInstance(ServiceFactory.API);
        String urlPrefix = apiService.getUrlPrefix();
        String pathPrefix = NativeUtil.getAppDataDirectoryPath(null);

        //先查本地库
        IResourceDAO resourceDAO = (IResourceDAO) daoFactory.getInstance(DAOFactory.RESOURCE);
        ResourceDO domain = resourceDAO.selectOne(type.getType(), urlPrefix + url);
        if (domain != null && checkExistence(domain)) {
            //校验通过直接返回
            return asUri ? Paths.get(domain.getPath()).toUri().toString() : domain.getPath();
        }
        //后重新下载
        if (downloadMissing(type.getType(), urlPrefix + url, pathPrefix + url)) {
            return asUri ? Paths.get(pathPrefix + url).toUri().toString() : pathPrefix + url;
        }
        return null;
    }

    @Override
    public List<String> get(ResourceTypeEnum type, List<String> urls, boolean asUri) {
        return urls.stream()
                .map(url -> get(type, url, asUri))
                .collect(Collectors.toList());
    }

    /**
     * 查询全部已存在资源
     *
     * @param urlType 返回URL类型
     * @return 查询结果
     */
    private List<ResourceDO> getAllExistence(int urlType) {
        IResourceDAO resourceDAO = (IResourceDAO) daoFactory.getInstance(DAOFactory.RESOURCE);
        Stream<ResourceDO> stream = resourceDAO.selectAll().stream()
                //过滤掉与当前配置的API不同源的
                .filter(domain -> {
                    IAPIService apiService = (IAPIService) serviceFactory.getInstance(ServiceFactory.API);
                    return domain.getUrl().startsWith(apiService.getUrlPrefix());
                });
        if (urlType == WITH_RELATIVE_URL) {
            stream = stream.peek(domain -> {
                IAPIService apiService = (IAPIService) serviceFactory.getInstance(ServiceFactory.API);
                domain.setUrl(domain.getUrl().replace(apiService.getUrlPrefix(), CommonConstant.EMPTY_STRING));
            });
        }
        return stream.collect(Collectors.toList());
    }

    /**
     * 检查已存在资源
     *
     * @param domain 资源实体
     * @return true-通过，false-不通过
     */
    private boolean checkExistence(ResourceDO domain) {
        IResourceDAO resourceDAO = (IResourceDAO) daoFactory.getInstance(DAOFactory.RESOURCE);
        if (!verifyFile(domain.getPath(), domain.getLength()) ||
                !verifyHeader(domain.getUrl(), domain.getLength(), domain.getModify())) {
            resourceDAO.delete(domain.getId());
            return false;
        }
        return true;
    }

    /**
     * 检查本地文件是否存在、大小是否一致
     *
     * @param path   文件路径
     * @param length 文件大小
     * @return true-通过，false-不通过
     */
    private boolean verifyFile(String path, long length) {
        File file = new File(path);
        if (!(file.exists() && file.isFile() && file.canRead())) {
            logger.warn("resource {} file not exist", path);
            return false;
        } else if (file.length() != length) {
            logger.warn("resource {} length inconsistent", path);
            return false;
        }
        return true;
    }

    /**
     * 检查本地记录头部信息与远程是否一致
     *
     * @param url    URL
     * @param length 资源大小
     * @param modify 修改时间
     * @return true-一致，false-不一致
     */
    private boolean verifyHeader(String url, long length, String modify) {
        Map<String, String> headers = getHeader(url);
        if (!(Objects.equals(headers.get(HEADER_CONTENT_LENGTH), String.valueOf(length))
                && Objects.equals(headers.get(HEADER_LAST_MODIFIED), modify))) {
            logger.warn("resource {} header inconsistent", url);
            return false;
        }
        return true;
    }

    /**
     * 获取资源头部信息(辅助校验)
     *
     * @param url 下载地址
     * @return 校验字段
     */
    private Map<String, String> getHeader(String url) {
        try {
            Map<String, List<String>> headers = HttpUtil.executeHead(url);
            //只需要content-length和last-modified两个header
            return headers.entrySet().stream()
                    .filter(entry -> Objects.equals(entry.getKey(), HEADER_CONTENT_LENGTH)
                            || Objects.equals(entry.getKey(), HEADER_LAST_MODIFIED))
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get(0)));
        } catch (Exception e) {
            //HttpUtil抛出异常
            logger.error("get header for {} failed", url, e);
            return Collections.emptyMap();
        }
    }

    /**
     * 查询全部缺失资源
     *
     * @param urls 已存在资源下载地址
     * @return 查询结果
     */
    private Map<String, String> getAllMissing(List<String> urls) {
        IAPIService apiService = (IAPIService) serviceFactory.getInstance(ServiceFactory.API);
        return apiService.invokeResourceList().getList().stream()
                //过滤掉已存在的资源
                .filter(resource -> !urls.contains(resource.getUrl()))
                .collect(Collectors.toMap(ResourceListResult.Item::getUrl, ResourceListResult.Item::getType));
    }

    /**
     * 下载缺失资源
     *
     * @param type 类型
     * @param url  下载地址
     * @param path 保存路径
     * @return true-成功，false-失败
     */
    private boolean downloadMissing(String type, String url, String path) {
        //获取头部信息
        Map<String, String> headers = getHeader(url);
        if (headers.size() == HEADER_COUNT) {
            Long length = Long.valueOf(headers.get(HEADER_CONTENT_LENGTH));
            String modify = headers.get(HEADER_LAST_MODIFIED);
            //下载文件
            if (getFile(url, path, length, false)) {
                IResourceDAO resourceDAO = (IResourceDAO) daoFactory.getInstance(DAOFactory.RESOURCE);
                resourceDAO.insert(type, url, path, length, modify);
                return true;
            }
        }
        return false;
    }

    /**
     * 下载文件
     *
     * @param url    下载地址
     * @param path   保存路径
     * @param length 预期大小
     * @param retry  重试标识(外部调用传false)
     * @return true-成功，false-失败
     */
    private boolean getFile(String url, String path, long length, boolean retry) {
        try {
            if (Objects.equals(HttpUtil.executeGet(url, path), length)) {
                return true;
            }
            //当下载的长度与预期不一致时有一次重试机会
            logger.warn("download file from {} length inconsistent, retry: {}", url, !retry);
            return !retry && getFile(url, path, length, true);
        } catch (Exception e) {
            logger.error("download file from {} to {} failed", url, path, e);
            return false;
        }
    }
}
