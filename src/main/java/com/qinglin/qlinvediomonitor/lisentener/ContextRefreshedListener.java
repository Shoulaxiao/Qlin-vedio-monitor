package com.qinglin.qlinvediomonitor.lisentener;

import com.qinglin.qlinvediomonitor.stream.VideoStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

    ExecutorService executorService = new ThreadPoolExecutor(1, 2, 10L, TimeUnit.MINUTES, new LinkedBlockingDeque<>(8));

    @Value("${stmp.server.address}")
    private String stmpRecordAddress;

    @Resource(name = "localVideoStreamer")
    private VideoStream localVideoStreamer;

    /**
     * 本地MP4文件的完整路径
     */
    private static final String MP4_FILE_PATH = "/Users/shoulaxiao/Movies/Normal.People.S01E01.WEBrip.720P.mp4";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("开始直播推流");
        executorService.submit(() -> localVideoStreamer.pushStream(MP4_FILE_PATH, stmpRecordAddress));
    }
}
