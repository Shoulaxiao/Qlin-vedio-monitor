package com.qinglin.qlinvediomonitor;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;


import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.bytedeco.javacpp.opencv_core.CV_8UC1;
import static org.bytedeco.javacpp.opencv_imgproc.*;


@Slf4j
public class PreviewCameraHogDetect extends AbstractCameraApplication{
    /**
     * 本机窗口
     */
    protected CanvasFrame previewCanvas;

    private OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();

    String classifierName = null;
    private opencv_core.Mat grabbedImage;
    private opencv_core.Mat grayImage;
    private opencv_objdetect.HOGDescriptor hogDescriptor;

    @Override
    protected void initOutput() throws IOException {
        previewCanvas = new CanvasFrame("摄像头预览", CanvasFrame.getDefaultGamma() / grabber.getGamma());
        previewCanvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        previewCanvas.setAlwaysOnTop(true);

        if (null==classifierName) {
            URL url = new URL("https://raw.github.com/opencv/opencv/master/data/hogcascades/hogcascade_pedestrians.xml");
            File file = Loader.cacheResource(url);
            classifierName = file.getAbsolutePath();
        }

        hogDescriptor = new opencv_objdetect.HOGDescriptor();
        hogDescriptor.setSVMDetector(new opencv_core.Mat(opencv_objdetect.HOGDescriptor.getDefaultPeopleDetector()));


        grabbedImage = converter.convert(grabber.grab());
        int height = grabbedImage.rows();
        int width = grabbedImage.cols();

        grayImage = new opencv_core.Mat(height, width, CV_8UC1);
    }

    @Override
    protected void output(Frame frame) {
        grabbedImage = converter.convert(frame);

        cvtColor(grabbedImage, grayImage, CV_BGR2GRAY);

        opencv_core.RectVector faces = new opencv_core.RectVector();
        DoublePointer weights = new DoublePointer();


        hogDescriptor.detectMultiScale(grayImage, faces, weights,
                0.5,
                new opencv_core.Size(2, 2), //winSize
                new opencv_core.Size(0, 0), //blocksize
                1.05,
                2,false);




        long total = faces.size();

        if (total<1) {
            previewCanvas.showImage(frame);
            return;
        }

        for (long i = 0; i < total; i++) {
            opencv_core.Rect r = faces.get(i);
            int x = r.x(), y = r.y(), w = r.width(), h = r.height();
            rectangle(grabbedImage, new opencv_core.Point(x, y), new opencv_core.Point(x + w, y + h), opencv_core.Scalar.RED, 1, CV_AA, 0);
        }

        for(int i=0;i<weights.limit();i++) {
            System.out.println("weight : " + weights.get(i));

        }


        // 预览窗口上显示当前帧
        previewCanvas.showImage(converter.convert(grabbedImage));
    }

    @Override
    protected int getInterval() {
        return 0;
    }

    @Override
    protected void releaseOutputResource() {
        if (null!= previewCanvas) {
            previewCanvas.dispose();
        }
    }

    public static void main(String[] args) {
        new PreviewCameraHogDetect().action(1000);
    }
}
