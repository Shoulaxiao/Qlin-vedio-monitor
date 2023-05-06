package com.qinglin.qlinvediomonitor;

import org.bytedeco.javacpp.avformat;
import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacv.*;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;
import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.javacpp.opencv_core.CV_8UC1;
import static org.bytedeco.javacpp.opencv_core.absdiff;
import static org.bytedeco.javacpp.opencv_imgproc.*;

public class MoveCheck {

    final static String MP4 = "G:\\pr\\MVI_1563_Trim_x264.mp4";

    static CanvasFrame canvas = new CanvasFrame("摄像头窗口");//新建一个窗口


    public static void main(String[] args) throws FrameGrabber.Exception, InterruptedException {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(MP4);

        grabber.start();


        avformat.AVFormatContext avFormatContext = grabber.getFormatContext();

        // 取得视频的帧率
        int frameRate = (int) grabber.getVideoFrameRate();
        int interval = 1000 / frameRate;

        canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        canvas.setAlwaysOnTop(true);

        Frame captureFrame;
        Mat frame;
        Mat tempFrame = null;
        Mat res;
        int count = 0;


        while ((captureFrame = grabber.grab()) != null) {
            count++;
            if (captureFrame.image != null) {
                frame = Java2DFrameUtils.toMat(captureFrame);
                if (count == 1) {
                    res = moveCheck(frame, frame);
                } else {
                    res = moveCheck(tempFrame, frame);
                }
                tempFrame = frame.clone();

            }
            Thread.sleep(interval);
        }
    }

    private static Mat moveCheck(Mat frontMat, Mat afterMat) {
        Mat frontGray = new Mat(frontMat.rows(), frontMat.cols(), CV_8UC1);
        Mat afterGray = new Mat(afterMat.rows(), afterMat.cols(), CV_8UC1);
        Mat diffGray =  new Mat(afterMat.rows(), afterMat.cols(), CV_8UC1);
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

        MatVector matVector = new MatVector();
        findContours(diffGray, matVector, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE, new Point(0, 0));

        showImg(diffGray);
        return null;
    }


    private static void showImg(Mat mat) {

        canvas.showImage(Java2DFrameUtils.toFrame(mat));    //获取摄像头图像并放到窗口上显示， 这里的Frame frame=grabber.grab(); frame是一帧视频图像
    }
}
