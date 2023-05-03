package com.qinglin.qlinvediomonitor;

import com.qinglin.qlinvediomonitor.stream.ActionConfig;
import com.qinglin.qlinvediomonitor.stream.RecordRtmpHandleAndPushRemote;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegLogCallback;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname VideoPushTest
 * @Description
 * @date 2023/4/19 10:31
 */
@Slf4j
public class VideoPushTest extends QlinVedioMonitorApplicationTests {

    @Resource
    private RecordRtmpHandleAndPushRemote recordRtmpHandleAndPushRemote;

    @Value("${stmp.server.address}")
    private String stmpRecordAddress;

    private static final String MP4_FILE_PATH = "/Users/shoulaxiao/Movies/Normal.People.S01E01.WEBrip.720P.mp4";

    @Test
    public void pushVideo() {
        ActionConfig config = ActionConfig.builder()
                .pushUrl(stmpRecordAddress)
                .sourceUrl(MP4_FILE_PATH)
                .build();
        recordRtmpHandleAndPushRemote.action(config);
    }
}
