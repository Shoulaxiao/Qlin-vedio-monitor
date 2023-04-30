package com.qinglin.qlinvediomonitor.stream;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.avformat;
import org.bytedeco.javacv.*;
import org.springframework.stereotype.Component;

//docker run -d --name nginx-hls -p 1935:1935 -p 8887:8887 -v $HOME/Documents/nginx/nginx.conf:/etc/nginx/nginx.conf alqutami/rtmp-hls
@Slf4j
@Component("localVideoStreamer")
public class LocalVideoStreamer extends AbstractVideoStreamer implements VideoStream {

    @Override
    public void pushStream(String source, String targetAddress) {
        FFmpegLogCallback.set();
        FFmpegFrameGrabber grabber = null;
        FFmpegFrameRecorder recorder = null;
        try {
            // 实例化帧抓取器对象，将文件路径传入
            grabber = initFrameGrabber(source);
            // grabber.start方法中，初始化的解码器信息存在放在grabber的成员变量oc中
            avformat.AVFormatContext avFormatContext = grabber.getFormatContext();
            // 文件内有几个媒体流（一般是视频流+音频流）
            int streamNum = avFormatContext.nb_streams();
            // 没有媒体流就不用继续了
            if (streamNum < 1) {
                log.error("文件内不存在媒体流");
                return;
            }
            // 取得视频的帧率
            int frameRate = (int) grabber.getVideoFrameRate();
            log.info("视频帧率[{}]，视频时长[{}]秒，媒体流数量[{}]",
                    frameRate,
                    avFormatContext.duration() / 1000000,
                    avFormatContext.nb_streams());

            // 遍历每一个流，检查其类型
            for (int i = 0; i < streamNum; i++) {
                avformat.AVStream avStream = avFormatContext.streams(i);
                avcodec.AVCodecParameters avCodecParameters = avStream.codecpar();
                log.info("流的索引[{}]，编码器类型[{}]，编码器ID[{}]", i, avCodecParameters.codec_type(), avCodecParameters.codec_id());
            }
            // 实例化FFmpegFrameRecorder，将SRS的推送地址传入
            recorder = initFrameRecorder(grabber, targetAddress, frameRate);
            doStartPush(frameRate, grabber, recorder);
        } catch (Exception e) {
            log.error("推流异常", e);
        } finally {
            close(recorder, grabber);
        }
    }

    private void doStartPush(int frameRate, FFmpegFrameGrabber grabber, FFmpegFrameRecorder recorder) throws FrameRecorder.Exception, FrameGrabber.Exception {
        Frame frame;

        long startTime = System.currentTimeMillis();
        log.info("开始推流");
        long videoTS = 0;

        int videoFrameNum = 0;
        int audioFrameNum = 0;
        int dataFrameNum = 0;

        // 假设一秒钟15帧，那么两帧间隔就是(1000/15)毫秒
        int interVal = 1000 / frameRate;
        // 发送完一帧后sleep的时间，不能完全等于(1000/frameRate)，不然会卡顿，
        // 要更小一些，这里取八分之一
        interVal /= 8;

        // 持续从视频源取帧
        while (null != (frame = grabber.grab())) {
            videoTS = 1000 * (System.currentTimeMillis() - startTime);
            // 时间戳
            if (0 != grabber.getTimestamp()) {
                recorder.setTimestamp(grabber.getTimestamp());
            }
            // recorder.setTimestamp(videoTS);
            if (null != frame.image) {
                videoFrameNum++;
            }
            if (null != frame.samples) {
                audioFrameNum++;
            }
            // 取出的每一帧，都推送到SRS
            recorder.record(frame);

            // 停顿一下再推送
            try {
                Thread.sleep(interVal);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        log.info("推送完成，视频帧[{}]，音频帧[{}]，数据帧[{}]，耗时[{}]秒",
                videoFrameNum,
                audioFrameNum,
                dataFrameNum,
                (System.currentTimeMillis() - startTime) / 1000);
    }

    private void close(FFmpegFrameRecorder recorder, FFmpegFrameGrabber grabber) {
        try {
            // 关闭帧录制器
            recorder.close();
            // 关闭帧抓取器
            grabber.close();
        } catch (FrameRecorder.Exception | FrameGrabber.Exception e) {
            log.error("关闭异常");
        }
    }
}
