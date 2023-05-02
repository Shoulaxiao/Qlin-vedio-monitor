package com.qinglin.qlinvediomonitor;


import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import java.io.File;
import java.io.IOException;

public class ImgTest {


    public static void main(String[] args) throws  Exception, IOException {
        OpenCVFrameGrabber frameGrabber = new OpenCVFrameGrabber(
                new File("C:\\Users\\huanjiejie\\Videos\\Captures\\Let Go - Beau Young Prince 2023-02-09 00-44-09.mp4"));
        frameGrabber.start();
        Frame origImg = frameGrabber.grab();

        //create canvas frame named 'Demo'
        final CanvasFrame canvas = new CanvasFrame("Demo");

        //Show image in canvas frame
        canvas.showImage(origImg);

        //This will close canvas frame on exit
        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    }
}
