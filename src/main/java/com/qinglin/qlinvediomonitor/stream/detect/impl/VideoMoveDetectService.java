package com.qinglin.qlinvediomonitor.stream.detect.impl;

import com.qinglin.qlinvediomonitor.model.FrameResult;
import com.qinglin.qlinvediomonitor.stream.detect.AbstractVideoDetect;
import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.bytedeco.javacv.OpenCVFrameConverter;

import javax.swing.*;

import static org.bytedeco.javacpp.opencv_core.CV_8UC1;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;

public class VideoMoveDetectService extends AbstractVideoDetect {

    Mat frontGray;
    Mat afterGray;
    Mat diffGray;

    public VideoMoveDetectService(Mat src) {
        frontGray = new Mat(src.rows(), src.cols(), CV_8UC1);
        afterGray = new Mat(src.rows(), src.cols(), CV_8UC1);
        diffGray = new Mat(src.rows(), src.cols(), CV_8UC1);
    }

    @Override
    public FrameResult detect(opencv_objdetect.CascadeClassifier classifier, OpenCVFrameConverter.ToMat converter, Frame rawFrame, Mat grabbedImage, Mat grayImage) {
        // 当前图片转为灰度图片
        CanvasFrame canvasFrame = new CanvasFrame("运动监测");
        canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvasFrame.setAlwaysOnTop(true);
        canvasFrame.setResizable(true);
        cvtColor(grabbedImage, grayImage, CV_BGR2GRAY);

        canvasFrame.showImage(Java2DFrameUtils.toFrame(frontGray));

        return null;
    }



    @Override
    protected FrameResult output(Frame frame) {
        return null;
    }
}
