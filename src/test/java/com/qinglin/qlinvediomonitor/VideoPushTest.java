package com.qinglin.qlinvediomonitor;

import com.qinglin.qlinvediomonitor.stream.LocalVideoStreamer;
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
    private LocalVideoStreamer localVideoStreamer;

    @Value("${stmp.server.address}")
    private String stmpRecordAddress;

    private static final String MP4_FILE_PATH = "/Users/shoulaxiao/Movies/Normal.People.S01E01.WEBrip.720P.mp4";

    @Test
    public void pushVideo() {
        localVideoStreamer.pushStream(MP4_FILE_PATH, stmpRecordAddress);
    }

    @Test
    public void test() {
        try {
            Class<?> aClass = FFmpegLogCallback.class.getClassLoader().loadClass("org.bytedeco.javacv.FFmpegLogCallback");
            FFmpegLogCallback fFmpegLogCallback1 = (FFmpegLogCallback) aClass.newInstance();
            System.out.println(fFmpegLogCallback1);
        } catch (ClassNotFoundException e) {
            log.info("error", e);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
