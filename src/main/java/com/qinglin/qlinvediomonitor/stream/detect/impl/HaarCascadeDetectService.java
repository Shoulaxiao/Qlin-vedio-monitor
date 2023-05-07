package com.qinglin.qlinvediomonitor.stream.detect.impl;

import com.qinglin.qlinvediomonitor.enums.VideoTypeEnum;
import com.qinglin.qlinvediomonitor.model.FrameResult;
import com.qinglin.qlinvediomonitor.stream.detect.AbstractVideoDetect;
import com.qinglin.qlinvediomonitor.stream.detect.DetectService;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier;
import org.springframework.core.io.ClassPathResource;


/**
 * @author willzhao
 * @version 1.0
 * @description 音频相关的服务
 * @date 2021/12/3 8:09
 */
@Slf4j
public class HaarCascadeDetectService extends AbstractVideoDetect {

    private VideoTypeEnum videoType;
    /**
     * 每一帧原始图片的对象
     */
    private Mat grabbedImage = null;

    /**
     * 原始图片对应的灰度图片对象
     */
    private Mat grayImage = null;

    /**
     * 分类器
     */
    private CascadeClassifier classifier;

    /**
     * 转换器
     */
    private OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();

    /**
     * 模型文件的下载地址
     */
    private String modelFileUrl;

    /**
     * 构造方法，在此指定模型文件的下载地址
     *
     * @param modelFileUrl
     */
    public HaarCascadeDetectService(String modelFileUrl, VideoTypeEnum videoType) throws Exception {
        this.modelFileUrl = modelFileUrl;
        this.videoType = videoType;
        this.init();
    }

    @Override
    protected FrameResult output(Frame frame) {
        log.info("检测成功，类型=[{}]",videoType.getDesc());
        return new FrameResult(frame, true, videoType);
    }

    /**
     * 音频采样对象的初始化
     *
     * @throws Exception
     */
    @Override
    public void init() throws Exception {
        // 下载模型文件
        ClassPathResource classPathResource = new ClassPathResource(modelFileUrl);
        // 模型文件下载后的完整地址
        String classifierName = classPathResource.getFile().getAbsolutePath();
        // 根据模型文件实例化分类器
        classifier = new CascadeClassifier(classifierName);
    }

    @Override
    public FrameResult convert(Frame frame) {
        // 由帧转为Mat
        grabbedImage = converter.convert(frame);

        // 灰度Mat，用于检测
        if (null == grayImage) {
            grayImage = DetectService.buildGrayImage(grabbedImage);
        }

        // 进行人脸识别，根据结果做处理得到预览窗口显示的帧
        return detect(classifier, converter, frame, grabbedImage, grayImage);
    }

    /**
     * 程序结束前，释放人脸识别的资源
     */
    @Override
    public void releaseOutputResource() {
        if (null != grabbedImage) {
            grabbedImage.release();
        }

        if (null != grayImage) {
            grayImage.release();
        }

        if (null == classifier) {
            classifier.close();
        }
    }

    @Override
    public FrameResult detect(Mat frontFrame, Mat afterFrame) {
        return null;
    }
}
