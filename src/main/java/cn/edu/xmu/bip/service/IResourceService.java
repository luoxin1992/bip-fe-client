/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.service;

import cn.edu.xmu.bip.meta.ResourceTypeEnum;
import javafx.concurrent.Task;

import java.util.List;

/**
 * 资源Service
 * <p>
 * 远程资源更新/下载/校验
 * 本地资源获取
 *
 * @author luoxin
 * @version 2017-7-2
 */
public interface IResourceService extends IBaseService {
    /**
     * (程序启动时)更新资源阶段1:检查
     * 将本地文件丢失、损坏，或校验字段与服务端不一致的资源标记为“删除”
     */
    Task<Void> checkUpdate();

    /**
     * (程序启动时)更新资源阶段2:下载
     * 下载远程新增的(即本地缺失的)资源
     */
    Task<Void> downloadUpdate();

    /**
     * (程序运行时)获取资源
     *
     * @param type  类型
     * @param url   URL
     * @param asUri 返回URI
     * @return 资源文件路径
     */
    String get(ResourceTypeEnum type, String url, boolean asUri);

    /**
     * (程序运行时)批量获取资源
     *
     * @param type  类型
     * @param urls  URL
     * @param asUri 返回URI
     * @return 资源文件路径
     */
    List<String> get(ResourceTypeEnum type, List<String> urls, boolean asUri);
}
