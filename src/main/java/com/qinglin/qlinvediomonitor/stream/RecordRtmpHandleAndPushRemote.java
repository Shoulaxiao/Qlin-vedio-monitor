package com.qinglin.qlinvediomonitor.stream;

import com.qinglin.qlinvediomonitor.model.FrameResult;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.avformat;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component(value = "recordRtmpHandleAndPushRemote")
public class RecordRtmpHandleAndPushRemote extends AbstractVideoApplication {

    protected FFmpegFrameRecorder recorder;

    protected long startRecordTime = 0L;


    @Override
    protected void initOutput() throws Exception {
        // 实例化FFmpegFrameRecorder，将SRS的推送地址传入
//        recorder = FrameRecorder.createDefault(config.getPushUrl(), getCameraImageWidth(), getCameraImageHeight());
        if (grabber instanceof FFmpegFrameGrabber) {
            FFmpegFrameGrabber fFmpegFrameGrabber = (FFmpegFrameGrabber) grabber;
            avformat.AVFormatContext avFormatContext = fFmpegFrameGrabber.getFormatContext();
            // 文件内有几个媒体流（一般是视频流+音频流）
            int streamNum = avFormatContext.nb_streams();
            // 没有媒体流就不用继续了
            if (streamNum < 1) {
                log.error("文件内不存在媒体流");
                return;
            }
            // 取得视频的帧率
            int frameRate = (int) fFmpegFrameGrabber.getVideoFrameRate();
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
        }
        recorder = new FFmpegFrameRecorder(config.getPushUrl(), getCameraImageWidth(), getCameraImageHeight(), getAudioChannel());

        recorder.setVideoOption("tune", "zerolatency");
//        // ultrafast对CPU消耗最低
        recorder.setVideoOption("preset", "ultrafast");
//        // Constant Rate Factor (see: https://trac.ffmpeg.org/wiki/Encode/H.264)
        recorder.setVideoOption("crf", "28");
        // 2000 kb/s, reasonable "sane" area for 720
//        recorder.setVideoBitrate(2000000);
        recorder.setVideoBitrate(getVideoBitrate());

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
        initDetectHandler();
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
        videoDetectHandler.release();
    }

    @Override
    protected int getInterval() {
        // 相比本地预览，推流时两帧间隔时间更短
        return super.getInterval() / 4;
    }

    /**
     * 直播流不做处理，避免延迟
     * @param frame
     * @return
     */
    @Override
    protected FrameResult frameDetect(Frame frame) {
        FrameResult frameResult = new FrameResult();
        frameResult.setFrame(frame);
        return frameResult;
    }

    @Override
    protected opencv_core.IplImage frame2Img(Frame frame) {
        return Java2DFrameUtils.toIplImage(frame);
    }

    @Override
    protected Frame mat2Frame(opencv_core.Mat mat) {
        return Java2DFrameUtils.toFrame(mat);
    }
}