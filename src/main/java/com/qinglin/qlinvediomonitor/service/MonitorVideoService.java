package com.qinglin.qlinvediomonitor.service;

import com.qinglin.qlinvediomonitor.common.PageResult;
import com.qinglin.qlinvediomonitor.exception.MonitorException;
import com.qinglin.qlinvediomonitor.model.VideoDto;
import com.qinglin.qlinvediomonitor.model.VideoReq;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname MonitorVideoService
 * @Description
 * @date 2023/4/11 14:59
 */
public interface MonitorVideoService {

    PageResult<VideoDto> queryPage (VideoReq req);

    /**
     * 查询对应视频的播放地址
     * @param id
     * @return
     */
    String getPlayerUrl(Long id) throws MonitorException;

    VideoDto getPlayerDetail(Long id) throws MonitorException;
}
