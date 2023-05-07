package com.qinglin.qlinvediomonitor.stream.detect.impl;

import com.qinglin.qlinvediomonitor.model.FrameResult;
import com.qinglin.qlinvediomonitor.stream.detect.AbstractVideoDetect;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.opencv.imgproc.Imgproc;


import static org.bytedeco.javacpp.opencv_core.CV_8UC1;
import static org.bytedeco.javacpp.opencv_core.absdiff;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_imgproc.CV_CHAIN_APPROX_SIMPLE;

@Slf4j
public class VideoMoveDetectService extends AbstractVideoDetect {

    private Mat frontGray;
    private Mat afterGray;
    private Mat diffGray;


    @Override
    public FrameResult detect(opencv_objdetect.CascadeClassifier classifier, OpenCVFrameConverter.ToMat converter, Frame rawFrame, Mat grabbedImage, Mat grayImage) {
        return null;
    }

    @Override
    public FrameResult detect(Mat frontFrame, Mat afterFrame) {
        // 当前图片转为灰度图片
        FrameResult result = new FrameResult();

        Mat diff = moveCheck(frontFrame, afterFrame);
        MatVector matVector = new MatVector();
        findContours(diff, matVector, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE, new Point(0, 0));

        // 不为空，表示两帧之间发生了变化
        result.setSuccess(!matVector.empty());
        result.setFrame(Java2DFrameUtils.toFrame(diffGray));

        return result;
    }


    @Override
    protected FrameResult output(Frame frame) {
        return null;
    }

    private Mat moveCheck(Mat frontMat, Mat afterMat) {
        frontGray = new Mat(frontMat.rows(), frontMat.cols(), CV_8UC1);
        afterGray = new Mat(afterMat.rows(), afterMat.cols(), CV_8UC1);
        diffGray = new Mat(frontMat.rows(), frontMat.cols(), CV_8UC1);

        cvtColor(frontMat, frontGray, CV_BGR2GRAY);
        cvtColor(afterMat, afterGray, CV_BGR2GRAY);

        //帧差处理 找到帧与帧之间运动的物体差异
        //缺点：会把其他运动物体也算进来
        absdiff(frontGray, afterGray, diffGray);

        //二值化：黑白分明 会产生大量白色噪点
        threshold(diffGray, diffGray, 25, 255, CV_THRESH_BINARY);

        //腐蚀处理：去除白色噪点 噪点不能完全去除，反而主要物体会被腐蚀的图案都变得不明显
        Mat element = getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        erode(diffGray, diffGray, element);

        //膨胀处理：将白色区域变“胖”
        Mat element2 = getStructuringElement(MORPH_RECT, new Size(20, 20));
        dilate(diffGray, diffGray, element2);

        return diffGray;
    }
}
