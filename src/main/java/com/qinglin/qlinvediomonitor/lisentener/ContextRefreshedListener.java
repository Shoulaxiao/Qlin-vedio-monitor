package com.qinglin.qlinvediomonitor.lisentener;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.qinglin.qlinvediomonitor.stream.ActionConfig;
import com.qinglin.qlinvediomonitor.stream.RecordRtmpHandleAndPushRemote;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.*;

@Component
@Slf4j
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

    private ThreadFactory namedFactory = new ThreadFactoryBuilder().setNameFormat("thread-videoHandle-%d").build();


    ExecutorService executorService = new ThreadPoolExecutor(
            2,
            4,
            10L,
            TimeUnit.MINUTES,
            new LinkedBlockingDeque<>(8)
            , namedFactory);

    @Value("${stmp.server.address}")
    private String stmpRecordAddress;
//
//    @Resource(name = "recordRtmpHandleAndPushRemote")
//    private RecordRtmpHandleAndPushRemote recordRtmpHandleAndPushRemote;

    /**
     * 本地MP4文件的完整路径
     */
    private static final String MP4_FILE_PATH = "/Users/shoulaxiao/Movies/Normal.People.S01E01.WEBrip.720P.mp4";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("开始直播推流");
        ActionConfig config = ActionConfig.builder()
                .pushUrl(stmpRecordAddress)
                .sourceUrl(MP4_FILE_PATH)
                .build();
//        executorService.submit(() -> recordRtmpHandleAndPushRemote.action(config));
    }
}
