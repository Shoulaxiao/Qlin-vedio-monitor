package com.qinglin.qlinvediomonitor.service;

import com.qinglin.qlinvediomonitor.exception.MonitorException;

import java.io.InputStream;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname MediaManageService
 * @Description
 * @date 2023/4/11 18:13
 */
public interface MediaManageService {

    /**
     * 上传服务
     * @param inputStream
     * @return
     */
    String upLoad(InputStream inputStream) throws MonitorException;

}
