package com.qinglin.qlinvediomonitor.stream;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;

@Slf4j
public abstract class AbstractVideoStreamer {


    FFmpegFrameGrabber initFrameGrabber(String sourcePath) throws FrameGrabber.Exception {
        long startTime = System.currentTimeMillis();
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(sourcePath);
        log.info("开始初始化帧抓取器");
        // 初始化帧抓取器，例如数据结构（时间戳、编码器上下文、帧对象等），
        // 如果入参等于true，还会调用avformat_find_stream_info方法获取流的信息，放入AVFormatContext类型的成员变量oc中
        grabber.start();
        log.info("帧抓取器初始化完成，耗时[{}]毫秒", System.currentTimeMillis() - startTime);
        return grabber;
    }


    FFmpegFrameRecorder initFrameRecorder(FFmpegFrameGrabber grabber, String targetAddress, int frameRate) throws FrameRecorder.Exception {

        // 视频宽度
        int frameWidth = grabber.getImageWidth();
        // 视频高度
        int frameHeight = grabber.getImageHeight();
        // 音频通道数量
        int audioChannels = grabber.getAudioChannels();

        // 实例化FFmpegFrameRecorder，将SRS的推送地址传入
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(targetAddress,
                frameWidth,
                frameHeight,
                audioChannels);

        log.info("视频宽度[{}]，视频高度[{}]，音频通道数[{}]",
                frameWidth,
                frameHeight,
                audioChannels);

        // 设置编码格式
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);

        // 设置封装格式
        recorder.setFormat("flv");

        // 一秒内的帧数
        recorder.setFrameRate(frameRate);

        // 两个关键帧之间的帧数
        recorder.setGopSize(frameRate);
        // 设置音频通道数，与视频源的通道数相等
        recorder.setAudioChannels(grabber.getAudioChannels());

        long startTime = System.currentTimeMillis();
        log.info("开始初始化帧抓取器");

        // 初始化帧录制器，例如数据结构（音频流、视频流指针，编码器），
        // 调用av_guess_format方法，确定视频输出时的封装方式，
        // 媒体上下文对象的内存分配，
        // 编码器的各项参数设置
        recorder.start();

        log.info("帧录制初始化完成，耗时[{}]毫秒", System.currentTimeMillis() - startTime);
        return recorder;
    }




}
