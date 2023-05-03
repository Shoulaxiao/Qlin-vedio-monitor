package com.qinglin.qlinvediomonitor.stream;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameRecorder;
import org.springframework.stereotype.Component;

@Slf4j
//@Component(value = "recordRtmpHandleAndPushRemote")
public class RecordRtmpHandleAndPushRemote extends AbstractVideoApplication {

    protected FrameRecorder recorder;

    protected long startRecordTime = 0L;

    @Override
    protected void initOutput() throws Exception {
        // 实例化FFmpegFrameRecorder，将SRS的推送地址传入
        recorder = FrameRecorder.createDefault(config.getPushUrl(), getCameraImageWidth(), getCameraImageHeight());

        recorder.setVideoOption("tune", "zerolatency");
        // ultrafast对CPU消耗最低
        recorder.setVideoOption("preset", "ultrafast");
        // Constant Rate Factor (see: https://trac.ffmpeg.org/wiki/Encode/H.264)
        recorder.setVideoOption("crf", "28");
        // 2000 kb/s, reasonable "sane" area for 720
        recorder.setVideoBitrate(2000000);
        // 设置编码格式
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        // 设置封装格式
        recorder.setFormat("flv");

        // FPS (frames per second)
        // 一秒内的帧数
        recorder.setFrameRate(getFrameRate());
        // Key frame interval, in our case every 2 seconds -> 30 (fps) * 2 = 60
        // 关键帧间隔
        recorder.setGopSize((int) getFrameRate() * 2);

        // 帧录制器开始初始化
        recorder.start();
    }

    @Override
    protected void output(Frame frame) throws Exception {
        if (0L == startRecordTime) {
            startRecordTime = System.currentTimeMillis();
        }
        // 时间戳
        recorder.setTimestamp(1000 * (System.currentTimeMillis() - startRecordTime));
        //推流
        recorder.record(frame);
    }

    @Override
    protected void releaseOutputResource() throws Exception {
        recorder.close();
    }

    @Override
    protected int getInterval() {
        // 相比本地预览，推流时两帧间隔时间更短
        return super.getInterval() / 4;
    }
}