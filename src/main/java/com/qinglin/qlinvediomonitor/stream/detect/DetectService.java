package com.qinglin.qlinvediomonitor.stream.detect;

import com.qinglin.qlinvediomonitor.model.FrameResult;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier;

import static org.bytedeco.javacpp.opencv_core.CV_8UC1;


/**
 * @author willzhao
 * @version 1.0
 * @description 检测工具的通用接口
 * @date 2021/12/5 10:57
 */
public interface DetectService {


    /**
     * 根据传入的MAT构造相同尺寸的MAT，存放灰度图片用于以后的检测
     *
     * @param src 原始图片的MAT对象
     * @return 相同尺寸的灰度图片的MAT对象
     */
    static Mat buildGrayImage(Mat src) {
        return new Mat(src.rows(), src.cols(), CV_8UC1);
    }


    /**
     * 检测图片，将检测结果用矩形标注在原始图片上
     *
     * @param classifier   分类器
     * @param converter    Frame和mat的转换器
     * @param rawFrame     原始视频帧
     * @param grabbedImage 原始视频帧对应的mat
     * @param grayImage    存放灰度图片的mat
     * @return 标注了识别结果的视频帧
     */
      FrameResult detect(CascadeClassifier classifier,
                               OpenCVFrameConverter.ToMat converter,
                               Frame rawFrame,
                               Mat grabbedImage,
                               Mat grayImage);



    /**
     * 初始化操作，例如模型下载
     *
     * @throws Exception
     */
    void init() throws Exception;

    /**
     * 得到原始帧，做识别，添加框选
     *
     * @param frame
     * @return
     */
    FrameResult convert(Frame frame);

    /**
     * 释放资源
     */
    void releaseOutputResource();

    /**
     * 检测两帧之间的是否发生变化
     * @param frontFrame
     * @param afterFrame
     * @return
     */
    FrameResult detect(Mat frontFrame, Mat afterFrame);
}
