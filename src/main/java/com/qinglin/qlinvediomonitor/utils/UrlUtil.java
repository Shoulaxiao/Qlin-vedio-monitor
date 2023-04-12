package com.qinglin.qlinvediomonitor.utils;

import com.alibaba.fastjson.JSON;
import com.qinglin.qlinvediomonitor.model.VideoDto;
import com.qinglin.qlinvediomonitor.model.constants.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname UrlUtil
 * @Description
 * @date 2023/4/11 17:24
 */
@Slf4j
public class UrlUtil {


    public static String parseVideoPlayUrl(HttpServletRequest request, Long id) {
        String domain;
        String remoteUrl = request.getHeader("x-forwarded-host");
        log.info("x-forwarded-host={}", remoteUrl);
        if (remoteUrl != null && remoteUrl.contains(Constant.LOCALHOST)) {
            domain = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        } else if (request.getServerPort() == Constant.HTTP_PORT) {
            domain = request.getScheme() + "://" + request.getServerName() + request.getContextPath();
        } else {
            domain = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        }
        String result = domain + Constant.PLAY_VIDEO_INTERFACE + id;

        log.info("parseVideoPlayUrl={}", result);
        return result;
    }
}
