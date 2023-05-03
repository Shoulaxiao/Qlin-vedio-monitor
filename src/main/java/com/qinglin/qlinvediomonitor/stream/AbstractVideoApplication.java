package com.qinglin.qlinvediomonitor.stream;

import com.qinglin.qlinvediomonitor.enums.VideoTypeEnum;
import com.qinglin.qlinvediomonitor.stream.detect.impl.HaarCascadeDetectService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacpp.avutil;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bytedeco.javacpp.opencv_core.*;


@Slf4j
public abstract class AbstractVideoApplication {

    protected VideoDetectHandler videoDetectHandler;


    protected ActionConfig config;

    /**
     * 摄像头序号，如果只有一个摄像头，那就是0
     */
    protected static final int CAMERA_INDEX = 0;

    /**
     * 帧抓取器
     */
    protected FrameGrabber grabber;

    /**
     * 输出帧率
     */
    @Getter
    @Setter
    private double frameRate = 30;

    /**
     * 摄像头视频的宽
     */
    @Getter
    @Setter
    private int cameraImageWidth = 1280;

    /**
     * 摄像头视频的高
     */
    @Getter
    @Setter
    private int cameraImageHeight = 720;

    /**
     * 转换器
     */
    private final OpenCVFrameConverter.ToIplImage openCVConverter = new OpenCVFrameConverter.ToIplImage();

    /**
     * 实例化、初始化输出操作相关的资源
     */
    protected abstract void initOutput() throws Exception;

    /**
     * 输出
     */
    protected abstract void output(Frame frame) throws Exception;

    /**
     * 释放输出操作相关的资源
     */
    protected abstract void releaseOutputResource() throws Exception;

    protected void initDetectHandler() throws Exception {
        // 检测服务的初始化操作
        videoDetectHandler = new VideoDetectHandler();
        HaarCascadeDetectService faceDetect = new HaarCascadeDetectService("haarcascade_frontalface_alt.xml", VideoTypeEnum.CHARACTER_FACE);
        HaarCascadeDetectService bodyDetect = new HaarCascadeDetectService("haarcascade_upperbody.xml",VideoTypeEnum.CHARACTER_MOVE);
        videoDetectHandler.addHandler(faceDetect)
                .addHandler(bodyDetect);
    }
    /**
     * 两帧之间的间隔时间
     *
     * @return
     */
    protected int getInterval() {
        // 假设一秒钟15帧，那么两帧间隔就是(1000/15)毫秒
        return (int) (1000 / frameRate);
    }

    /**
     * 实例化帧抓取器，默认OpenCVFrameGrabber对象，
     * 子类可按需要自行覆盖
     *
     */
    protected void instanceGrabber() {
        if (config.getCameraIndex() >= 0) {
            log.info("开始初始化本地摄像头视频源");
            grabber = new OpenCVFrameGrabber(CAMERA_INDEX);
        } else {
            grabber = new OpenCVFrameGrabber(config.getSourceUrl());
        }
    }

    /**
     * 用帧抓取器抓取一帧，默认调用grab()方法，
     * 子类可以按需求自行覆盖
     *
     * @return
     */
    protected Frame grabFrame() throws FrameGrabber.Exception {
        return grabber.grab();
    }

    /**
     * 初始化帧抓取器
     *
     * @throws Exception
     */
    protected void initGrabber() throws Exception {
        // 实例化帧抓取器
        instanceGrabber();
        // 开启抓取器
        grabber.start();
        this.cameraImageHeight = grabber.getImageHeight();
        this.cameraImageWidth = grabber.getImageWidth();
        this.frameRate = (int) grabber.getFrameRate();

    }

    /**
     * 预览和输出
     *
     * @throws Exception
     */
    private void grabAndOutput() throws Exception {
        int grabSeconds = config.getGrabSeconds();
        // 添加水印时用到的时间工具
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long endTime = System.currentTimeMillis() + 1000L * grabSeconds;

        // 两帧输出之间的间隔时间，默认是1000除以帧率，子类可酌情修改
        int interVal = getInterval();

        // 水印在图片上的位置
        Point point = new Point(15, 35);

        Frame captureFrame;
        IplImage img;
        Mat mat;

        // 超过指定时间就结束循环
        long startTime = System.currentTimeMillis();
        try {
            while (grabber.grab() != null || System.currentTimeMillis() < endTime) {
                // 取一帧
                captureFrame = grabFrame();

                if (null == captureFrame) {
                    log.error("帧对象为空");
                    break;
                }
                log.info("帧对象的信息，宽度[{}],高度[{}]",captureFrame.imageWidth,captureFrame.imageHeight);
                // 将帧对象转为IplImage对象
                img = openCVConverter.convert(captureFrame);
                // IplImage转mat
                mat = new Mat(img);

                // 在图片上添加水印，水印内容是当前时间，位置是左上角
                opencv_imgproc.putText(mat,
                        simpleDateFormat.format(new Date()),
                        point,
                        opencv_imgproc.CV_FONT_VECTOR0,
                        0.8,
                        new Scalar(255, 255, 255, 0),
                        2,
                        0,
                        false);
                output(openCVConverter.convert(mat));
                // 适当间隔，让肉感感受不到闪屏即可
                if (interVal > 0) {
                    Thread.sleep(interVal);
                }
            }

        }catch (Exception e){
            log.error("推送异常",e);
            throw e;
        }finally {
            log.info("推送完成，耗时[{}]秒",
                    (System.currentTimeMillis() - startTime) / 1000);
        }

    }

    /**
     * 释放所有资源
     */
    private void safeRelease() {
        try {
            // 子类需要释放的资源
            releaseOutputResource();
        } catch (Exception exception) {
            log.error("do releaseOutputResource error", exception);
        }

        if (null != grabber) {
            try {
                grabber.close();
            } catch (Exception exception) {
                log.error("close grabber error", exception);
            }
        }
    }

    /**
     * 整合了所有初始化操作
     *
     * @throws Exception
     */
    private void init() throws Exception {
        long startTime = System.currentTimeMillis();

        // 设置ffmepg日志级别
        avutil.av_log_set_level(avutil.AV_LOG_ERROR);
        FFmpegLogCallback.set();

        // 实例化、初始化帧抓取器
        initGrabber();
        // 实例化、初始化输出操作相关的资源，
        // 具体怎么输出由子类决定，例如窗口预览、存视频文件等
        initOutput();
        log.info("初始化完成，耗时[{}]毫秒，帧率[{}]，图像宽度[{}]，图像高度[{}]",
                System.currentTimeMillis() - startTime,
                frameRate,
                cameraImageWidth,
                cameraImageHeight);
    }

    /**
     * 执行抓取和输出的操作
     */
    public void action(ActionConfig config) {
        try {
            this.config = config;
            // 初始化操作
            init();
            // 持续拉取和推送
            grabAndOutput();
        } catch (Exception exception) {
            log.error("execute action error", exception);
        } finally {
            // 无论如何都要释放资源
            safeRelease();
        }
    }
}
