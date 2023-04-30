package com.qinglin.qlinvediomonitor;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import javax.swing.*;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname CameriaTest
 * @Description
 * @date 2023/4/19 23:12
 */
public class CameraTest {

    public static void main(String[] args) throws FrameGrabber.Exception {
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        grabber.start();

        CanvasFrame canvas = new CanvasFrame("摄像头");
        canvas.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        while (canvas.isDisplayable()){
            canvas.showImage(grabber.grab());
        }
        grabber.close();
    }
}
