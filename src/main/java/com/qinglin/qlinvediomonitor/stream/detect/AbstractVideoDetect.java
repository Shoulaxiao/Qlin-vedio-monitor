package com.qinglin.qlinvediomonitor.stream.detect;

import com.qinglin.qlinvediomonitor.enums.VideoTypeEnum;
import com.qinglin.qlinvediomonitor.model.FrameResult;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_imgproc.CV_AA;

public abstract class AbstractVideoDetect implements DetectService{

    @Override
    public FrameResult detect(opencv_objdetect.CascadeClassifier classifier, OpenCVFrameConverter.ToMat converter, Frame rawFrame, opencv_core.Mat grabbedImage, opencv_core.Mat grayImage) {
        // 当前图片转为灰度图片
        cvtColor(grabbedImage, grayImage, CV_BGR2GRAY);

        // 存放检测结果的容器
        opencv_core.RectVector objects = new opencv_core.RectVector();

        // 开始检测
        classifier.detectMultiScale(grayImage, objects);

        // 检测结果总数
        long total = objects.size();

        // 如果没有检测到结果，就用原始帧返回
        if (total < 1) {
            return new FrameResult(rawFrame, false, VideoTypeEnum.STATIC);
        }

        // 如果有检测结果，就根据结果的数据构造矩形框，画在原图上
        for (long i = 0; i < total; i++) {
            opencv_core.Rect r = objects.get(i);
            int x = r.x(), y = r.y(), w = r.width(), h = r.height();
            rectangle(grabbedImage, new opencv_core.Point(x, y), new opencv_core.Point(x + w, y + h), opencv_core.Scalar.RED, 1, CV_AA, 0);
        }

        // 释放检测结果资源
        objects.close();

        // 将标注过的图片转为帧，返回
        return output(converter.convert(grabbedImage));
    }

    protected abstract FrameResult output(Frame frame);


    @Override
    public void init() throws Exception {

    }

    @Override
    public FrameResult convert(Frame frame) {
        return null;
    }

    @Override
    public void releaseOutputResource() {

    }
}
