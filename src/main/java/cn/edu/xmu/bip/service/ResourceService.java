/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.service;

import cn.com.lx1992.lib.client.util.DigestUtil;
import cn.com.lx1992.lib.client.util.HttpUtil;
import cn.edu.xmu.bip.dao.ResourceDAO;
import cn.edu.xmu.bip.result.ResourceListItemResult;
import cn.edu.xmu.bip.ui.splash.model.LoadingModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * 资源Service
 * <p>
 * 远程资源检查更新/下载/校验
 * 本地资源读取
 *
 * @author luoxin
 * @version 2017-7-2
 */
public class ResourceService {
    //资源状态：0=删除/1=正常/2=待校验/3=待下载
    private static final int STATUS_DELETE = 0;
    private static final int STATUS_NORMAL = 1;
    private static final int STATUS_VERIFY_WAIT = 2;
    private static final int STATUS_DOWNLOAD_WAIT = 3;

    private final Logger logger = LoggerFactory.getLogger(ResourceService.class);

    @Inject
    private ApiService apiService= new ApiService();
    @Inject
    private ResourceDAO resourceDAO = new ResourceDAO();

    @Inject
    private LoadingModel loadingModel;
    private ExecutorService executors;

    @PostConstruct
    public void initial() {
        int threads = Runtime.getRuntime().availableProcessors();
        executors = Executors.newFixedThreadPool(threads);
    }

    @PreDestroy
    public void destroy() {
        executors.shutdown();
    }

    public void updateResource() {
        CompletableFuture<Void> updateResourceFuture = CompletableFuture.runAsync(this::getResourceList, executors);
        //TODO
        //List<ResourceListItemResult> resources = new ArrayList<>();
        //resources.forEach(resource -> {
        //    String filename = resource.getUri().substring(resource.getUri().lastIndexOf("/"));
        //    //1. 检查数据库
        //    ResourceQueryModel model = queryResourceInfo(resource.getUri());
        //    if (model == null) {
        //        //2. 下载资源文件
        //        //String urlPrefix = ApiService.class;
        //        String pathPrefix = NativeUtil.getAppDataPath();
        //        //downloadResourceFile(resource.getUri());
        //    } else {
        //        //3. 检查资源文件
        //        boolean result = verifyResourceFile(model.getPath(), model.getMd5());
        //        if (result) {
        //            //4. 更新可用资源计数器
        //        } else {
        //            //5. 删除废弃的资源
        //            deleteResourceInDb(model.getId());
        //            //6. 重新下载
        //        }
        //    }
        //});
    }

    public void getResourceList() {
        //apiService.invokeResourceList(
        //        result -> {
        //            //loadingModel.setStatus(MessageFormat.format("更新资源文件{0}/{1}", 0, result.getTotal()));
        //            classifyResource(result.getList());
        //        },
        //        error -> AlertUtil.showError(error, nil -> System.exit(1), true));
    }

    /**
     * 将获取的资源分类
     * 写入本地数据库，以status区分
     *
     * @param resources 获取的资源
     */
    private void classifyResource(List<ResourceListItemResult> resources) {
        //resources.parallelStream().forEach(resource -> {
        //    try {
        //        ResourceQueryModel domain = resourceDAO.selectUrl(apiService.getApiBaseUrl() + resource.getUri());
        //        logger.info("test {}", domain);
        //        if (domain != null) {
        //            if (Objects.equals(domain.getType(), resource.getType())
        //                    && Objects.equals(domain.getMd5(), resource.getMd5())) {
        //                //本地数据库记录正确：等待校验具体文件
        //                resourceDAO.updateStatus(domain.getId(), STATUS_VERIFY_WAIT);
        //                return;
        //            } else {
        //                //本地数据库记录错误(type或md5不相符)：删除已有资源并重新下载
        //                resourceDAO.updateStatus(domain.getId(), STATUS_DELETE);
        //            }
        //        }
        //        //此资源尚未下载过/需要重新下载
        //        domain = new ResourceQueryModel();
        //        domain.setType(resource.getType());
        //        domain.setUrl(apiService.getApiBaseUrl() + resource.getUri());
        //        domain.setStatus(STATUS_DOWNLOAD_WAIT);
        //        resourceDAO.insert(domain);
        //    } catch (SQLException e) {
        //        e.printStackTrace();
        //    }
        //});
    }

    /**
     * 检查本地数据库中的资源记录
     *
     * @param url  下载地址
     * @param type 类型
     * @param md5  MD5
     * @return true-数据库中存在相符记录，false-数据库中不存在相符记录
     */
    private boolean checkResourceRecord(String url, String type, String md5) {
        //try {
        //    ResourceQueryModel domain = resourceDAO.selectUrl(url);
        //    //检查规则：1.数据库中记录存在；2.类型(type)与接口返回相符；3.MD5与接口返回相符
        //    return domain != null && Objects.equals(type, domain.getType()) && !Objects.equals(md5, domain.getMd5());
        //} catch (SQLException e) {
        //    logger.error("query resource {} failure", url, e);
        //    return false;
        //}
        return false;
    }

    /**
     * 校验资源文件MD5
     *
     * @param path 文件路径
     * @param md5  正确MD5
     * @return true-校验通过，false-校验不通过
     */
    private boolean verifyResourceFile(String path, String md5) {
        logger.info("expect file {} has md5 digest {}", path, md5);
        File file = new File(path);
        if (!(file.isFile() && file.exists())) {
            logger.warn("file {} not exist", path);
            return false;
        }
        try {
            return Objects.equals(DigestUtil.getFileMD5(path), md5);
        } catch (IOException e) {
            logger.error("verify file {} md5 digest failure", path, e);
            return false;
        }
    }

    private void downloadResourceFile(String url, String path, Consumer<Void> onSuccess) {
        logger.info("download resource file form {} to {}", url, path);
        HttpUtil.executeGetAsync(url, path, length -> {
            logger.info("download resource file {} success, size {}", url, length);
            onSuccess.accept(null);
        }, errorCode -> {
        }, errorInfo -> {
        });
    }

    /**
     * 将新下载的资源写入数据库
     *
     * @param type     类型
     * @param url      下载地址
     * @param path     保存路径
     * @param filename 文件名
     * @param md5      MD5
     */
    public void createResourceInDb(String type, String url, String path, String filename, String md5) {
        //ResourceQueryModel domain = new ResourceQueryModel();
        //domain.setType(type);
        //domain.setUrl(url);
        //domain.setPath(path);
        //domain.setMd5(md5);
        //
        //try {
        //    int id = resourceDAO.insert(domain);
        //    logger.info("create new resource {} successful", id);
        //} catch (SQLException e) {
        //    logger.error("create new resource failure", e);
        //}
    }

    /**
     * 将废弃的资源从数据库删除
     *
     * @param id ID
     */
    public void deleteResourceInDb(int id) {
        //try {
        //    boolean result = resourceDAO.updateStatus(id, STATUS_DELETE);
        //    logger.info("delete old resource {} result {}", id, result);
        //} catch (SQLException e) {
        //    logger.error("delete old resource failure", e);
        //}
    }
}
